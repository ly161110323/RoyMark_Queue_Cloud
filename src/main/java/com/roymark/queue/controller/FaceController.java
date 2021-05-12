package com.roymark.queue.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.roymark.queue.entity.FaceFeature;
import com.roymark.queue.entity.FaceVector;
import com.roymark.queue.entity.Window;
import com.roymark.queue.service.FaceFeatureService;
import com.roymark.queue.service.FaceVectorService;
import com.roymark.queue.service.WindowService;
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
    private FaceVectorService faceVectorService;

    @Autowired
    private FaceFeatureService faceFeatureService;

    @Autowired
    private WindowService windowService;

    @RequestMapping(value = "/upload", produces = "application/json;charset=utf-8")
    public Object insert(String faceId, Long windowHiddenId, String reId) {
        JSONObject jsonObject = new JSONObject();
        try {
            // faceId不为空，则利用faceId在faceVector中获取uId，且保存faceId和reId的对应关系
            if (!faceId.equals("")) {
                FaceFeature faceFeature = new FaceFeature(Long.valueOf(0), faceId, reId);
                faceFeatureService.save(faceFeature);
            }
            // faceId为空，在faceFeature中获取对应的faceId，然后在faceVector中获取uId
            else {
                FaceFeature faceFeature = faceFeatureService.getOne(Wrappers.<FaceFeature>lambdaQuery().eq(FaceFeature::getReId, reId));
                if (faceFeature != null)
                    faceId = faceFeature.getFaceId();
                else {
                    jsonObject.put("result", "no");
                    jsonObject.put("msg", "reId没有对应的face");
                    return jsonObject;
                }
            }
            FaceVector faceVector = faceVectorService.getOne(Wrappers.<FaceVector>lambdaQuery().eq(FaceVector::getFaceId, faceId));

            Window window = windowService.getById(windowHiddenId);
            if (window == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "窗口不存在");
            }
            else if (faceVector == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "未查询到用户");

            }
            else {
                Long userHiddenId = faceVector.getUserHiddenId();
                window.setUserHiddenId(userHiddenId);
                windowService.update(window, Wrappers.<Window>lambdaUpdate().eq(Window::getWindowHiddenId, windowHiddenId));
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "上传成功");
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
