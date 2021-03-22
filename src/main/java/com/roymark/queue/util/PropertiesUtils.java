package com.roymark.queue.util;

import java.io.*;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * 配置文件工具类
 * @author benjamin
 *
 */
public class PropertiesUtils {
    /** 
     * 根据KEY，读取文件对应的值 
     * @param filePath 文件路径，即文件所在包的路径，例如：java/util/config.properties 
     * @param key 键 
     * @return key对应的值 
     */  
    public static String readData(String filePath, String key) {  
        //System.out.println(PropertiesUtils.class.getResource("/"));
    	//获取绝对路径  
        filePath = PropertiesUtils.class.getResource("/" + filePath).toString();
        //截掉路径的”file:“前缀  
        filePath = filePath.substring(6);  
        Properties props = new Properties();  
        try {
            filePath= URLDecoder.decode(filePath,"UTF-8");
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));  
            props.load(in);  
            in.close();  
            String value = props.getProperty(key);  
            return value;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
    /** 
     * 修改或添加键值对 如果key存在，修改, 反之，添加。 
     * @param filePath 文件路径，即文件所在包的路径，例如：java/util/config.properties 
     * @param key 键 
     * @param value 键对应的值 
     */  
    public static void writeData(String filePath, String key, String value) {  
        //获取绝对路径  
        filePath = PropertiesUtils.class.getResource("/" + filePath).toString();

        //截掉路径的”file:/“前缀  
        filePath = filePath.substring(6);  
        Properties prop = new Properties();  
        try {
            filePath= URLDecoder.decode(filePath,"UTF-8");
            File file = new File(filePath);  
            if (!file.exists())
            {
                file.createNewFile();
            }
            InputStream fis = new FileInputStream(file);  
            prop.load(fis);  
            //一定要在修改值之前关闭fis  
            fis.close();  
            OutputStream fos = new FileOutputStream(filePath);  
            prop.setProperty(key, value);  
            //保存，并加入注释  
            prop.store(fos, "Update '" + key + "' value");  
            fos.close();  
        } catch (IOException e) {  
            System.err.println("Visit " + filePath + " for updating " + value + " value error");  
        }  
    }  
    
    /*public static Map getIBApiConfi(){
    	 String topAipUrl = PropertiesUtils.readData("conf/properties/IBApiConfig.properties", "topAipUrl");
         String topApiAppKey = PropertiesUtils.readData("conf/properties/IBApiConfig.properties", "topApiAppKey");
         String topApiAppSecret = PropertiesUtils.readData("conf/properties/IBApiConfig.properties", "topApiAppSecret");
         String topApiSessionKey = PropertiesUtils.readData("conf/properties/IBApiConfig.properties", "topApiSessionKey");
    	Map tempMap = new HashMap();
    	tempMap.put("topAipUrl", topAipUrl);
    	tempMap.put("topApiAppKey",topApiAppKey);
    	tempMap.put("topApiAppSecret", topApiAppSecret);
    	tempMap.put("topApiSessionKey", topApiSessionKey);
    	return tempMap;
    }*/
      
    public static void main(String[] args) {
       PropertiesUtils.writeData("ftpinfo.properties", "ftpport","wfz_test");
    }    
}
