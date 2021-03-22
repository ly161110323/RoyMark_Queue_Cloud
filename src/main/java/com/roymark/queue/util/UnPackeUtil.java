package com.roymark.queue.util;

import net.lingala.zip4j.core.ZipFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class UnPackeUtil {
    /**
     * zip文件解压
     *
     * @param destPath 解压文件路径
     * @param zipFile  压缩文件
     * @param password 解压密码(如果有)
     */
    private static final Logger logger = LogManager.getLogger(UnPackeUtil.class);
    public static void unPackZip(File zipFile, String password, String destPath) throws Exception{
        try {
            ZipFile zip = new ZipFile(zipFile);
            /*zip4j默认用GBK编码去解压,这里设置编码为GBK的*/
            zip.setFileNameCharset("UTF-8");

            zip.extractAll(destPath);
            // 如果解压需要密码
            if (zip.isEncrypted()) {
                zip.setPassword(password);
            }
        } catch (Exception e) {
            logger.error("unPack zip file to " + destPath + " fail ....", e.getMessage(), e);
            throw(e);
        }
    }



}
