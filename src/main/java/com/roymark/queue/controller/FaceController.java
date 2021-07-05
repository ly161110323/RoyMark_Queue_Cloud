package com.roymark.queue.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.roymark.queue.dao.AnomalyUserMapper;
import com.roymark.queue.entity.*;
import com.roymark.queue.service.AnomalyUserService;
import com.roymark.queue.service.FaceFeatureService;
import com.roymark.queue.service.FaceVectorService;
import com.roymark.queue.service.WindowService;
import com.roymark.queue.util.AnomalyMsgUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

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

    @Autowired
    private AnomalyUserService anomalyUserService;

    @RequestMapping(value = "/upload", produces = "application/json;charset=utf-8")
    public Object insert(@RequestBody List<ReceivedFace> receivedFaces) {
        JSONObject jsonObject = new JSONObject();
        Date date = new Date();
        try {
            // 接收消息时清空无效信息
            AnomalyMsgUtil anomalyMsgUtil = new AnomalyMsgUtil();
            anomalyMsgUtil.deleteInvalidMsg();
            // System.out.println("faceId:"+faceId);
            // System.out.println("windowHiddenId:" + windowHiddenId);
            // System.out.println("reId:"+ reId);
            // faceId不为空，则利用faceId在faceVector中获取uId，且保存faceId和reId的对应关系
            if (receivedFaces == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "发送为空");
                return jsonObject;
            }
            StringBuilder msg = new StringBuilder();
            for (ReceivedFace receivedFace: receivedFaces) {
                System.out.println("faceBoxId:"+receivedFace.getBoxId());
                String faceId = receivedFace.getFaceId();
                Long windowHiddenId = receivedFace.getWindowHiddenId();
                String reId = receivedFace.getReId();
                double faceConf = receivedFace.getFaceConf();
                if (!faceId.equals("")) {
                    FaceFeature faceFeature = new FaceFeature((long)0, faceId, reId);
                    faceFeatureService.save(faceFeature);
                }
                // faceId为空，在faceFeature中获取对应的faceId，然后在faceVector中获取uId
                else {
                    FaceFeature faceFeature = faceFeatureService.getOne(Wrappers.<FaceFeature>lambdaQuery().eq(FaceFeature::getReId, reId));
                    if (faceFeature != null)
                        faceId = faceFeature.getFaceId();
                    else {
                        msg.append("reId:").append(reId).append("没有对应的faceId\n");
                        continue;
                    }
                }
                FaceVector faceVector = faceVectorService.getOne(Wrappers.<FaceVector>lambdaQuery().eq(FaceVector::getFaceId, faceId));

                Window window = windowService.getById(windowHiddenId);
                if (window == null) {
                    msg.append("windowHiddenId:").append(windowHiddenId).append("不存在\n");
                }
                else if (faceVector == null) {
                    msg.append("faceId:").append(faceId).append("找不到用户\n");
                }
                else {
                    Long anomalyHiddenId = anomalyMsgUtil.addMap(receivedFace.getBoxId(), faceId, faceConf);
                    Long userHiddenId = faceVector.getUserHiddenId();
                    // 如果获取到了，加入到表中。checkInsert其中任一为空，则不会加入
                    anomalyUserService.checkInsert(new AnomalyUser((long)0, anomalyHiddenId, userHiddenId, faceConf));
                    window.setUserHiddenId(userHiddenId);
                    window.setUserUpdateTime(date);
                    window.setUserFaceConfidence(faceConf);
                    windowService.update(window, Wrappers.<Window>lambdaUpdate().eq(Window::getWindowHiddenId, windowHiddenId));
                    msg.append("faceId:").append(faceId).append("上传成功\n");
                }
            }
            jsonObject.put("result", "ok");
            jsonObject.put("msg", msg);
            return jsonObject;
        } catch (Exception e) {
            logger.error("/face/upload 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }

    }
}
