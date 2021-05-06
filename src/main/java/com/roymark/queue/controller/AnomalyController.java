package com.roymark.queue.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.ActionUser;
import com.roymark.queue.entity.Anomaly;
import com.roymark.queue.entity.Camera;
import com.roymark.queue.entity.Server;
import com.roymark.queue.service.AnomalyService;
import com.alibaba.fastjson.JSONObject;
import com.roymark.queue.service.CameraService;
import com.roymark.queue.service.ServerService;
import com.roymark.queue.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/anomaly")
public class AnomalyController {
    private static final Logger logger = LogManager.getLogger(AnomalyController.class);

    @Autowired
    private AnomalyService anomalyService;

    @Autowired
    private UserService userService;

    @Autowired
    private CameraService cameraService;

    @Autowired
    private ServerService serverService;

    @RequestMapping(value = "/updateAnomalyFromServer", produces = "application/json;charset=utf-8")
    public void updateAnomalyFromServer(Anomaly anomaly, String imagePath, String videoPath) {
        try {
            if (anomaly.getAnomalyEvent() == null || anomaly.getWindowHiddenId() == null || anomaly.getAnomalyStartDate() == null) {
                logger.info("回传格式有误");
                return;
            }
            //申明线程池
            ExecutorService threadPool = Executors.newFixedThreadPool(1);
            Callable<Object> callable = new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 200);
                    return jsonObject;
                }
            };
            threadPool.submit(callable).get();

            // 获取服务器信息
            String[] imagePaths = imagePath.split(",");
            StringBuilder anomalyImagePath = new StringBuilder();
            if (anomaly.getCamHiddenId() != null && imagePaths.length > 0 && imagePaths.length < 6) {       // 最多保存6张
                Camera camera = cameraService.getById(anomaly.getCamHiddenId());
                if (camera != null) {
                    Server server = serverService.getById(camera.getServerHiddenId());
                    if (server != null) {
                        for (int i=0; i<imagePaths.length; i++) {
                            if (!imagePaths[i].equals("")) {
                                StringBuilder str = new StringBuilder();
                                str.append("http://");
                                str.append(server.getServerIp());
                                str.append(":");
                                str.append(server.getServerPort());
                                str.append(imagePaths[i]);
                                anomalyImagePath.append(str);
                                anomalyImagePath.append(",");
                            }
                        }
                    }
                }
            }
            if (anomalyImagePath.length() > 0)
                anomaly.setAnomalyImagePath(anomalyImagePath.deleteCharAt(anomalyImagePath.length()-1).toString());

            // 将服务器信息加到视频路径中
            StringBuilder anomalyVideoPath = new StringBuilder();
            if (anomaly.getCamHiddenId() != null && !videoPath.equals("")) {
                Camera camera = cameraService.getById(anomaly.getCamHiddenId());
                if (camera != null) {
                    Server server = serverService.getById(camera.getServerHiddenId());
                    if (server != null) {
                        anomalyVideoPath.append("http://");
                        anomalyVideoPath.append(server.getServerIp());
                        anomalyVideoPath.append(":");
                        anomalyVideoPath.append(server.getServerPort());
                        anomalyVideoPath.append(videoPath);
                    }
                }
            }
            anomaly.setAnomalyVideoPath(anomalyVideoPath.toString());

            // 根据windowHiddenId查询userHiddenId
            ActionUser user = userService.getOne(Wrappers.<ActionUser>lambdaQuery().eq(ActionUser::getWindowHiddenId, anomaly.getWindowHiddenId()));
            if (user == null) {
                logger.info("该窗口id无对应的用户");
                anomaly.setUserHiddenId(null);
                //return;
            }
            else {
                anomaly.setUserHiddenId(user.getUserHiddenId());
            }


            // 首先检查是否有与开始时间完全相同的一项
            Timestamp startTime = anomaly.getAnomalyStartDate();

            Anomaly queryAnomaly = anomalyService.getOne(Wrappers.<Anomaly>lambdaQuery().eq(Anomaly::getAnomalyStartDate, startTime)
                    .eq(Anomaly::getWindowHiddenId, anomaly.getWindowHiddenId())
                    .eq(Anomaly::getAnomalyEvent, anomaly.getAnomalyEvent()));

            if (queryAnomaly != null && queryAnomaly.equals(anomaly)) {     // 如果与该项完全相同，不做处理
                return;
            }
            else if (queryAnomaly != null) {         // 如果存在且不同，则对该项进行更新
                anomaly.setAnomalyHiddenId(queryAnomaly.getAnomalyHiddenId());
                anomalyService.updateById(anomaly);
            }
            else {
                // 若新增的开始时间与表内某个的结束时间差值<1分钟，则认为是表内该项的继续
                Date endTimeThreshold = new Date(startTime.getTime() - 60 * 1000);
                queryAnomaly = anomalyService.getOne(Wrappers.<Anomaly>lambdaQuery().between(Anomaly::getAnomalyEndDate, endTimeThreshold, startTime)
                        .eq(Anomaly::getWindowHiddenId, anomaly.getWindowHiddenId())
                        .eq(Anomaly::getAnomalyEvent, anomaly.getAnomalyEvent()));

                if (queryAnomaly != null) {                 // 如果差值<1分钟，更新该项
                    anomaly.setAnomalyStartDate(queryAnomaly.getAnomalyStartDate());
                    anomaly.setAnomalyHiddenId(queryAnomaly.getAnomalyHiddenId());
                    anomalyService.updateById(anomaly);
                }
                else {
                    anomaly.setAnomalyHiddenId(Long.valueOf(0));
                    anomalyService.save(anomaly);
                }
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

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

    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(Anomaly anomaly, String anomalyStartTime, String anomalyEndTime) {
        JSONObject jsonObject = new JSONObject();

        Timestamp startTime = Timestamp.valueOf(anomalyStartTime);
        Timestamp endTime = Timestamp.valueOf(anomalyEndTime);

        anomaly.setAnomalyEndDate(endTime);
        anomaly.setAnomalyStartDate(startTime);
        try {

            if (anomalyService.getById(anomaly.getAnomalyHiddenId()) == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "异常记录不存在");
                return jsonObject;
            }
            boolean result;
            if (anomaly.getUserHiddenId() != null && anomaly.getWindowHiddenId() != null) {
                result = anomalyService.update(anomaly, Wrappers.<Anomaly>lambdaUpdate().eq(Anomaly::getAnomalyHiddenId, anomaly.getAnomalyHiddenId()));
            }
            else if (anomaly.getUserHiddenId() != null) {
                result = anomalyService.update(anomaly, Wrappers.<Anomaly>lambdaUpdate().set(Anomaly::getWindowHiddenId, null).eq(Anomaly::getAnomalyHiddenId, anomaly.getAnomalyHiddenId()));
            }
            else if (anomaly.getWindowHiddenId() != null) {
                result = anomalyService.update(anomaly, Wrappers.<Anomaly>lambdaUpdate().set(Anomaly::getUserHiddenId, null).eq(Anomaly::getAnomalyHiddenId, anomaly.getAnomalyHiddenId()));
            }
            else {
                result = anomalyService.update(anomaly, Wrappers.<Anomaly>lambdaUpdate().set(Anomaly::getUserHiddenId, null).set(Anomaly::getWindowHiddenId, null).eq(Anomaly::getAnomalyHiddenId, anomaly.getAnomalyHiddenId()));
            }
            if (result) {
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "修改成功");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "修改失败");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/anomaly/update 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "修改出现错误");
            return jsonObject;
        }
    }

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
            queryWrapper.orderByDesc("anomaly_start_date");
            // 执行分页
            IPage<Anomaly> pageList = anomalyService.page(page, queryWrapper);
            // 返回结果
            if (pageList.getTotal() <= 0) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "搜素结果为空");
                jsonObject.put("pageList", pageList);
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
