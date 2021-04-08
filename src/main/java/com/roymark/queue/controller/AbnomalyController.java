package com.roymark.queue.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.Abnomaly;
import com.roymark.queue.service.AbnomalyService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/abnomaly")
public class AbnomalyController {
    private static final Logger logger = LogManager.getLogger(AbnomalyController.class);

    @Autowired
    private AbnomalyService abnomalyService;

    @Autowired
    private UserService userService;

    @Autowired
    private WindowService windowService;

    @RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
    public Object getAllAbnomalies() {
        JSONObject jsonObject = new JSONObject();

        try {
            List<Abnomaly> abnomalies = abnomalyService.getAllAbnomalies();

            if (abnomalies.size() <= 0) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "获取结果为空");
                jsonObject.put("abnomalies", abnomalies);
                return jsonObject;
            }
            jsonObject.put("abnomalies", abnomalies);
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "获取成功");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/abnomaly/getAll 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(Abnomaly abnomaly, String abnomalyStartTime, String abnomalyEndTime) {
        JSONObject jsonObject = new JSONObject();

        abnomaly.setAbnomalyHiddenId(Long.valueOf(0));

        Timestamp startTime = Timestamp.valueOf(abnomalyStartTime);
        Timestamp endTime = Timestamp.valueOf(abnomalyEndTime);

        abnomaly.setAbnomalyEndDate(endTime);
        abnomaly.setAbnomalyStartDate(startTime);
        try {
            if (abnomaly.getUserHiddenId() == null || userService.getById(abnomaly.getUserHiddenId()) == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "用户不存在");
                return jsonObject;
            }

            if (abnomaly.getWindowHiddenId() == null || windowService.getById(abnomaly.getWindowHiddenId()) == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "窗口不存在");
                return jsonObject;
            }
            Abnomaly queryAbnomaly = abnomalyService.getOne(Wrappers.<Abnomaly>lambdaQuery().eq(Abnomaly::getAbnomalyId, abnomaly.getAbnomalyId()));
            if (queryAbnomaly != null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "异常ID已存在");
                return jsonObject;
            }
            boolean result = abnomalyService.save(abnomaly);
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
            logger.error("/abnomaly/insert 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "添加出现错误");
            return jsonObject;
        }
    }
/*
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(Abnomaly abnomaly) {
        JSONObject jsonObject = new JSONObject();

        try {
            Abnomaly queryAbnomaly = abnomalyService.getOne(Wrappers.<Abnomaly>lambdaQuery().eq(Abnomaly::getId, abnomaly.getId()));
            if (queryAbnomaly != null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "异常ID已存在");
                return jsonObject;
            }
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
                abnomalyService.removeById(Long.valueOf(deletes[i]));
            }
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "删除成功");
            return jsonObject;

        } catch (Exception e) {
            logger.error("/abnomaly/delete 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "删除出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
    public Object getOne(Long abnomalyHiddenId) {
        JSONObject jsonObject = new JSONObject();

        try {
            Abnomaly abnomaly = abnomalyService.getByHiddenId(abnomalyHiddenId);
            if (abnomaly != null) {
                jsonObject.put("result", "ok");
                jsonObject.put("abnomaly", abnomaly);
                jsonObject.put("msg", "获取成功");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "获取失败");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/abnomaly/getOne 错误:" + e.getMessage(), e);
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
            Page<Abnomaly> page = new Page<Abnomaly>(pageNo, pageSize);
            QueryWrapper<Abnomaly> queryWrapper = new QueryWrapper<Abnomaly>();
            if (event != null)
                queryWrapper.like ("abnomaly_event",event);
            if (windowId != null)
                queryWrapper.like("window_id", windowId);
            if (date != null) {
                String start = date + " 00:00:00";
                String end = date + " 23:59:59";
                // System.out.println(end);
                Timestamp startTime = Timestamp.valueOf(start);
                Timestamp endTime = Timestamp.valueOf(end);
                queryWrapper.between("abnomaly_start_date", start, end);
            }
            // 执行分页
            IPage<Abnomaly> pageList = abnomalyService.page(page, queryWrapper);
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
            logger.error("/abnomaly/queryData 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "搜索出现错误");
            return jsonObject;
        }
    }


}
