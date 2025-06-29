package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.BlindBoxRecordMapper;
import com.qiguliuxing.dts.db.dao.BoxLockRecordMapper;
import com.qiguliuxing.dts.db.dao.DtsCouponUserMapper;
import com.qiguliuxing.dts.db.util.ActivityType;
import com.qiguliuxing.dts.db.util.PointsTransactionType;
import com.qiguliuxing.dts.db.util.StatusType;
import com.qiguliuxing.dts.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 盲盒记录服务
 */
@Service
public class BlindBoxRecordService {

    @Autowired
    private BlindBoxRecordMapper blindBoxRecordMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private BoxProductService boxProductService;

    @Autowired
    private BoxOrderService boxOrderService;

    @Autowired
    private DtsUserService dtsUserService;

    @Resource
    private DtsCouponUserMapper couponUserMapper;

    @Autowired
    private PointsTransactionService pointsTransactionService;


    private static final int BATCH_SIZE = 100;

    @Autowired
    private BoxLockRecordMapper lockRecordMapper;

    /**
     * 获取指定系列和箱子的开赏记录
     * @param seriesId 系列ID
     * @param boxNumber 箱子编号
     * @return 开赏记录列表，如果没有记录则返回空列表
     */
    public List<BlindBoxRecordVO> getOpenRecords(Integer seriesId, String boxNumber) {
        List<BlindBoxRecordVO> records = blindBoxRecordMapper.selectOpenRecordsBySeriesAndBox(seriesId, boxNumber);
        return CollectionUtils.isEmpty(records) ? Collections.emptyList() : records;
    }



    /**
     * 查询指定系列所有的产品列表
     * @param seriesId 系列ID
     * @param boxNumber 箱子编号
     * @return 可用产品列表
     */
    public List<ProductBoxResultVo> selectAllProducts(Integer seriesId, String boxNumber){
        return blindBoxRecordMapper.selectAllProducts(seriesId, boxNumber);
    }


    /**
     * 抽取盲盒(魂力值抽奖)
     * @param request 请求参数
     * @return
     */
    @Transactional
    public List<BlindBoxDrawResultVO> drawBlindBoxBySpiritPower(BlindBoxDrawRequestVO request) {
        // 1. 查询可用的产品列表
        List<ProductBoxResultVo> spiritPowerProducts = blindBoxRecordMapper.selectSpiritPowerProducts(
                request.getSeriesId(), request.getBoxNumber());
        if (spiritPowerProducts.isEmpty()) {
            throw new RuntimeException("该盲盒已售罄或不存在");
        }
        // 2. 准备抽取的产品池（考虑库存数量）
        List<ProductBoxResultVo> productPool = new ArrayList<>();
        for (ProductBoxResultVo product : spiritPowerProducts) {
            int availableCount = product.getQuantity() - product.getSoldQuantity();
            for (int i = 0; i < availableCount; i++) {
                productPool.add(product);
            }
        }
        if (productPool.isEmpty()) {
            throw new RuntimeException("该盲盒已售罄");
        }
        // 3. 随机抽取产品
        List<BlindBoxDrawResultVO> results = new ArrayList<>();
        Random random = new Random();
        for (Integer number : request.getNumbers()) {
            // 随机选择一个产品
            ProductBoxResultVo selectedProduct = productPool.get(random.nextInt(productPool.size()));
            // 4. 更新库存
            int updated = blindBoxRecordMapper.updateSoldQuantity(
                    selectedProduct.getBoxId(),
                    selectedProduct.getProductId(),
                    1);
            if (updated == 0) {
                throw new RuntimeException("产品库存更新失败，可能已被其他用户购买");
            }


            ProductVO product = productService.getProductById(selectedProduct.getProductId());
            BoxProductVO boxProductVO = new BoxProductVO();
            boxProductVO.setActivityType(ActivityType.SOUL_POWER.getName());
            boxProductVO.setProductId(selectedProduct.getProductId());
            boxProductVO.setProductName(selectedProduct.getProductName());
            boxProductVO.setProductImage(product.getProductImage());
            boxProductVO.setProductLevel(product.getProductLevelName());
            boxProductVO.setStatus(StatusType.PENDING.getCode());
            boxProductVO.setProductLevel(product.getProductLevelName());
            boxProductVO.setBoxNumber(request.getBoxNumber());
            boxProductVO.setObtainTime(new Date());
            boxProductVO.setCreatedTime(new Date());
            boxProductVO.setUpdatedTime(new Date());
            boxProductVO.setUserId(request.getUserId());
            boxProductVO.setProductBadge(product.getProductBadge());
            // 抽中的产品入盒柜表
            int id = boxProductService.addProduct(boxProductVO);

            // 5. 记录抽取记录
            blindBoxRecordMapper.insertDrawRecord(
                    id,
                    request.getUserId(),
                    number,
                    request.getSeriesId(),
                    request.getBoxNumber(),
                    selectedProduct.getProductId());

            //写子表数据
            BoxOrderVO boxOrderVO = new BoxOrderVO();
            boxOrderVO.setOrderAmount(BigDecimal.valueOf(request.getSpiritPower()));
            boxOrderVO.setCouponDeduction(BigDecimal.valueOf(0));
            boxOrderVO.setShippingFee(BigDecimal.valueOf(0));
            boxOrderVO.setPointDeduction(BigDecimal.valueOf(0));
            boxOrderVO.setPaymentAmount(BigDecimal.valueOf(request.getSpiritPower()));
            boxOrderVO.setRecordId(boxProductVO.getId());
            boxOrderVO.setCreatedTime(new Date());
            boxOrderVO.setPaymentTime(new Date());
            boxOrderVO.setUpdatedTime(new Date());
            boxOrderService.createBoxOrder(boxOrderVO);

            // 6. 构建返回结果
            BlindBoxDrawResultVO result = new BlindBoxDrawResultVO();
            result.setBoxId(selectedProduct.getBoxId());
            result.setBoxNumber(selectedProduct.getBoxNumber());
            result.setProductId(selectedProduct.getProductId());
            result.setProductName(selectedProduct.getProductName());
            result.setProductImage(selectedProduct.getProductImage());
            result.setProductPrice(selectedProduct.getProductPrice());
            result.setLevelName(selectedProduct.getLevelName());
            result.setNumber(number);
            results.add(result);
        }
        // 更新用户魂力值
        dtsUserService.updateSpiritPower(request.getUserId(), request.getSpiritPower(), false);
        return results;
    }

    /**
     * 抽取盲盒
     * @param wxOrderParameter 请求参数
     * @return
     */
    @Transactional
    public List<BlindBoxDrawResultVO> drawBlindBox(WxOrderParameter wxOrderParameter) {
        // 1. 查询可用的产品列表
        List<ProductBoxResultVo> availableProducts = blindBoxRecordMapper.selectAvailableProducts(
                wxOrderParameter.getSeriesId(), wxOrderParameter.getBoxNumber());
        if (availableProducts.isEmpty()) {
            throw new RuntimeException("该盲盒已售罄或不存在");
        }
        // 2. 准备抽取的产品池（考虑库存数量）
        List<ProductBoxResultVo> productPool = new ArrayList<>();
        for (ProductBoxResultVo product : availableProducts) {
            int availableCount = product.getQuantity() - product.getSoldQuantity();
            for (int i = 0; i < availableCount; i++) {
                productPool.add(product);
            }
        }
        if (productPool.isEmpty()) {
            throw new RuntimeException("该盲盒已售罄");
        }
        List<ProductBoxResultVo> filterPools = productPool.stream().filter(pool -> !pool.getLevelName().equals("终赏")).collect(Collectors.toList());
        // 3. 随机抽取产品
        List<BlindBoxDrawResultVO> results = new ArrayList<>();
        Random random = new Random();
        int spiritPower = 0;
        for (Integer number : wxOrderParameter.getNumbers()) {
            // 随机选择一个产品
            ProductBoxResultVo selectedProduct = filterPools.get(random.nextInt(filterPools.size()));
            // 4. 更新库存
            updateSoldQuantity(selectedProduct);

            ProductVO product = productService.getProductById(selectedProduct.getProductId());
            spiritPower += product.getProductSpiritPower();
            BoxProductVO boxProductVO = getBoxProductVO(wxOrderParameter, selectedProduct, product);
            // 抽中的产品入盒柜表
            int id = boxProductService.addProduct(boxProductVO);

            // 5. 记录抽取记录
            blindBoxRecordMapper.insertDrawRecord(
                    id,
                    wxOrderParameter.getUserId(),
                    number,
                    wxOrderParameter.getSeriesId(),
                    wxOrderParameter.getBoxNumber(),
                    selectedProduct.getProductId());

            //写子表数据
            BoxOrderVO boxOrderVO = getBoxOrderVO(wxOrderParameter, boxProductVO);
            boxOrderService.createBoxOrder(boxOrderVO);

            // 6. 构建返回结果
            BlindBoxDrawResultVO result = getBlindBoxDrawResultVO(number, selectedProduct);
            results.add(result);
        }


        if (wxOrderParameter.getNumbers().size() == filterPools.size()) {
            List<ProductBoxResultVo> laterProducts = productPool.stream().filter(pool -> pool.getLevelName().equals("终赏")).collect(Collectors.toList());
            // 1. 查询所有产品信息
            List<ProductBoxResultVo> productBoxResultVos = blindBoxRecordMapper.selectAllProducts(wxOrderParameter.getSeriesId(), wxOrderParameter.getBoxNumber());
            // 3. 计算总数量（所有产品数量的总和）
            int totalNumber = productBoxResultVos.stream().filter(productBoxResultVo -> !productBoxResultVo.getLevelName().equals("终赏"))
                    .mapToInt(ProductBoxResultVo::getQuantity)
                    .sum();
            for (ProductBoxResultVo laterProduct : laterProducts) {
                totalNumber++;
                // 4. 更新库存
                updateSoldQuantity(laterProduct);
                ProductVO product = productService.getProductById(laterProduct.getProductId());
                spiritPower += product.getProductSpiritPower();
                BoxProductVO boxProductVO = getBoxProductVO(wxOrderParameter, laterProduct, product);
                // 抽中的产品入盒柜表
                int id = boxProductService.addProduct(boxProductVO);
                // 5. 记录抽取记录
                blindBoxRecordMapper.insertDrawRecord(
                        id,
                        wxOrderParameter.getUserId(),
                        totalNumber,
                        wxOrderParameter.getSeriesId(),
                        wxOrderParameter.getBoxNumber(),
                        laterProduct.getProductId());
                //写子表数据
                BoxOrderVO boxOrderVO = getBoxOrderVO(wxOrderParameter, boxProductVO);
                boxOrderService.createBoxOrder(boxOrderVO);
                // 6. 构建返回结果
                BlindBoxDrawResultVO result = getBlindBoxDrawResultVO(totalNumber, laterProduct);
                results.add(result);
            }
        }

        // 更新用户魂力值
        dtsUserService.updateSpiritPower(wxOrderParameter.getUserId(), spiritPower, true);

        // 用户使用了优惠券则更新使用记录
        if (wxOrderParameter.getCouponId() != null){
            int updateCouponResult = couponUserMapper.updateCouponStatusToUsed(wxOrderParameter.getCouponId(), wxOrderParameter.getUserId(), wxOrderParameter.getWxOrderNo());
            if (updateCouponResult < 1) {
                throw new RuntimeException("优惠券使用记录更新失败");
            }
        }
        // 用户使用了积分则进行积分扣减
        if (wxOrderParameter.getPoint() != null){
            pointsTransactionService.insertPointsTransaction(wxOrderParameter.getUserId(), wxOrderParameter.getPoint(), PointsTransactionType.ORDER_DEDUCTION.getCode(), wxOrderParameter.getWxOrderNo());
        }

        // 十连抽进行处理
        if (Objects.equals(wxOrderParameter.getIds().size(), 10)) {
            handleBoxLock(wxOrderParameter.getUserId(), wxOrderParameter.getSeriesId(),wxOrderParameter.getBoxNumber(), wxOrderParameter.getLock());
        }

        return results;
    }

    private BlindBoxDrawResultVO getBlindBoxDrawResultVO(Integer number, ProductBoxResultVo selectedProduct) {
        BlindBoxDrawResultVO result = new BlindBoxDrawResultVO();
        result.setBoxId(selectedProduct.getBoxId());
        result.setBoxNumber(selectedProduct.getBoxNumber());
        result.setProductId(selectedProduct.getProductId());
        result.setProductName(selectedProduct.getProductName());
        result.setProductImage(selectedProduct.getProductImage());
        result.setProductPrice(selectedProduct.getProductPrice());
        result.setLevelName(selectedProduct.getLevelName());
        result.setNumber(number);
        return result;
    }

    private BoxOrderVO getBoxOrderVO(WxOrderParameter wxOrderParameter, BoxProductVO boxProductVO) {
        BoxOrderVO boxOrderVO = new BoxOrderVO();
        boxOrderVO.setOrderAmount(wxOrderParameter.getOrderAmount());
        boxOrderVO.setCouponDeduction(wxOrderParameter.getCouponDeduction());
        boxOrderVO.setShippingFee(BigDecimal.valueOf(0));
        boxOrderVO.setPointDeduction(wxOrderParameter.getPointDeduction());
        boxOrderVO.setPaymentAmount(wxOrderParameter.getPaymentAmount());
        boxOrderVO.setRecordId(boxProductVO.getId());
        boxOrderVO.setCreatedTime(new Date());
        boxOrderVO.setPaymentTime(new Date());
        boxOrderVO.setUpdatedTime(new Date());
        return boxOrderVO;
    }

    private BoxProductVO getBoxProductVO(WxOrderParameter wxOrderParameter, ProductBoxResultVo selectedProduct, ProductVO product) {
        BoxProductVO boxProductVO = new BoxProductVO();
        boxProductVO.setActivityType(wxOrderParameter.getActivityType());
        boxProductVO.setProductId(selectedProduct.getProductId());
        boxProductVO.setProductName(selectedProduct.getProductName());
        boxProductVO.setProductImage(product.getProductImage());
        boxProductVO.setProductLevel(product.getProductLevelName());
        boxProductVO.setStatus(StatusType.PENDING.getCode());
        boxProductVO.setProductLevel(product.getProductLevelName());
        boxProductVO.setBoxNumber(wxOrderParameter.getBoxNumber());
        boxProductVO.setObtainTime(new Date());
        boxProductVO.setCreatedTime(new Date());
        boxProductVO.setUpdatedTime(new Date());
        boxProductVO.setUserId(wxOrderParameter.getUserId());
        boxProductVO.setProductBadge(product.getProductBadge());
        boxProductVO.setWxOrderNo(wxOrderParameter.getWxOrderNo());
        return boxProductVO;
    }

    private void updateSoldQuantity(ProductBoxResultVo selectedProduct) {
        // 4. 更新库存
        int updated = blindBoxRecordMapper.updateSoldQuantity(
                selectedProduct.getBoxId(),
                selectedProduct.getProductId(),
                1);
        if (updated == 0) {
            throw new RuntimeException("产品库存更新失败，可能已被其他用户购买");
        }
    }

    /**
     * 盒柜产品提货
     */
    @Transactional
    public void handleBoxLock(String userId, Integer seriesId, String boxNumber, boolean isLockSelected) {
        // 1. 获取用户当前激活的锁
        Optional<BoxLockRecordVO> currentLockOpt = lockRecordMapper.findActiveLockByUserId(userId);

        // 2. 检查是否是同系列同箱子的连续锁定
        boolean isSameBox = currentLockOpt.isPresent() &&
                currentLockOpt.get().getSeriesId().equals(seriesId) &&
                currentLockOpt.get().getBoxNumber().equals(boxNumber);

        // 3. 计算锁定时间
        int lockMinutes;
        if (isLockSelected) {
            lockMinutes = isSameBox ? 180 : 30; // 同箱3小时，新箱30分钟
        } else {
            lockMinutes = 5; // 未选锁箱5分钟
        }

        // 4. 处理旧锁定（如果存在且不是同一个箱子）
        if (currentLockOpt.isPresent() && !isSameBox) {
            lockRecordMapper.unlockAllByUser(userId);
        }

        // 5. 创建/更新锁箱记录
        if (isSameBox && isLockSelected) {
            // 更新现有记录
            BoxLockRecordVO existing = currentLockOpt.get();
            existing.setUnlockTime(calculateUnlockTime(lockMinutes));
            existing.setLockCount(existing.getLockCount() + 1);
            lockRecordMapper.updateLock(existing);
        } else {
            // 创建新记录
            BoxLockRecordVO newRecord = new BoxLockRecordVO();
            newRecord.setSeriesId(seriesId);
            newRecord.setBoxNumber(boxNumber);
            newRecord.setUserId(userId);
            newRecord.setLockStatus(1);
            newRecord.setCreatedBy(userId);
            newRecord.setUnlockTime(calculateUnlockTime(lockMinutes));
            newRecord.setLockCount(1);
            lockRecordMapper.insert(newRecord);
        }
    }

    // 定时解锁任务
    @Scheduled(cron = "0 * * * * ?") // 每分钟执行
    @Transactional
    public void unlockBoxesTask() {
        Date now = new Date();
        int processedCount;

        do {
            // 分批获取过期锁ID
            List<Long> expiredIds = lockRecordMapper.findExpiredLockIds(now, BATCH_SIZE);
            processedCount = expiredIds.size();

            if (processedCount > 0) {
                // 批量解锁
                lockRecordMapper.unlockExpiredRecords(expiredIds);
            }

        } while (processedCount == BATCH_SIZE); // 继续处理直到没有完整批次
    }

    private Date calculateUnlockTime(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    // 查询箱子锁定状态
    public BoxLockStatusDTO getBoxLockStatus(Integer seriesId, String boxNumber, String currentUserId) {
        Optional<BoxLockRecordVO> boxLock = lockRecordMapper.findActiveLockByBox(seriesId, boxNumber);
        Optional<BoxLockRecordVO> userLock = lockRecordMapper.findActiveLockByUserId(currentUserId);

        return covertBoxLockStatusDTO(boxLock.orElse(null), userLock.orElse(null), currentUserId);
    }

    private BoxLockStatusDTO covertBoxLockStatusDTO(BoxLockRecordVO boxLock, BoxLockRecordVO userLock, String currentUserId) {
            BoxLockStatusDTO boxLockStatusDTO = new BoxLockStatusDTO();
            if (boxLock != null) {
                boxLockStatusDTO.setLocked(true);
                boxLockStatusDTO.setOwnedByCurrentUser(boxLock.getUserId().equals(currentUserId));
                boxLockStatusDTO.setUnlockTime(boxLock.getUnlockTime());
                boxLockStatusDTO.setLockCount(boxLock.getLockCount());
                boxLockStatusDTO.setLockedByUserId(boxLock.getUserId());
            } else {
                boxLockStatusDTO.setLocked(false);
                boxLockStatusDTO.setOwnedByCurrentUser(false);
            }
            return boxLockStatusDTO;
        }
}
