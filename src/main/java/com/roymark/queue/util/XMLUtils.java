package com.roymark.queue.util;

import com.roymark.queue.entity.XmlBasicCase;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class XMLUtils<T> {
	
	/**
	 * 写入XML 此方法测试有效
	 * @param list 对象集合
	 * @param elementClass 类名
	 * @param XMLPathAndName 文件路径和名称
	 * @param Encode 字符集编码
	 */
    public static void writeXml(List list,Class elementClass,String XMLPathAndName,String Encode) {
    	
    	XMLWriter xmlWriter = null;// 声明写XML的对象    
        OutputFormat format = OutputFormat.createPrettyPrint();   
        format.setEncoding(Encode);// 设置XML文件的编码格式   
        
    	String filePath = XMLPathAndName;//获得文件地址   
        File file = new File(filePath);//获得文件     
        if (file.exists()) {   
            file.delete();   
        }  
        
        // 生成一个Dom树
        Document document = DocumentHelper.createDocument();
        // 增加根节点
        Element root = document.addElement(elementClass.getSimpleName()+"s");
        // 循环添加Student节点
        for (Object obj : list) {
            Element element = root.addElement(elementClass.getSimpleName());
            try {
                //获取父类对象的所有属性
                Class per = elementClass.getSuperclass();
                Field fieldsPerson[] = per.getDeclaredFields();
                for (Field field : fieldsPerson) {
                    field.setAccessible(true);
                    //System.out.println("属性名："+field.getName()+"，值："+field.get(obj));
                    String text = field.get(obj)==null?"":field.get(obj).toString();
                    // 给Student节点中添加Student的子节点
                    element.addElement(field.getName()).setText(text);
                }
                //获取子对象的所有属性
                Field fields[]=elementClass.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    //System.out.println("属性名："+field.getName()+"，值："+field.get(obj));
                    String text = field.get(obj)==null?"":field.get(obj).toString();
                    // 给Student节点中添加Student的子节点
                    element.addElement(field.getName()).setText(text);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 将xml写入文件（输出流）
        try {
            //xmlWriter = new XMLWriter(new FileOutputStream(elementClass.getSimpleName()+"s.xml"));
        	xmlWriter = new XMLWriter(new FileWriter(file), format); 
        	xmlWriter.write(document);
            xmlWriter.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
	/**  
     * DMO4J写入XML    
     * @param obj        泛型对象  
     * @param entityPropertys 泛型对象的List集合  
     * @param Encode     XML自定义编码类型(推荐使用GBK)  
     * @param XMLPathAndName    XML文件的路径及文件名  
     */  
    public void writeXmlDocument(T obj, List<T> entityPropertys, String Encode,   
            String XMLPathAndName) {   
        long lasting = System.currentTimeMillis();//效率检测   
        try {   
            XMLWriter writer = null;// 声明写XML的对象    
            OutputFormat format = OutputFormat.createPrettyPrint();   
            format.setEncoding(Encode);// 设置XML文件的编码格式   
            String filePath = XMLPathAndName;//获得文件地址   
            File file = new File(filePath);//获得文件     
            if (file.exists()) {   
                file.delete();   
            }   
            // 新建student.xml文件并新增内容   
            Document document = DocumentHelper.createDocument();   
            String rootname = obj.getClass().getSimpleName();//获得类名   
            Element root = document.addElement(rootname + "s");//添加根节点   
            Field[] properties = obj.getClass().getDeclaredFields();//获得实体类的所有属性   
            for (T t : entityPropertys) {                                //递归实体   
                Element secondRoot = root.addElement(rootname);            //二级节点   
                for (int i = 0; i < properties.length; i++) {                      
                    //反射get方法       
                    Method meth = t.getClass().getMethod(                      
                            "get"+ properties[i].getName().substring(0, 1).toUpperCase()   
                            + properties[i].getName().substring(1));   
                    //为二级节点添加属性，属性值为对应属性的值   
                    secondRoot.addElement(properties[i].getName()).setText(   
                            meth.invoke(t).toString());   
                }   
            }   
            //生成XML文件   
            writer = new XMLWriter(new FileWriter(file), format);   
            writer.write(document);   
            writer.close();   
            long lasting2 = System.currentTimeMillis();   
            System.out.println("写入XML文件结束,用时"+(lasting2 - lasting)+"ms");   
        } catch (Exception e) {   
            System.out.println("XML文件写入失败");   
        }   
  
    }
    
    /**  
     *   
     * @param XMLPathAndName XML文件的路径和地址  
     * @param t     泛型对象  
     * @return  
     */  
    public List<T> readXML(String XMLPathAndName, T t) {   
        long lasting = System.currentTimeMillis();//效率检测   
        List<T> list = new ArrayList<T>();//创建list集合   
        try {   
            File f = new File(XMLPathAndName);//读取文件   
            SAXReader reader = new SAXReader();   
            Document doc = reader.read(f);//dom4j读取   
            Element root = doc.getRootElement();//获得根节点   
            Element foo;//二级节点   
            Field[] properties = t.getClass().getDeclaredFields();//获得实例的属性   
            //实例的get方法   
            Method getmeth;   
            //实例的set方法   
            Method setmeth;   
            for (Iterator i = root.elementIterator(t.getClass().getSimpleName()); i.hasNext();) {//遍历t.getClass().getSimpleName()节点   
                foo = (Element) i.next();//下一个二级节点   
               t=(T)t.getClass().newInstance();//获得对象的新的实例   
               for (int j = 0; j < properties.length; j++) {//遍历所有孙子节点   
                    //实例的set方法   
                      setmeth = t.getClass().getMethod(   
                            "set"+ properties[j].getName().substring(0, 1).toUpperCase()   
                            + properties[j].getName().substring(1),properties[j].getType());   
                  //properties[j].getType()为set方法入口参数的参数类型(Class类型)   
                    setmeth.invoke(t, foo.elementText(properties[j].getName()));//将对应节点的值存入   
                }   
                list.add(t);   
            }   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
        long lasting2 = System.currentTimeMillis();   
        System.out.println("读取XML文件结束,用时"+(lasting2 - lasting)+"ms");   
        return list;   
    }
    
    /**  
     *   
     * @param XMLPathAndName XML文件的路径和地址  

     */  
    public List readXML(String XMLPathAndName,Class cls) {   
        long lasting = System.currentTimeMillis();//效率检测   
        List list = new ArrayList();//创建list集合   
        try {   
            File f = new File(XMLPathAndName);//读取文件   
            SAXReader reader = new SAXReader();   
            Document doc = reader.read(f);//dom4j读取   
            Element root = doc.getRootElement();//获得根节点   
            Element foo;//二级节点   
            Field[] properties = cls.getDeclaredFields();//获得实例的属性   
            //实例的get方法   
            Method getmeth;   
            //实例的set方法   
            Method setmeth;   
            for (Iterator i = root.elementIterator(cls.getSimpleName()); i.hasNext();) {//遍历t.getClass().getSimpleName()节点   
                foo = (Element) i.next();//下一个二级节点   
               Object t=cls.newInstance();//获得对象的新的实例   
               for (int j = 0; j < properties.length; j++) {//遍历所有孙子节点   
                    //实例的set方法   
                      setmeth = cls.getMethod(   
                            "set"+ properties[j].getName().substring(0, 1).toUpperCase()   
                            + properties[j].getName().substring(1),properties[j].getType());   
                  //properties[j].getType()为set方法入口参数的参数类型(Class类型)  
                      System.out.println(foo.elementText(properties[j].getName()));
                    //setmeth.invoke(t, foo.elementText(properties[j].getName()));//将对应节点的值存入   
                }   
                list.add(t);   
            }   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
        long lasting2 = System.currentTimeMillis();   
        System.out.println("读取XML文件结束,用时"+(lasting2 - lasting)+"ms");   
        return list;   
    }
    
    /** 
     * 遍历每个节点和属性 
     * @param filename 
     * @returnPraseXMLTest5.java 
     */ 
	public  static  List<Map<String,String>> iterateWholeXML(String filename) { 
	  List <Map<String,String>>list = new ArrayList<Map<String,String>>(); 
	  SAXReader saxReader = new SAXReader(); 
	   try { 
	    Document document = saxReader.read(new File(filename)); 
	    Element root = document.getRootElement(); 
	    recursiveNode(root,list); 
	       return list; 
	   } catch (DocumentException e) { 
	    e.printStackTrace(); 
	   } 
	   return null; 
	
	} 
    
	/** 
	  * 递归遍历所有的节点获得对应的值 

	  */ 
	private static void recursiveNode(Element root,List <Map<String,String>>list ){ 
	  // 遍历根结点的所有孩子节点 
	  for (Iterator iter = root.elementIterator(); iter.hasNext();) { 
		  HashMap<String, String> map = new HashMap<String, String>(); 
	
		  Element element = (Element) iter.next(); 
		  if (element == null) {
              continue;
          }
		  // 获取属性和它的值 
		  for (Iterator attrs = element.attributeIterator(); attrs.hasNext();) { 
			  Attribute attr = (Attribute) attrs.next(); 
			  if (attr == null) {
                  continue;
              }
			  String attrName = attr.getName(); 
			  String attrValue = attr.getValue(); 
	
			  map.put(attrName, attrValue); 
		  } 
		  //如果有PCDATA，则直接提出 
		  if(element.isTextOnly()){ 
			  String innerName = element.getName(); 
			  String innerValue = element.getText(); 
			  map.put(innerName, innerValue); 
			  list.add(map); 
		  }else{ 
			  list.add(map); 
			  //递归调用 
	          recursiveNode(element ,list);    
		  } 
	  	} 
	}
    
    public static void main(String[] args) {   
    	XMLUtils d = new XMLUtils();
    	List<XmlBasicCase> list = new ArrayList<XmlBasicCase>();
    	XmlBasicCase obj  = new XmlBasicCase();
    	obj.setCom1_BaudRate("11111");
    	obj.setTerminal_Ls(111);
    	obj.setTerminalID("1111");
    	obj.setTerminalName("AAAAA");

    	XmlBasicCase obj2  = new XmlBasicCase();
    	obj2.setCom1_BaudRate("2222");
    	obj2.setTerminal_Ls(222);
    	obj2.setTerminalID("2222");
    	obj2.setTerminalName("AAAAA222");

    	XmlBasicCase obj3  = new XmlBasicCase();
    	obj3.setCom1_BaudRate("33333");
    	obj3.setTerminal_Ls(333);
    	obj3.setTerminalID("3333");
    	obj3.setTerminalName("AAAAA3333");
    	list.add(obj2);
    	list.add(obj);
    	list.add(obj3);
    	try {

            XMLUtils.writeXml(list, XmlBasicCase.class, "D:\\bb.xml", "GBK");
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    	
        /*List<QueueUserInfo> resultList=  d.readXML("D:\\aa.xml",QueueUserInfo.class);   
        System.out.println("XML文件读取结果");   
        for(int i =0;i<resultList.size();i++){   
        	QueueUserInfo usename=(QueueUserInfo)resultList.get(i);   
            System.out.println("name"+usename.getUserInfoName());   
            System.out.println("age"+usename.getUserInfoAge());   
            System.out.println("code"+usename.getUserInfoCode());   
               
        }*/
        
        /*String filename = "d:\\aa.xml"; 
        List<Map<String,String>> list2 = d.iterateWholeXML(filename); 
        for(Map <String,String>map:list2){ 
        for (String ss : map.keySet()) { 
        	System.out.println(ss + ":" + map.get(ss)); 
        	} 
        } */
      
    }
}
