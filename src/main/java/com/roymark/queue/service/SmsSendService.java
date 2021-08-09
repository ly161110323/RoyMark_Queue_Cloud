package com.roymark.queue.service;

import cn.hutool.core.io.resource.ClassPathResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Slf4j
public class SmsSendService {


    /*
    封装发送短信方法
    mobiles为手机号码，多个手机号码用逗号隔开,
    content为要发送的短信内容，如content中存在双引号，
     */
    public boolean SendMsg(String _mobiles,String _content) {
        Properties properties = new Properties();
        boolean blResult = true;
        try {
            ClassPathResource classPathResource = new ClassPathResource("classpath:sms_config.properties");
            InputStream inputStream = classPathResource.getStream();
            // 参数读取
            properties.load(inputStream);
            String SpCode = properties.getProperty("SpCode");
            String LoginName = properties.getProperty("LoginName");
            String url = properties.getProperty("url");
            String Password = properties.getProperty("Password");
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String SerialNumber = "000" + format.format(new Date());

            try {
                HttpClient httpclient = new HttpClient();
                PostMethod post = new PostMethod(url);//
                post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "gbk");
                post.addParameter("SpCode", SpCode);
                post.addParameter("LoginName", LoginName);
                post.addParameter("Password", Password);
                post.addParameter("MessageContent", _content);
                post.addParameter("UserNumber", _mobiles);
                post.addParameter("SerialNumber", SerialNumber);
                post.addParameter("ScheduleTime", "");
                post.addParameter("ExtendAccessNum", "");
                post.addParameter("f", "1");
                httpclient.executeMethod(post);
                String info = new String(post.getResponseBody(), "gbk");
                log.info("sms send info:" + info);
                if (!info.contains("result=0") && !info.contains("result=27")) {
                    blResult = false;
                }
            } catch (Exception ex) {
                log.error("SmsSendService->SendMsg error:" + ex.getMessage(), ex);
                return false;
            }

        } catch (IOException e) {
            log.error("配置文件读取失败，请联系管理员检查");
            return false;
        }
        return blResult;
    }
}
