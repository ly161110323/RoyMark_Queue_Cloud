package com.roymark.dog;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import SuperDog.Dog;
import SuperDog.DogTime;
import SuperDog.DogStatus;
import SuperDog.DogApiVersion;

public class DogBean {

    public static final int DEMO_MEMBUFFER_SIZE = 128;

    public static final String vendorCode = new String(
            "vu/JPwdJ04UIjJRqho7kPtYaLJOnD02ZltTfkPzUuUtjPr1BEbXdYqI2Mq2AyApUfxmNF" +
                    "zrUAjGlrUZ0VSct+ZgcdpDiuPXKoK7TlMqlpOqM+V477g0APimYoaQRm5Crc4mPBsKcQd/" +
                    "Ne1SqxgSSFyxSc6a9WSOLcSD7IV3d9dv2YJiZKhemt/l37KE/o3fFGGgvhujXdpIdVCDxSYB" +
                    "dpf/xCtLlJ3HCCksbexOx6NVqMJTkxumGnuuDuS0kr8jHGeU/Agt2MXowtTLCFPkHmbJ/B1we/" +
                    "Pb4tKBesObttLKJHzhWv8A4u3c8do5VJXNPhuqPtiP/zu8TsRDfTm80RKORcnjxH0ydB+R6Lu5qqh9P/" +
                    "r8+cSHaRfQaVyX9mIxuiSDPd5mXlnyrsuUdLJm5OO1DKSZ5ATyUHq2kSryU4C8iXIz14UYQzlpU0A7K/" +
                    "WXv8ZZCVIOiFlyKRfrw00dQfmvXOYPEUEj3bRBWrQnjrt+4Za/fhuL2HmBvRJ7ei10WyVQU0yRUYmbrJa" +
                    "AlXRRmyJZqzQoL44sbJcive+61AkOXU/voFAxAo2yKFYJAmP/UqTb5vzywOuIONLPuOyn8YexXT0MrqpW/" +
                    "8RQgxStGhWd8BBGva192atPNqPwpor9gwAxMhcqqUMnFLLXKVt5zSRDInBmk+b/77ZDJqVOyXu/nJXXpjkh7n6" +
                    "jIb0tj8gJBpGChSLkAqR9kyn09eJJttbjMLJDiC1+7sO5wmPPq5sX6OaL7VhBwsn96336MKVcXZnZ8IPncNpO" +
                    "gnOS3stRKPP4wpg9zpVgZSo1RLwbMyyT6R9CD1CXfHgsaUfZD666aNGbCR0cotJ7zmj4rvkjtMDMCMaChUHyA+wwt1qNqWenfW79" +
                    "HBgwd1XK6ljEq39pH7Ads4FiDhbzu8Kg3CgZawrTJdO5+2CKITsRpjsffFtdENB80KeRFdDc7yMqIpfChp6NYClMwOj6Cz9bf0z2OwQ==");

    public static final String productScope = new String(
            "<dogscope>\n" +
                    " <product id=\"12\"/>\n" +
                    "</dogscope>\n");

    public static final String scope = new String(
            "<dogscope />\n");

    public static final String view = new String(
            "<dogformat root=\"my_custom_scope\">\n" +
                    "  <dog>\n" +
                    "    <attribute name=\"id\" />\n" +
                    "    <attribute name=\"type\" />\n" +
                    "    <feature>\n" +
                    "      <attribute name=\"id\" />\n" +
                    "      <element name=\"license\" />\n" +
                    "      <session>\n" +
                    "        <element name=\"hostname\" />\n" +
                    "        <element name=\"apiversion\" />\n" +
                    "      </session>\n" +
                    "    </feature>\n" +
                    "  </dog>\n" +
                    "</dogformat>\n");

    public static final byte[] data = { 0x74, 0x65, 0x73, 0x74, 0x20, 0x73, 0x74, 0x72,
            0x69, 0x6e, 0x67, 0x20, 0x31, 0x32, 0x33, 0x34 };
    private static DogTime datetime;
    private static String sessionXml;
    private static Long dogtime;

    /************************************************************************
     *
     * helper function: dumps a given block of data, in hex and ascii
     */

    /*
     * Converts a byte to hex digit and writes to the supplied buffer
     */
    private static void byte2hex(byte b, StringBuffer buf)
    {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    /*
     * Converts a byte array to hex string
     */
    private static String toHexString(byte[] block)
    {
        StringBuffer buf = new StringBuffer();

        int len = block.length;

        for (int i = 0; i < len; i++)
        {
            byte2hex(block[i], buf);
            if (i < len - 1)
            {
                buf.append(":");
            }
        }
        return buf.toString();
    }

    public static void dump(byte[] data, String margin)
    {
        int i, j;
        byte b;
        byte[] s = new byte[16];
        byte hex[] = {0};
        String shex;
        String PrtString;

        if (data.length == 0)
        {
            return;
        }

        s[0] = 0;
        j = 0;
        for (i = 0; i < data.length; i++)
        {
            if (j == 0)
            {
                System.out.print(margin);
            }
            b = data[i];
            if ((b < 32) || (b > 127))
            {
                s[j] = '.';
            } else
            {
                s[j] = b;
            }
            if (j < 15)
            {
                s[j+1] = 0;
            }

            hex[0] = b; shex = toHexString(hex);
            System.out.print(shex + " ");
            j++;
            if (((j & 3) == 0) && (j < 15)) {
                System.out.print("| ");
            }
            PrtString = new String(s);
            if (j > 15)
            {
                System.out.println("[" + PrtString + "]");
                j = 0;
                s[0] = 0;
            }
        }
        if (j != 0)
        {
            while (j < 16)
            {
                System.out.print("   ");
                j++;
                if (((j & 3) == 0) && (j < 15)) {
                    System.out.print("| ");
                }
            }
            PrtString = new String(s);
            System.out.println(" [" + PrtString + "]");
        }
    }
//如果访问狗出错，返回错误提示信息
//如果能正常访问到狗，返回大写的OK字符串

    public int dog_login()
    {
        int status;
        //String strReturn="";
        Dog curDog = new Dog(0);

        DogApiVersion version = curDog.getVersion(vendorCode);
        status = version.getLastError();
           /*
           switch (status)
           {
               case DogStatus.DOG_STATUS_OK:
                   break;
               case DogStatus.DOG_NO_API_DYLIB:
            	   //strReturn="超级狗  API 动态链接库没有找到!";
                   //return strReturn;
            	   break;
               case DogStatus.DOG_INV_API_DYLIB:
            	   //strReturn="超级狗  API 动态链接库损坏!";
                   //return strReturn;
            	   break;
               default:
            	   //strReturn="未知异常,请联系开发人员!";
            	   //return strReturn;
            	   break;
           }
           */
        if (status != DogStatus.DOG_STATUS_OK){
            return status;
        }
        //System.out.println("API Version: " + version.majorVersion() + "."
        //        + version.minorVersion()
        //        + "." + version.buildNumber() + "\n");
        //登录
        curDog.login(vendorCode);
        status = curDog.getLastError();
           /*
           switch (status)
           {
               case DogStatus.DOG_STATUS_OK:
            	   strReturn="OK";
                   break;
               case DogStatus.DOG_FEATURE_NOT_FOUND:
            	   strReturn="特征不可用";
            	   return strReturn;
               case DogStatus.DOG_NOT_FOUND:
            	   strReturn="没有可用的狗";
            	   return strReturn;
               case DogStatus.DOG_INV_VCODE:
            	   strReturn="输入的开发商代码无效!";
            	   return strReturn;
               case DogStatus.DOG_LOCAL_COMM_ERR:
            	   strReturn="API和超级狗通讯环境错误!";
            	   return strReturn;
               default:
            	   strReturn="登录出现未知异常,请联系开发人员!";
            	   return strReturn;
           }

     		return strReturn;
           */

        if (status != DogStatus.DOG_STATUS_OK){
            return status;
        }
        sessionXml=curDog.getSessionInfo(Dog.DOG_SESSIONINFO);
        dogtime=curDog.getTime().getDogTime();
//           System.out.println(sessionXml);
//           String exp_date=readStringXml(sessionXml);
//           System.out.println(exp_date);
//           if(exp_date!=null&&!"".equals(exp_date)){
//        	   Date dat=new Date(curDog.getTime().getDogTime());
//        	   Long d=Long.parseLong(exp_date)/(24*60*60);
//
//        	 Date str  =getDateAfter(dat,Integer.parseInt(d+""));
//        	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        	 System.out.println( sf.format(str)+"=="+str.getMonth()+"="+str.getDay());
//				//Long.parseLong(exp_date)/(24*)
//
//			}
           /*
           datetime = curDog.getTime();
           status = curDog.getLastError();
           if (status != DogStatus.DOG_STATUS_OK){
        	   return status;
           }

           System.out.println("time: " + datetime.getHour() + ":"
                   + datetime.getMinute() + ":" + datetime.getSecond() +" H:M:S");
           System.out.println("                                   date: "
                   + datetime.getDay() + "/" + datetime.getMonth() + "/"
                   + datetime.getYear() + " D/M/Y");
           */

        return status;
    }
    public String dog_feature_id()
    {
        int status;
        //String strReturn="";
        Dog curDog = new Dog(Dog.DOG_DEFAULT_FID);
        return curDog.getInfo(scope, view, vendorCode);
    }
    public static String getSessionXml() {
        return sessionXml;
    }

    public static Long getDogtime() {
        return dogtime;
    }

}

