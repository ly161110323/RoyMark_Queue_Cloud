package com.roymark.queue.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.roymark.queue.entity.Abnomaly;
import com.roymark.queue.service.AbnomalyService;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/abnomaly")
public class AbnomalyController {
    private static final Logger logger = LogManager.getLogger(AbnomalyController.class);

    @Autowired
    private AbnomalyService abnomalyService;

    @RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
    public Object getAllAbnomalies() {
        JSONObject jsonObject = new JSONObject();

        try {
            List<Abnomaly> abnomalies = abnomalyService.getAllAbnomalies();

            jsonObject.put("abnomalies", abnomalies);
            jsonObject.put("result", "ok");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/abnomaly/getAllAbnomalies 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(String event, double confidence, String link, String userId, String windowId, String startDate, String endDate) {
        JSONObject jsonObject = new JSONObject();
        Abnomaly abnomaly = new Abnomaly();
        abnomaly.setEvent(event);
        abnomaly.setConfidence(confidence);
        abnomaly.setLink(link);
        abnomaly.setUserId(userId);
        abnomaly.setWindowId(windowId);

        Timestamp startTime = Timestamp.valueOf(startDate);
        System.out.println(startTime);
        Timestamp endTime = Timestamp.valueOf(endDate);

        abnomaly.setEndDate(endTime);
        abnomaly.setStartDate(startTime);
        try {
            boolean result = abnomalyService.save(abnomaly);
            if (result) {
                jsonObject.put("result", "ok");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/abnomaly/insert 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(Abnomaly abnomaly) {
        JSONObject jsonObject = new JSONObject();

        try {
            boolean result = abnomalyService.update(abnomaly, Wrappers.<Abnomaly>lambdaUpdate().eq(Abnomaly::getId, abnomaly.getId()));
            if (result) {
                jsonObject.put("result", "ok");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/abnomaly/update 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object delete(Long abnomalyId) {
        JSONObject jsonObject = new JSONObject();

        try {
            boolean result = abnomalyService.removeById(abnomalyId);
            if (result) {
                jsonObject.put("result", "ok");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/abnomaly/delete 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
    public Object getOne(Long abnomalyId) {
        JSONObject jsonObject = new JSONObject();

        try {
            Abnomaly abnomaly = abnomalyService.getById(abnomalyId);
            if (abnomaly != null) {
                jsonObject.put("result", "ok");
                jsonObject.put("abnomaly", abnomaly);
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/abnomaly/getOne 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/getByWindowId", produces = "application/json;charset=utf-8")
    public Object getByWindowId(String windowId) {
        JSONObject jsonObject = new JSONObject();

        try {
            List<Abnomaly> abnomalies = abnomalyService.getByWindowId(windowId);
            jsonObject.put("abnomalies", abnomalies);
            jsonObject.put("result", "ok");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/abnomaly/getOne 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }


}
