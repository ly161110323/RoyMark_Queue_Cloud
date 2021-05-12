package com.roymark.queue.controller;

import com.alibaba.fastjson.JSONObject;
import com.roymark.queue.entity.Face;
import com.roymark.queue.service.FaceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/face")
public class FaceController {

    private static final Logger logger = LogManager.getLogger(FaceController.class);

    @Autowired
    private FaceService faceService;

    @RequestMapping(value = "/upload", produces = "application/json;charset=utf-8")
    public Object insert(Long faceId, Long userId) {
        JSONObject jsonObject = new JSONObject();
        try {
            Face face = new Face();
            face.setFaceId(faceId);
            face.setUserId(userId);
            Boolean result = faceService.save(face);
            if (result) {
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "添加成功");
            }
            else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "添加失败");
            }
            return jsonObject;
        } catch (Exception e) {
            logger.error("/face/upload 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }

    }
}
