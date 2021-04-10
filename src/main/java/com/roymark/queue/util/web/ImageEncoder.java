package com.roymark.queue.util.web;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * @author liucl
 * @create 2021-01-19 18:22
 */
public class ImageEncoder implements Encoder.Text<Image> {
    @Override
    public String encode(Image image) throws EncodeException {
        if(image != null && !ArrayUtils.isEmpty(image.getImageByte())){
            String base64Image= Base64.encode(image.getImageByte());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 201);
            jsonObject.put("msg", "获取视频帧成功");
            jsonObject.put("data", base64Image);
            return JSON.toJSONString(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "获取视频帧失败");
        jsonObject.put("data", null);
        return JSON.toJSONString(jsonObject);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
