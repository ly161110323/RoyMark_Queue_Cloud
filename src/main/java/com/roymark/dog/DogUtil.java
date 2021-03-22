package com.roymark.dog;

import org.dom4j.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class DogUtil {

    public   String readStringXml2(String sessionXml){
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(sessionXml);
            Element rootElt = doc.getRootElement(); // 获取根节点
            Iterator iter = rootElt.elementIterator("dog"); // 获取根节点下的子节点head
            int index=0;
            if (iter.hasNext()) {
                Element recordEle = (Element) iter.next();
                Iterator iter2 =recordEle.elementIterator("feature");
                while (iter2.hasNext()) {
                    ++index;
                    Element feature = (Element) iter2.next();
                    Attribute attribute= feature.attribute("id"); // 获取子节点head下的子节点script
                    if(index==2){
                        return attribute.getText();
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } // 将字符串转为XML
        return "";
    }

    public  String getDogTime(){
        DogBean dog=new DogBean();
        //int status=dog.dog_login();
        String sessionXml="";
        String exp_date="";
        String end_day="";
        sessionXml=dog.dog_feature_id();
        exp_date=readStringXml(sessionXml);


        System.out.println("解析后日期:"+exp_date);
        if(exp_date!=null&&!"".equals(exp_date)&&!"-1".equals(exp_date)){
            end_day=stampToDate(exp_date,"yyyy-MM-dd HH:mm:ss");
            /*
             * 将时间戳转换为时间
             */

        }else if("-1".equals(exp_date)){
            end_day=plusDay(300);
        }
        System.out.println("end_day="+end_day);
        return end_day;
    }

    public  String plusDay(int num){
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currdate = format.format(d);
        System.out.println("现在的日期是：" + currdate);
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        d = ca.getTime();
        String enddate = format.format(d);
        System.out.println("增加天数以后的日期：" + enddate);
        return enddate;
    }

    /**
     *字符串的日期格式的计算
     */
    public  int daysBetween(String smdate,String bdate) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        long between_days=0L;
        try {
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            between_days=(time2-time1)/(1000*3600*24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(String.valueOf(between_days));
    }

    public String readStringXml(String sessionXml){
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(sessionXml);
            Element rootElt = doc.getRootElement(); // 获取根节点
            Iterator iterDog = rootElt.elementIterator("dog"); // 获取根节点下的子节点head
            while (iterDog.hasNext()) {
                Element dogEle = (Element) iterDog.next();

                Iterator iter = dogEle.elementIterator("feature"); // 获取根节点下的子节点head
                while (iter.hasNext()) {
                    Element recordEle = (Element) iter.next();

                    //System.out.println("recordEle.attribute('id')="+recordEle.attribute("id").getText());
                    if ("0".equals(recordEle.attribute("id").getText())) {
                        continue;
                    }

                    Iterator iters = recordEle.elementIterator("license"); // 获取子节点head下的子节点script

                    // 遍历Header节点下的Response节点
                    while (iters.hasNext()) {
                        Element itemEle = (Element) iters.next();
                        String exp_date = itemEle.elementTextTrim("exp_date");
//			                   System.out.println(exp_date+"=====exp_date======2");
                        if("".equals(exp_date)||exp_date==null){
                            return "-1";
                        }
                        return exp_date;
                    }

                }
            }
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // 将字符串转为XML
        return "";
    }

    public static String stampToDate(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }

}
