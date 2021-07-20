package com.roymark.queue.util;

import com.roymark.queue.entity.AnomalyMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class AnomalyMsgUtil {
    private static final Logger logger = LogManager.getLogger(AnomalyMsgUtil.class);

    // 以String boxId为索引快速查找
    private final static Map<String, AnomalyMessage> anomalyMessageMap = new HashMap<>();

    // 行为端添加
    public Map<String, Double> addMap(List<String> boxIdList, Long anomalyHiddenId) {
        try {
            Date updateDate = new Date();
            deleteInvalidMsg();
            Map<String, Double> returnMap = new HashMap<>();
            for (String boxId: boxIdList) {
                if (anomalyMessageMap.containsKey(boxId)) {     // 已存在（表明人脸端已添加过了，获取对应结果并删除
                    AnomalyMessage anomalyMessage = anomalyMessageMap.get(boxId);
                    if (anomalyMessage.getFaceId()==null || anomalyMessage.getFaceId().equals("")) {        // 重复但人脸端结果未到
                        continue;
                    }
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
        } catch (Exception e) {
            logger.error("AnomalyMsgUtil addMap for action method:exception");
            logger.error(e.getMessage());
            return null;
        }

    }

    // 人脸端添加
    public Long addMap(String boxId, String faceId, double faceConf) {
        try {
            deleteInvalidMsg();
            if (anomalyMessageMap.containsKey(boxId)) {     // 已存在（表明行为端已添加过了，获取对应结果并删除
                Long anomalyHiddenId = anomalyMessageMap.get(boxId).getAnomalyHiddenId();
                if (anomalyHiddenId == null || anomalyHiddenId == 0) {          // 重复但行为端结果未到
                    return null;
                }
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
        } catch (Exception e) {
            logger.error("AnomalyMsgUtil addMap for face method:exception");
            logger.error(e.getMessage());
            return null;
        }

    }

    // 删除Map中无效的信息，指AnomalyMsg为空或其中时间为空或其中时间已过去一分钟
    public void deleteInvalidMsg() {
        try {
            Date curDate = new Date();
            anomalyMessageMap.entrySet().removeIf(entry -> entry.getValue() == null
                    || entry.getValue().getUpdateTime() == null
                    || curDate.getTime()-entry.getValue().getUpdateTime().getTime() > 1000*60);
        } catch (Exception e) {
            logger.error("AnomalyMsgUtil deleteInvalidMsg method:exception");
            logger.error(e.getMessage());
        }

    }

}
