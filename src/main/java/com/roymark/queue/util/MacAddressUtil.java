package com.roymark.queue.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 获取Mac地址工具类
 * 
 * @author James
 *
 */
public class MacAddressUtil {
	 /**   
     * 通过IP地址获取MAC地址   
     * @param ip String,127.0.0.1格式   
     * @return mac String   
     * @throws Exception   
     */      
    public static String getMACAddress(String ip) throws Exception {      
        String line = "";      
        String macAddress = "";      
        final String MAC_ADDRESS_PREFIX = ip+" ";      
        final String LOOPBACK_ADDRESS = "127.0.0.1";      
        //如果为127.0.0.1,则获取本地MAC地址。      
        if (LOOPBACK_ADDRESS.equals(ip)) {      
            InetAddress inetAddress = InetAddress.getLocalHost();      
            //貌似此方法需要JDK1.6。      
            byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();      
            //下面代码是把mac地址拼装成String      
            StringBuilder sb = new StringBuilder();      
            for (int i = 0; i < mac.length; i++) {      
                if (i != 0) {      
                    sb.append("-");      
                }      
                //mac[i] & 0xFF 是为了把byte转化为正整数      
                String s = Integer.toHexString(mac[i] & 0xFF);      
                sb.append(s.length() == 1 ? 0 + s : s);      
            }      
            //把字符串所有小写字母改为大写成为正规的mac地址并返回      
            macAddress = sb.toString().trim().toUpperCase();      
            return macAddress;      
        }      
        //获取非本地IP的MAC地址      
        try {
        	String cmd = "ping  " + ip;
          
            String ret = callCmd(cmd);
            //判断IP地址是否存在
            if(ret!=null &&!"".equals(ret)){
            	cmd = "nbtstat -a " + ip;
            	macAddress = callCmdMAC(cmd);
            }
        } catch (IOException e) {     
        	e.printStackTrace();
          //  e.printStackTrace(System.out);      
        }      
        return macAddress;      
    } 
    /**
     * 执行cmd命令
     * @param cmd
     * @return
     */
    public static String callCmd(String cmd) throws IOException{
    	Process p = Runtime.getRuntime().exec(cmd);      
        InputStreamReader isr = new InputStreamReader(p.getInputStream());      
        BufferedReader br = new BufferedReader(isr);
        String line="";
        String ret="";
        while ((line = br.readLine()) != null) {      
            if (line != null) {      
                ret =line;    
            }      
        }      
        br.close();
        return ret;
    }
    
    public static String callCmdMAC(String cmd) throws IOException{
    	Process p = Runtime.getRuntime().exec(cmd); 
        InputStreamReader isr = new InputStreamReader(p.getInputStream());      
        BufferedReader br = new BufferedReader(isr);
        String line="";
        int startIndex = 0;
        while ((line = br.readLine()) != null) {
           startIndex = line.indexOf("MAC 地址");
           if(startIndex>0){
        	   line = line.substring(startIndex+8, line.length()).trim().toUpperCase();
        	   break;
           }
        }      
        br.close();
        return line;
    }

/*	public static void main(String[] args) {
		MacAddressUtil macAddressUtil = new MacAddressUtil();
		try {
			String string = macAddressUtil.getMACAddress("192.168.5.73");
			System.out.println(string);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
}
