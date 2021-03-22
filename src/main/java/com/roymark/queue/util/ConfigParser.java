package com.roymark.queue.util;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Properties;

/**
 * XML配置文件解析器,主要目的，是为做前期工作
 */
public class ConfigParser extends DefaultHandler {
	// 定义一个properties用来存放属性
	private Properties props;
	private String currentName;
	private StringBuffer currentValue = new StringBuffer();

	public ConfigParser() {
		this.props = new Properties();
	}

	public Properties getProps() {
		return this.props;
	}

	// 这里是将xml中元素值加入currentValue
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		currentValue.append(ch, start, length);

	}

	// 在遇到</xx>时，将之间的字符存放在props中间
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		props.put(currentName.toLowerCase(), currentValue.toString().trim());
	}

	// 定义开始解析元素的方法，这里将<xx>中的名称xx提出来，
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentValue.delete(0, currentValue.length());
		currentName = qName;
	}

	//
}