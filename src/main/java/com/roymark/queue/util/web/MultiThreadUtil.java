package com.roymark.queue.util.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liucl
 * @create 2020-11-27 19:56
 */
public class MultiThreadUtil {
    private final static String URL_1="";
    private final static String URL_2="";
    private final static String URL_3="";
    private final static String URL_4="";
    private final static String URL_5="";

    // 单线程发送get请求
    private static String httpGetRequest(String urlStr){
        String result = "";
        BufferedReader br = null;

        try{
            // 将url字符串转化为URL对象
            URL url = new URL(urlStr);

            // 打开和URL之间的连接
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(3000);


            // 设置请求属性
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");

            // 建立实际的连接
            connection.connect();

            // 定义BufferedReader输入流来读取URL的响应
            br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));

            // 从流中读取数据
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }

        }catch (Exception e){
            System.out.println(urlStr+"对应的服务器未启动");
        }finally {
            try {
                if(br!=null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 多线程发送请求
    public static List<JSONObject> getThirdPartyData(List<String> urls) throws ExecutionException, InterruptedException {
        List<JSONObject> list = new ArrayList<>();
        //申明线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(urls.size());

        StringBuilder builder = new StringBuilder();
        for (int i=0; i<urls.size(); i++ ){
            int finalI = i;
            // 申请单个线程执行类
            Callable<String> call = new Callable<String>() {
                @Override
                public String call() {
                    return httpGetRequest(urls.get(finalI));
                }
            };
            // 提交单个线程获取字符串
            String s = threadPool.submit(call).get();
            if(!StringUtils.isEmpty(s)){
                // 返回正常的数据
                List<JSONObject> jsonObjects = JSONObject.parseArray(s, JSONObject.class);
                for(JSONObject item:jsonObjects){
                    item.put("ip",urls.get(i).split(":")[1].split("//")[1]);
                };
                list.addAll(jsonObjects);
                builder.append(urls.get(i)+"对应的服务器正常返回数据;");
            }else {
                // 将错误信息拼接
                builder.append(urls.get(i)+"对应的服务器访问失败;");
            }
        }
        // 将错误信息封装为一个JSONObject对象，放在list的首位
        JSONObject msg = new JSONObject();
        msg.put("msg", builder.toString());
        list.add(0, msg);
        return list;
    }

}
