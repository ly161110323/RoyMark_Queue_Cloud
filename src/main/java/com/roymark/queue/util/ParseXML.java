package com.roymark.queue.util;

/*
 * XML配置文件计取处理
 */

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Properties;

public class ParseXML {
	// 定义一个Proerties用来存放属性值
	private Properties props;

	public Properties getProps() {
		return this.props;
	}

	public void parse(String filename) throws Exception {
		// 将我们的解析器对象化
		ConfigParser handler = new ConfigParser();
		// 获取SAX工厂对象
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);
		// 获取SAX解析
		SAXParser parser = factory.newSAXParser();

		try {

			// 将解析器和解析对象xml联系起来，开始解析
			parser.parse(filename, handler);
			// 获取解析成功后的属性
			props = handler.getProps();
		} finally {
			factory = null;
			parser = null;
			handler = null;
		}
	}

}
