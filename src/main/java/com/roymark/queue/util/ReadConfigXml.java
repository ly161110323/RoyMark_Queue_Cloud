package com.roymark.queue.util;

/*
 * 读取XML配置文件
 */

import java.util.Properties;

public class ReadConfigXml {
	private Properties props;

	public ReadConfigXml(String url) {
//System.out.println(url);
		ParseXML myRead = new ParseXML();
		try {
			url="file:///"+url;
			myRead.parse(url);
			props = new Properties();
			props = myRead.getProps();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public String getSystemPath() {
		return props.getProperty("pathvalue");
	}
	public String getSysLogOutPath() {
		return props.getProperty("syslogoutpath");
	}
	public String getSysServerDBUrl() {
		return props.getProperty("sysserverdburl");
	}
	
	public String getUsername(){
		return props.getProperty("username");
	}
	public String getPassword(){
		return props.getProperty("password");
	}
	

	public Integer getDistanceSeconds(){
		return Integer.parseInt(props.getProperty("distanceseconds"));
	}
}