package com.roymark.queue.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.*;
import com.roymark.queue.service.*;
import com.alibaba.fastjson.JSONObject;
import com.roymark.queue.util.AnomalyDateControlUtil;
import com.roymark.queue.util.AnomalyMsgUtil;
import com.roymark.queue.util.web.HttpUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;
import java.util.Map;
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
    private CameraService cameraService;

    @Autowired
    private ServerService serverService;

    @Autowired
    private WindowService windowService;

    @Autowired
    private FaceVectorService faceVectorService;

    @Autowired
    private AnomalyUserService anomalyUserService;

    @Autowired
    private UserService userService;

    private final AnomalyDateControlUtil anomalyDateControlUtil = new AnomalyDateControlUtil();

    AnomalyMsgUtil anomalyMsgUtil = new AnomalyMsgUtil();

    /* 快速更新异常记录的状态 */
    @RequestMapping(value = "/updateAnomalyStatus", produces = "application/json;charset=utf-8")
    public Object updateAnomalyStatus(Long anomalyHiddenId, String anomalyStatus) {
        JSONObject jsonObject = new JSONObject();
        try {
            Boolean result = anomalyService.update(Wrappers.<Anomaly>lambdaUpdate().set(Anomaly::getAnomalyStatus, anomalyStatus)
                    .eq(Anomaly::getAnomalyHiddenId, anomalyHiddenId));
            if (result) {
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "修改成功");
            } else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "修改失败");
            }
            return jsonObject;
        } catch (Exception e) {
            logger.error("/anomaly/updateAnomalyStatus 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }

    }

    /* 接受从服务器端传来的消息 */
    @RequestMapping(value = "/updateAnomalyFromServer", produces = "application/json;charset=utf-8")
    public void updateAnomalyFromServer(Anomaly anomaly, String imagePath, String videoPath,
                                        String boxIds) {

        try {
//            System.out.println(anomaly);
//            System.out.println(imagePath);
//            System.out.println(videoPath);
//            System.out.println("BR:" + boxIds);
            String[] boxIdArray = boxIds.split(",");
            List<String> boxIdList = Arrays.asList(boxIdArray);

            System.out.println(anomaly);
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
                        for (int i = 0; i < imagePaths.length; i++) {
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
                anomaly.setAnomalyImagePath(anomalyImagePath.deleteCharAt(anomalyImagePath.length() - 1).toString());

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

            // 根据windowHiddenId查询默认的最新的userHiddenId
            Window window = windowService.getById(anomaly.getWindowHiddenId());
            if (window != null) {
                Date lastUpdateTime = window.getUserUpdateTime();
                Date currentTime = new Date();
                if (lastUpdateTime != null && currentTime.getTime() - lastUpdateTime.getTime() <= 10 * 1000 * 60) // 10分钟以内以更新的用户为准
                    anomaly.setUserHiddenId(window.getUserHiddenId());
                    anomaly.setAnomalyFaceConfidence(window.getUserFaceConfidence());
//            }
            }

            Long boxAnomalyHiddenId;         // 记录box对应的Anomaly

            // 首先检查是否有与开始时间完全相同的一项
            Timestamp startTime = anomaly.getAnomalyStartDate();

            Anomaly queryAnomaly = anomalyService.getOne(Wrappers.<Anomaly>lambdaQuery().eq(Anomaly::getAnomalyStartDate, startTime)
                    .eq(Anomaly::getWindowHiddenId, anomaly.getWindowHiddenId())
                    .eq(Anomaly::getAnomalyEvent, anomaly.getAnomalyEvent()));

            if (queryAnomaly != null) {         // 如果存在，则对该项进行更新
                anomaly.setAnomalyHiddenId(queryAnomaly.getAnomalyHiddenId());
                anomalyService.updateById(anomaly);
                boxAnomalyHiddenId = queryAnomaly.getAnomalyHiddenId();
            } else {
//                // 若新增的开始时间与表内某个的结束时间差值<1分钟，则认为是表内该项的继续
//                Date endTimeThreshold = new Date(startTime.getTime() - 60 * 1000);
//                Date startTimeThreshold = new Date(startTime.getTime() + 60 * 100);
//                queryAnomaly = anomalyService.getOne(Wrappers.<Anomaly>lambdaQuery().between(Anomaly::getAnomalyEndDate, endTimeThreshold, startTimeThreshold)
//                        .eq(Anomaly::getWindowHiddenId, anomaly.getWindowHiddenId())
//                        .eq(Anomaly::getAnomalyEvent, anomaly.getAnomalyEvent()));
//
//                if (queryAnomaly != null) {                 // 如果差值<1分钟，更新该项
//                    anomaly.setAnomalyStartDate(queryAnomaly.getAnomalyStartDate());
//                    anomaly.setAnomalyHiddenId(queryAnomaly.getAnomalyHiddenId());
//                    anomalyService.updateById(anomaly);
//                    boxAnomalyHiddenId = queryAnomaly.getAnomalyHiddenId();
//                } else {
                anomaly.setAnomalyHiddenId((long) 0);
                anomalyService.save(anomaly);
                Anomaly newAnomaly = anomalyService.getOne(Wrappers.<Anomaly>lambdaQuery().eq(Anomaly::getAnomalyEvent, anomaly.getAnomalyEvent())
                        .eq(Anomaly::getWindowHiddenId, anomaly.getWindowHiddenId())
                        .eq(Anomaly::getAnomalyStartDate, anomaly.getAnomalyStartDate()));
                boxAnomalyHiddenId = newAnomaly.getAnomalyHiddenId();
//              }
            }

            // 接收时间和结束时间控制

            anomalyDateControlUtil.deal(boxAnomalyHiddenId, anomaly.getAnomalyEndDate());
            // 将获取到的异常id和最近接收时间的映射更新进数据库
//            System.out.println(idAndDateMap);
//            if (idAndDateMap != null) {
//                for (Map.Entry<Long, Date> entry : idAndDateMap.entrySet()) {
//                    LambdaUpdateWrapper<Anomaly> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//                    lambdaUpdateWrapper.eq(Anomaly::getAnomalyHiddenId, entry.getKey())
//                            .set(Anomaly::getAnomalyEndDate, entry.getValue())
//                            .set(Anomaly::getAnomalyEndDateValid, false);
//                    anomalyService.update(null, lambdaUpdateWrapper);
//                }
//
//            }

            // 获取的已匹配的faceId与anomalyHiddenId的Map
            Map<String, Double> anomalyFaceMap = anomalyMsgUtil.addMap(boxIdList, boxAnomalyHiddenId);
            if (anomalyFaceMap != null) {
                // 向异常与用户的中间表中加入匹配的信息
                for (Map.Entry<String, Double> entry : anomalyFaceMap.entrySet()) {
                    String faceId = entry.getKey();
                    FaceVector faceVector = faceVectorService.getOne(Wrappers.<FaceVector>lambdaQuery().eq(FaceVector::getFaceId, faceId));
                    if (faceVector != null) {
                        Long userHiddenId = faceVector.getUserHiddenId();
                        anomalyUserService.checkInsert(new AnomalyUser((long) 0, boxAnomalyHiddenId, userHiddenId, entry.getValue()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
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

        if (!anomalyStartTime.equals("")) {
            Timestamp startTime = Timestamp.valueOf(anomalyStartTime);
            anomaly.setAnomalyStartDate(startTime);
        }
        if (!anomalyEndTime.equals("")) {
            Timestamp endTime = Timestamp.valueOf(anomalyEndTime);
            anomaly.setAnomalyEndDate(endTime);
        }

        try {

            if (anomalyService.getById(anomaly.getAnomalyHiddenId()) == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "异常记录不存在");
                return jsonObject;
            }
            boolean result;
            if (anomaly.getUserHiddenId() != null && anomaly.getWindowHiddenId() != null) {
                result = anomalyService.update(anomaly, Wrappers.<Anomaly>lambdaUpdate().eq(Anomaly::getAnomalyHiddenId, anomaly.getAnomalyHiddenId()));
            } else if (anomaly.getUserHiddenId() != null) {
                result = anomalyService.update(anomaly, Wrappers.<Anomaly>lambdaUpdate().set(Anomaly::getWindowHiddenId, null).eq(Anomaly::getAnomalyHiddenId, anomaly.getAnomalyHiddenId()));
            } else if (anomaly.getWindowHiddenId() != null) {
                result = anomalyService.update(anomaly, Wrappers.<Anomaly>lambdaUpdate().set(Anomaly::getUserHiddenId, null).eq(Anomaly::getAnomalyHiddenId, anomaly.getAnomalyHiddenId()));
            } else {
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
            if (deletes.length <= 0) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "没有选中的删除项");
                return jsonObject;
            }
            for (int i = 0; i < deletes.length; i++) {
                Anomaly anomaly = anomalyService.getById(Long.valueOf(deletes[i]));
                if (anomaly == null) {
                    jsonObject.put("result", "error");
                    jsonObject.put("msg", "数据不存在");
                    return jsonObject;
                }
            }
            for (int i = 0; i < deletes.length; i++) {
                anomalyService.removeById(Long.valueOf(deletes[i]));
                anomalyUserService.remove(Wrappers.<AnomalyUser>lambdaUpdate().eq(AnomalyUser::getAnomalyHiddenId
                        , Long.valueOf(deletes[i])));
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
                         @RequestParam(required = false) String date, @RequestParam(required=false) String userName,
                         @RequestParam(required = false) String anomalyStatus, int pageNo, int pageSize) {
        JSONObject jsonObject = new JSONObject();
        try {
            // 分页构造器
            Page<Anomaly> page = new Page<Anomaly>(pageNo, pageSize);
            QueryWrapper<Anomaly> queryWrapper = new QueryWrapper<Anomaly>();
            if (event != null)
                queryWrapper.like("anomaly_event", event);
            if (windowId != null)
                queryWrapper.like("window_id", windowId);
            if (date != null) {
                String start = date + " 00:00:00";
                String end = date + " 23:59:59";
                queryWrapper.between("anomaly_start_date", start, end);
            }
            if (userName != null) {
                // 查询模糊用户
                List<ActionUser> users = userService.list(Wrappers.<ActionUser>lambdaQuery().like(ActionUser::getUserName, userName));
                Set<Long> anomalyHiddenIdSet = new HashSet<>();     // 去除重复
                anomalyHiddenIdSet.add((long)-1);                   // 该Set不能为空，加入-1值，若users为空则以-1查询为空
                for (ActionUser user: users) {
                    // 获取用户Id对应的多个AnomalyUser(通过行为和人脸进行绑定的)
                    List<AnomalyUser> anomalyUsers = anomalyUserService.list(Wrappers.<AnomalyUser>lambdaQuery().eq(AnomalyUser::getUserHiddenId, user.getUserHiddenId()));
                    for (AnomalyUser anomalyUser: anomalyUsers) {
                        // 存储用户所对应的anomalyHiddenId
                        anomalyHiddenIdSet.add(anomalyUser.getAnomalyHiddenId());
                    }
                    // 处理窗口中默认的
                    List<Anomaly> anomalies = anomalyService.list(Wrappers.<Anomaly>lambdaQuery()
                            .eq(Anomaly::getUserHiddenId, user.getUserHiddenId())
                            .eq(Anomaly::getDefaultUserFlag, true));
                    for (Anomaly anomaly: anomalies) {
                        anomalyHiddenIdSet.add(anomaly.getAnomalyHiddenId());
                    }

                }
                // 选择选中的anomalyHiddenId
                queryWrapper.in("anomaly_hidden_id", anomalyHiddenIdSet);

            }
            if (anomalyStatus != null) {
                String[] array = anomalyStatus.split(",");
                if (array.length > 0) {
                    queryWrapper.in("anomaly_status", Arrays.asList(array));
                }
            }
            queryWrapper.orderByDesc("anomaly_start_date");
            // 执行分页
            IPage<Anomaly> pageList = anomalyService.page(page, queryWrapper);
            // 返回结果
            if (pageList.getRecords().size() <= 0) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "搜素结果为空");
                jsonObject.put("pageList", pageList);
                return jsonObject;
            } else {
                for (Anomaly anomaly : pageList.getRecords()) {
                    // 如果获取到的UserIds为空，则填入默认的user（根据userHiddenId
                    if (anomaly.getUserShortInfos().size() == 0 && anomaly.getUserHiddenId() != null) {
                        ActionUser user = userService.getById(anomaly.getUserHiddenId());
                        addDefaultUser(anomaly, user);
                    }
                }
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

    @RequestMapping(value = "/getLatestAnomaly", produces = "application/json;charset=utf-8")
    public Object getLatestAnomaly(Long mapHiddenId) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Camera> cameras = cameraService.list(Wrappers.<Camera>lambdaQuery().eq(Camera::getMapHiddenId, mapHiddenId));
            if (cameras.size() <= 0) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "当前楼层无摄像头");
                return jsonObject;
            }
            // 摄像头id,摄像头状态，摄像头所处服务器状态一一对应的List；同时摄像头对应多个窗口，同时每个窗口对应不超过3个最新异常
            Map<List<Long>, List<Anomaly>> cameraMap = new HashMap<>();
            List<Anomaly> anomalies;
            for (Camera camera : cameras) {
                Long camHiddenId = camera.getCamHiddenId();
                // 获取的异常满足窗口，结束时间为空且有效，并且异常状态有效或待处理
                anomalies = anomalyService.list(Wrappers.<Anomaly>lambdaQuery()
                        .eq(Anomaly::getCamHiddenId, camHiddenId)
                        .isNull(Anomaly::getAnomalyEndDate)
                        .eq(Anomaly::getAnomalyEndDateValid, true)
                        .ne(Anomaly::getAnomalyStatus, "invalid")
                        .orderByDesc(Anomaly::getAnomalyStartDate));
                if (anomalies.size() > 3) {
                    anomalies = anomalies.subList(0, 3);
                }
                for (Anomaly anomaly: anomalies) {
                    if (anomaly.getUserShortInfos().size() == 0 && anomaly.getUserHiddenId() != null) {
                        ActionUser user = userService.getById(anomaly.getUserHiddenId());
                        addDefaultUser(anomaly, user);
                    }
                }
                List<Long> camInfoList = new ArrayList<>();
                // 设置摄像头及其相关状态
                camInfoList.add(camHiddenId);
                camInfoList.add(cameraService.getCamStatus(camera)?(long)1:0);
                camInfoList.add(serverService.getServerOnStatus(camera.getServerHiddenId())?(long)1:0);
                cameraMap.put(camInfoList, anomalies);
            }
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "获取成功");
            jsonObject.put("data", cameraMap);
            return jsonObject;
        } catch (Exception e) {
            logger.error("/camera/getLatestAnomaly 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }

    public void addDefaultUser(Anomaly anomaly, ActionUser user) {
        if (user != null) {
            List<UserShortInfo> userShortInfos = new ArrayList<>();
            userShortInfos.add(new UserShortInfo(user.getUserHiddenId(), user.getUserName(), anomaly.getAnomalyFaceConfidence()));
            anomaly.setUserShortInfos(userShortInfos);
        }
    }

    @RequestMapping(value = "/testt", produces = "application/json;charset=utf-8")
    public Object Test2() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("map",anomalyDateControlUtil.getAnomalyControlMap());
        return jsonObject;
    }

}
