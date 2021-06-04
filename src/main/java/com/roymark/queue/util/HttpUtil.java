package com.roymark.queue.util;

import cn.hutool.http.HttpResponse;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class HttpUtil {

    public static ResponseEntity<String> sendPost(String url, MultiValueMap<String,Object> requestParams, Map<String,String> headParams){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        for (Map.Entry<String,String> entry:headParams.entrySet()){
            headers.set(entry.getKey(),entry.getValue());
        }
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        //将参数的设置提取出去了
//            FileSystemResource resource = new FileSystemResource(new File(filePath));
//            MultiValueMap<String,Object> params = new LinkedMultiValueMap<>();
//            params.add("partfile",resource);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(requestParams, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return response;

    }
}