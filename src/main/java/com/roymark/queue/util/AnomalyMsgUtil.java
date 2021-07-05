package com.roymark.queue.util;

import com.roymark.queue.entity.AnomalyMessage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class AnomalyMsgUtil {

    // 以String boxId为索引快速查找
    private static Map<String, AnomalyMessage> anomalyMessageMap;

    // 初始化
    public void init() {
        anomalyMessageMap = new HashMap<>();
    }

    // 获取Map
    public Map<String, AnomalyMessage> getMap() {
        return anomalyMessageMap;
    }

    // 行为端添加
    public Map<String, Double> addMap(List<String> boxIdList, Long anomalyHiddenId) {
        if (anomalyMessageMap == null) {
            init();
        }
        Date updateDate = new Date();
        deleteInvalidMsg();
        Map<String, Double> returnMap = new HashMap<>();
        for (String boxId: boxIdList) {
            if (anomalyMessageMap.containsKey(boxId)) {     // 已存在（表明人脸端已添加过了，获取对应结果并删除
                AnomalyMessage anomalyMessage = anomalyMessageMap.get(boxId);
                returnMap.put(anomalyMessage.getFaceId(), anomalyMessage.getFaceConf());
                anomalyMessageMap.remove(boxId);
            }
            else {
                AnomalyMessage curMsg = new AnomalyMessage();
                curMsg.setUpdateTime(updateDate);
                curMsg.setAnomalyHiddenId(anomalyHiddenId);
                anomalyMessageMap.put(boxId, curMsg);
            }
        }
        return returnMap;
    }

    // 人脸端添加
    public Long addMap(String boxId, String faceId, double faceConf) {
        if (anomalyMessageMap == null) {
            init();
        }
        if (anomalyMessageMap.containsKey(boxId)) {     // 已存在（表明行为端已添加过了，获取对应结果并删除
            Long anomalyHiddenId = anomalyMessageMap.get(boxId).getAnomalyHiddenId();
            anomalyMessageMap.remove(boxId);
            return anomalyHiddenId;
        }
        else {
            AnomalyMessage curMsg = new AnomalyMessage();
            curMsg.setUpdateTime(new Date());
            curMsg.setFaceId(faceId);
            curMsg.setFaceConf(faceConf);
            anomalyMessageMap.put(boxId, curMsg);
            return null;
        }
    }

    // 删除Map中无效的信息，指AnomalyMsg为空或其中时间为空或其中时间已过去一分钟
    public void deleteInvalidMsg() {
        Date curDate = new Date();
        if (anomalyMessageMap != null) {
            anomalyMessageMap.entrySet().removeIf(entry -> entry.getValue() == null
                    || entry.getValue().getUpdateTime() == null
                    || curDate.getTime()-entry.getValue().getUpdateTime().getTime() > 1000*60);
        }
    }
}
