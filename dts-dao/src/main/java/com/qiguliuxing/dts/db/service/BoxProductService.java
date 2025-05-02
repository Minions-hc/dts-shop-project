package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.BoxProductMapper;
import com.qiguliuxing.dts.vo.BoxProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoxProductService {

    @Autowired
    private BoxProductMapper boxProductMapper;

    public List<BoxProductVO> getProductsByUserId(String userId, List<String> statusList) {
        return boxProductMapper.selectByUserId(userId, statusList);
    }

    @Transactional
    public int addProduct(BoxProductVO boxProduct) {
        return boxProductMapper.insert(boxProduct);
    }

    @Transactional
    public int updateProductStatus(BoxProductVO boxProduct) {
        return boxProductMapper.updateSelective(boxProduct);
    }
}
