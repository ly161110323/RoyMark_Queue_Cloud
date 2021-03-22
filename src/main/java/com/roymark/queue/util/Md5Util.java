package com.roymark.queue.util;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 * @author Administrator
 *
 */
public class Md5Util {
	
	  /**
		*利用MD5进行加密
	 　　* @param str  待加密的字符串
	　　 * @return  加密后的字符串
	　　 * @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
	　　 * @throws UnsupportedEncodingException  
	　　 */
	public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		  //确定计算方法
		  MessageDigest md5=MessageDigest.getInstance("MD5");
		  BASE64Encoder base64en = new BASE64Encoder();
		  //加密后的字符串
		  String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
		  return newstr;
	  }
	   
	  /**判断用户密码是否正确
　　　　* @param newpasswd  用户输入的密码
　　　　 * @param oldpasswd  数据库中存储的密码－－用户密码的摘要
　　　　* @return
　　　　* @throws NoSuchAlgorithmException
　　　　* @throws UnsupportedEncodingException
　　　　*/
	  public static boolean checkpassword(String newpasswd,String oldpasswd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		  if(EncoderByMd5(newpasswd).equals(oldpasswd)){
			  return true;
		  }else{
			  return false;
		  }
	  }
	  
	   // MD5加码。32位   
     public static String MD5(String inStr) {   
      MessageDigest md5 = null;   
      try {   
       md5 = MessageDigest.getInstance("MD5");   
      } catch (Exception e) {   
       System.out.println(e.toString());   
       e.printStackTrace();   
       return "";   
      }   
      char[] charArray = inStr.toCharArray();   
      byte[] byteArray = new byte[charArray.length];   
      
      for (int i = 0; i < charArray.length; i++)   
       byteArray[i] = (byte) charArray[i];   
      
      byte[] md5Bytes = md5.digest(byteArray);   
      
      StringBuffer hexValue = new StringBuffer();   
      
      for (int i = 0; i < md5Bytes.length; i++) {   
       int val = ((int) md5Bytes[i]) & 0xff;   
       if (val < 16)   
        hexValue.append("0");   
       hexValue.append(Integer.toHexString(val));   
      }   
      
      return hexValue.toString();   
     }   
      
     // 可逆的加密算法   
     public static String KL(String inStr) {   
      // String s = new String(inStr);   
      char[] a = inStr.toCharArray();   
      for (int i = 0; i < a.length; i++) {   
       a[i] = (char) (a[i] ^ 't');   
      }   
      String s = new String(a);   
      return s;   
     }   
      
     // 加密后解密   
     public static String JM(String inStr) {   
      char[] a = inStr.toCharArray();   
      for (int i = 0; i < a.length; i++) {   
       a[i] = (char) (a[i] ^ 't');   
      }   
      String k = new String(a);   
      return k;   
     } 

	public static void main(String[] args) {
		 Md5Util md5 = new Md5Util();
		 String str = "123456";
		 try {
	      String newString = md5.EncoderByMd5(str);
	      System.out.println(newString);
	      System.out.println(md5.checkpassword("apple1", "HzhwvidPbEmz4xoMZyiVfw=="));
	    } catch (NoSuchAlgorithmException e) {
	      e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	      e.printStackTrace();
	    }
		 
		 String s = new String("123456");   
		 System.out.println("原始：" + s);   
		 System.out.println("MD5后：" + MD5(s));   
		 System.out.println("MD5后再加密：" + KL(MD5(s)));   
		 System.out.println("解密为MD5后的：" + JM(KL(MD5(s))));  
		 
	}

}
