package com.roymark.queue.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
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
    public BufferedImage mark(BufferedImage bufferedImage, Color markContentColor, String waterMarkContent) {

        try {
            // 加水印
            Graphics2D g = bufferedImage.createGraphics();
            // Font font = new Font("Courier New", Font.PLAIN, 12);
            Font font = new Font("宋体", Font.PLAIN, 20);
            g.setColor(markContentColor); // 根据图片的背景设置水印颜色
            g.setFont(font);
            g.drawString(waterMarkContent, 10, 30);
            g.dispose();

            return bufferedImage;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return bufferedImage;
        }

    }

}