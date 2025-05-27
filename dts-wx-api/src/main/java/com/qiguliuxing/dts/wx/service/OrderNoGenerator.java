package com.qiguliuxing.dts.wx.service;

import com.qiguliuxing.dts.db.dao.DtsOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class OrderNoGenerator {

    // 业务前缀
    private static final String ORDER_PREFIX = "SO";
    // 日期格式
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    // 随机数位数
    private static final int RANDOM_DIGITS = 12;

    @Autowired
    private DtsOrderMapper orderMapper;

    /**
     * 生成唯一订单号
     */
    public String generateOrderNo() {
        String orderNo;
        int maxAttempts = 5; // 最大尝试次数
        int attempts = 0;

        do {
            // 1. 生成候选订单号
            orderNo = generateCandidateOrderNo();

            // 2. 检查是否已存在
            boolean exists = orderMapper.existsByOrderNo(orderNo);

            if (!exists) {
                return orderNo;
            }

            attempts++;

        } while (attempts < maxAttempts);

        throw new RuntimeException("生成唯一订单号失败，请重试");
    }

    /**
     * 生成候选订单号
     */
    private String generateCandidateOrderNo() {
        // 日期部分
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);

        // 随机数部分
        String randomPart = generateRandomNumber(RANDOM_DIGITS);

        return ORDER_PREFIX + datePart + randomPart;
    }

    /**
     * 生成指定位数的随机数
     */
    private String generateRandomNumber(int digits) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(digits);

        for (int i = 0; i < digits; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
