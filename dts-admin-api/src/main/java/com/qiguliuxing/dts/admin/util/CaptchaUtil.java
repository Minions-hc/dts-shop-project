package com.qiguliuxing.dts.admin.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtil {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 40;
    private static final int FONT_SIZE = 30;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static Captcha generateCaptcha() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置背景颜色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 设置字体
        g.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));

        // 生成随机字符串
        Random random = new Random();
        StringBuilder captchaText = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            char c = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
            captchaText.append(c);
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawString(String.valueOf(c), 20 * i + 10, 30);
        }

        g.dispose();

        return new Captcha(captchaText.toString(), image);
    }

    public static class Captcha {
        private final String text;
        private final BufferedImage image;

        public Captcha(String text, BufferedImage image) {
            this.text = text;
            this.image = image;
        }

        public String getText() {
            return text;
        }

        public BufferedImage getImage() {
            return image;
        }
    }

}
