package com.roymark.queue.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.Anomaly;
import com.roymark.queue.service.AnomalyService;
import com.roymark.queue.service.UserService;
import com.roymark.queue.service.WindowService;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/anomaly")
public class AnomalyController {
    private static final Logger logger = LogManager.getLogger(AnomalyController.class);

    @Autowired
    private AnomalyService anomalyService;

    @Autowired
    private UserService userService;

    @Autowired
    private WindowService windowService;

    @RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
    public Object getAllAnomalies() {
        JSONObject jsonObject = new JSONObject();

        try {
            List<Anomaly> anomalies = anomalyService.getAllAnomalies();

            if (anomalies.size() <= 0) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "获取结果为空");
                jsonObject.put("anomalies", anomalies);
                return jsonObject;
            }
            jsonObject.put("anomalies", anomalies);
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "获取成功");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/anomaly/getAll 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(Anomaly anomaly, String anomalyStartTime, String anomalyEndTime) {
        JSONObject jsonObject = new JSONObject();

        anomaly.setAnomalyHiddenId(Long.valueOf(0));

        Timestamp startTime = Timestamp.valueOf(anomalyStartTime);
        Timestamp endTime = Timestamp.valueOf(anomalyEndTime);

        anomaly.setAnomalyEndDate(endTime);
        anomaly.setAnomalyStartDate(startTime);
        try {
            if (anomaly.getUserHiddenId() == null || userService.getById(anomaly.getUserHiddenId()) == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "用户不存在");
                return jsonObject;
            }

            if (anomaly.getWindowHiddenId() == null || windowService.getById(anomaly.getWindowHiddenId()) == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "窗口不存在");
                return jsonObject;
            }

            boolean result = anomalyService.save(anomaly);
            if (result) {
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "添加成功");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "添加失败");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/anomaly/insert 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "添加出现错误");
            return jsonObject;
        }
    }
/*
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(Anomaly anomaly) {
        JSONObject jsonObject = new JSONObject();

        try {
            Anomaly queryAnomaly = anomalyService.getOne(Wrappers.<Anomaly>lambdaQuery().eq(Anomaly::getId, anomaly.getId()));
            if (queryAnomaly != null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "异常ID已存在");
                return jsonObject;
            }
            boolean result = anomalyService.update(anomaly, Wrappers.<Anomaly>lambdaUpdate().eq(Anomaly::getId, anomaly.getId()));
            if (result) {
                jsonObject.put("result", "ok");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/anomaly/update 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }*/

    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object delete(String deleteId) {
        JSONObject jsonObject = new JSONObject();

        try {
            String[] deletes = deleteId.split(",");
            if (deletes.length <= 0)
            {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "没有选中的删除项");
                return jsonObject;
            }
            for (int i = 0; i < deletes.length; i++) {
                anomalyService.removeById(Long.valueOf(deletes[i]));
            }
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "删除成功");
            return jsonObject;

        } catch (Exception e) {
            logger.error("/anomaly/delete 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "删除出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
    public Object getOne(Long anomalyHiddenId) {
        JSONObject jsonObject = new JSONObject();

        try {
            Anomaly anomaly = anomalyService.getByHiddenId(anomalyHiddenId);
            if (anomaly != null) {
                jsonObject.put("result", "ok");
                jsonObject.put("anomaly", anomaly);
                jsonObject.put("msg", "获取成功");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "获取失败");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/anomaly/getOne 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/queryData", produces = "application/json;charset=utf-8")
    public Object search(@RequestParam(required = false) String event, @RequestParam(required = false) String windowId,
                         @RequestParam(required = false) String date, int pageNo, int pageSize) {
        JSONObject jsonObject = new JSONObject();

        try {
            // 分页构造器
            Page<Anomaly> page = new Page<Anomaly>(pageNo, pageSize);
            QueryWrapper<Anomaly> queryWrapper = new QueryWrapper<Anomaly>();
            if (event != null)
                queryWrapper.like ("anomaly_event",event);
            if (windowId != null)
                queryWrapper.like("window_id", windowId);
            if (date != null) {
                String start = date + " 00:00:00";
                String end = date + " 23:59:59";
                // System.out.println(end);
                Timestamp startTime = Timestamp.valueOf(start);
                Timestamp endTime = Timestamp.valueOf(end);
                queryWrapper.between("anomaly_start_date", start, end);
            }
            // 执行分页
            IPage<Anomaly> pageList = anomalyService.page(page, queryWrapper);
            // 返回结果
            if (pageList.getTotal() <= 0) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "搜素结果为空");
                return jsonObject;
            }
            else {
                jsonObject.put("pageList", pageList);
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "搜索成功");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/anomaly/queryData 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "搜索出现错误");
            return jsonObject;
        }
    }


}
