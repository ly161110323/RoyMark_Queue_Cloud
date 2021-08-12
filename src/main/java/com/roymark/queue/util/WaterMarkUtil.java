package com.roymark.queue.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;

/**

 * 給图片添加文字水印

 *

 * @author liuchengl

 *

 */

public class WaterMarkUtil {
    private static final Logger logger = LogManager.getLogger(WaterMarkUtil.class);

    /**

     * 图片添加水印

     *

     * @param bufferedImage

     *            需要添加水印的图片

     * @param markContentColor

     *            水印文字的颜色

     * @param waterMarkContent

     *            水印的文字

     */
    public static BufferedImage mark(BufferedImage bufferedImage, Color markContentColor, String waterMarkContent) {

        try {
            // 加水印
            Graphics2D g = bufferedImage.createGraphics();

            // 设置字体
            Font font = new Font("宋体", Font.BOLD, 30);
            g.setFont(font);
            g.setColor(markContentColor); // 根据图片的背景设置水印颜色
            // 位置
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            int x = width - getWatermarkLength(waterMarkContent, g) - 10;
            x = Math.max(x, 0);

            g.drawString(waterMarkContent, x, 30);
            g.dispose();

            return bufferedImage;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return bufferedImage;
        }

    }
    /**
     * 获取水印文字总长度
     * @param text 水印的文字
     * @param g
     * @return 水印文字总长度
     */
    private static int getWatermarkLength(String text, Graphics g) {
        return g.getFontMetrics(g.getFont()).charsWidth(
                text.toCharArray(), 0, text.length());
    }
}