package com.qiguliuxing.dts.db.service;


import com.qiguliuxing.dts.db.dao.RedemptionCodeMapper;
import com.qiguliuxing.dts.vo.RedemptionCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class RedemptionCodeService {

    @Autowired
    private RedemptionCodeMapper redemptionCodeMapper;

    public boolean createRedemptionCode() {
        RedemptionCodeVo redemptionCodeVo = new RedemptionCodeVo();
        redemptionCodeVo.setCreateTime(new Date());
        redemptionCodeVo.setUpdateTime(new Date());
        String redemptionCode = getRandomNum(12);
        redemptionCodeVo.setCode(redemptionCode);
        redemptionCodeVo.setAvailable(true);
        redemptionCodeVo.setCodeType(0);
        return redemptionCodeMapper.insert(redemptionCodeVo) > 0;
    }

    public boolean deleteRedemptionCode(String code) {
        return redemptionCodeMapper.deleteByCode(code) > 0;
    }

    public RedemptionCodeVo getRedemptionCode(String code) {
        return redemptionCodeMapper.selectByCode(code);
    }

    public List<RedemptionCodeVo> listRedemptionCodes(Integer codeType, String code, Boolean available, Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        return redemptionCodeMapper.selectAll(codeType, code, available, offset, size);
    }

    public boolean updateRedemptionCodeStatus(String code, boolean available) {
        return redemptionCodeMapper.batchUpdateStatus(Collections.singletonList(code), available) > 0;
    }

    public boolean updateRedemptionCodeType(String code, Integer codeType) {
        return redemptionCodeMapper.batchUpdateCodeType(Collections.singletonList(code), codeType) > 0;
    }

    /**
     * 批量更新兑换码类型
     */
    public int batchUpdateCodeType(List<String> codes, Integer codeType) {
        if (CollectionUtils.isEmpty(codes)) {
            return 0;
        }
        return redemptionCodeMapper.batchUpdateCodeType(codes, codeType);
    }

    /**
     * 批量更新兑换码状态
     */
    public int batchUpdateStatus(List<String> codes, Boolean available) {
        if (CollectionUtils.isEmpty(codes)) {
            return 0;
        }
        return redemptionCodeMapper.batchUpdateStatus(codes, available);
    }

    public String getRandomNum(Integer num) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        base += "0123456789";

        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        String redemptionCode = sb.toString();
        RedemptionCodeVo redemptionCodeVo = redemptionCodeMapper.selectByCode(redemptionCode);
        if (redemptionCodeVo != null) {
            redemptionCode = getRandomNum(num);
        }
        return redemptionCode;
    }
}
