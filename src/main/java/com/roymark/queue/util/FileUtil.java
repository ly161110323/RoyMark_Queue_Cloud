/**
 * Copyright (C) 2009 长沙信凯软件有限公司.
 *
 * 本系统是商用软件,未经授权擅自复制或传播本程序的部分或全部将是非法的.
 *
 */
package com.roymark.queue.util;

import java.io.*;


/**
 * 文件操作类<br>
 * 
 * @author yy
 * @version 1.0
 */
public class FileUtil {
	
	public static String basePath =null;
	
	static String path = getSysPath()+"systemPath.xml";
	static ReadConfigXml r = new ReadConfigXml(path);
	/**
	 * 取得系统路径.
	 * 
	 * @return 系统路径
	 */
	public static String getSysPath() {
		String url;
		url = FileUtil.class.getResource("").toString();
		url = url.replaceAll("file:/", "");
		for (int i = 0; i < 5; i++) {
			url = url.substring(0, url.lastIndexOf("/", url.length() - 3) + 1);
		}
		return url.replaceAll("%20", " ");

	}

	/**
	 * 文件保存路径
	 * @return
	 */
	public static String getXMLPathValue() {
		return basePath;
	}
	
	/**
	 * 日志输出路径
	 * @return
	 */
	public static String getSysLogOutPath(){
		return r.getSysLogOutPath();
	}
	public static String getSysServerDBUrl(){
		return r.getSysServerDBUrl();
	}
	
	public static String getUsername(){
		return r.getUsername();
	}
	
	public static String getPassword(){
		return r.getPassword();
	}
	
	/**
	 * 判断终端 离线在线时间截
	 */
	public static Integer getDistanceSeconds(){
		return r.getDistanceSeconds();
	}
	
	/**
	 * 根据文件名在文件夹中查找,若存在，返回文件名
	 * @param pathString
	 * @param fileName
	 * @return
	 */
	public static String findFile(String pathString, String fileName) {
        String resultString="";
		File file = new File(pathString);
		File[] tempFiles = file.listFiles();
		if(tempFiles!=null){
		for (int i = 0; i < tempFiles.length; i++) {
			if (tempFiles[i].isFile()) {
				//System.out.println("name================="+tempFiles[i].getName());
				if (tempFiles[i].getName().startsWith(fileName)) {
					resultString +=tempFiles[i].getName()+",";
				} 
			}
		}	
		}
		//System.out.println(resultString);
		return resultString;
	}
	
	/**
	 * 读取文件
	 * @param file
	 * @param charset
	 * @return
	 */
	public static String readFile(File file, String charset){
        //设置默认编码
        if(charset == null){
            charset = "UTF-8";
        }
        if(file.isFile() && file.exists()){
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                 
                StringBuffer sb = new StringBuffer();
                String text = null;
                while((text = bufferedReader.readLine()) != null){
                    sb.append(text);
                }
                return sb.toString();
            } catch (Exception e) {
            }
        }
        return null;
    }
	
	/**
	 * 写入文件
	 * @param file
	 * @param content
	 */
	public static void writeToFile(File file,String content){
	     FileOutputStream fileOutputStream = null;
	   try {
	       if(!file.exists()){
	           file.createNewFile();
	       }
	       fileOutputStream = new FileOutputStream(file);
	       fileOutputStream.write(content.getBytes());
	       fileOutputStream.flush();
	       fileOutputStream.close();
	   } catch (Exception e) {
	       e.printStackTrace();
	   }
	   //System.out.println("ssss");
	}
	
	
	/**
	 * 删除文件夹
	 * 前提文件夹为空以及InputStream和OutputStream等一些数据文件流关掉【close()】，否则文件无法删除
	 * @param folderPath
	 */
	public static void delFolder(String folderPath) {
	     try {
	        delAllFile(folderPath); //删除完里面所有内容
	        String filePath = folderPath;
	        filePath = filePath.toString();
	        File myFilePath = new File(filePath);
	        myFilePath.delete(); //删除空文件夹
	     } catch (Exception e) {
	       e.printStackTrace(); 
	     }
	}
	
	/**
	 * 删除指定文件夹下的所有文件
	 * @param path
	 * @return
	 */
	public static boolean delAllFile(String path) {
       boolean flag = false;
       File file = new File(path);
       if (!file.exists()) {
         return flag;
       }
       if (!file.isDirectory()) {
         return flag;
       }
       String[] tempList = file.list();
       File temp = null;
       for (int i = 0; i < tempList.length; i++) {
          if (path.endsWith(File.separator)) {
             temp = new File(path + tempList[i]);
          } else {
              temp = new File(path + File.separator + tempList[i]);
          }
          if (temp.isFile()) {
             temp.delete();
          }
          if (temp.isDirectory()) {
             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
             delFolder(path + "/" + tempList[i]);//再删除空文件夹
             flag = true;
          }
       }
       return flag;
     }
	
	public static void main(String[] args) {
		FileUtil f = new FileUtil();
		String content = "测试使用字符串";
		File file = new File("D:\\aa.txt");
		f.writeToFile(file,content);
		String ss = f.readFile(file, "utf-8");
		System.out.println(ss);
	}
}
