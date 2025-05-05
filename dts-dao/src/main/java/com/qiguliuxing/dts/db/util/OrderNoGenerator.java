package com.qiguliuxing.dts.db.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 订单号生成器
 */
public final class OrderNoGenerator {
    private static final AtomicInteger sequence = new AtomicInteger(0);
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String generate() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        int seq = sequence.incrementAndGet() % 10000;
        return "BOX" + timestamp + String.format("%04d", seq);
    }
}
