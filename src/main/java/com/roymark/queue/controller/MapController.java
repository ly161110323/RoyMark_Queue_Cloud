package com.roymark.queue.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.*;
import com.roymark.queue.service.CameraService;
import com.roymark.queue.service.MapService;
import com.alibaba.fastjson.JSONObject;
import com.roymark.queue.util.UploadUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/map")
public class MapController {

    private static final Logger logger = LogManager.getLogger(MapController.class);

    @Autowired
    private MapService mapService;

    @Autowired
    private CameraService cameraService;

    @RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
    public Object getAll() {
        JSONObject jsonObject = new JSONObject();

        try {
            List<Map> maps = mapService.list();
            if (maps.size() <= 0) {
                jsonObject.put("maps", maps);
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "暂无区域");
            }
            jsonObject.put("maps", maps);
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "获取成功");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/map/getAll 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(HttpServletRequest request,
                         Map map, @RequestParam(value = "uploadMap", required = false) MultipartFile uploadMap) {
        JSONObject jsonObject = new JSONObject();

        try {
            map.setMapHiddenId(0L);
            if (uploadMap != null) {
                String uploadPath = "/uploads/map/";
                String filePath = UploadUtil.fileupload(request, uploadMap, uploadPath);
                map.setMapPath(filePath);
            }
            List<Map> maps = mapService.list();
            for (Map queryMap: maps) {
                if (map.getMapId().equals(queryMap.getMapId()) || map.getMapName().equals(queryMap.getMapName())) {
                    jsonObject.put("result", "no");
                    jsonObject.put("msg", "区域ID或名称已存在");
                    return jsonObject;
                }
            }
            boolean result = mapService.save(map);
            if (result) {
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "添加成功");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "添加失败");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/map/insert 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "添加出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(HttpServletRequest request,
                         Map map, @RequestParam(value = "uploadMap", required = false) MultipartFile uploadMap) {
        JSONObject jsonObject = new JSONObject();

        try {
            Map queryMap = mapService.getById(map.getMapHiddenId());

            if (queryMap == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "区域不存在");
                return jsonObject;
            }
            List<Map> maps = mapService.list(Wrappers.<Map>lambdaQuery().eq(Map::getMapId, map.getMapId())
                    .or().eq(Map::getMapName, map.getMapName()));

            // 为空表示 名字、ID都被修改为不存在值
            if (maps.size() == 0) {
            }
            // 查询到的只有一个且hiddenId相同，表明 名字、ID都没有被修改/某一个未被修改且其余修改值不存在
            else if (maps.size() == 1 && maps.get(0).getMapHiddenId().equals(map.getMapHiddenId())) {
            }
            else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "区域ID或名称已存在");
                return jsonObject;
            }

            if (queryMap != null && !queryMap.getMapHiddenId().equals(map.getMapHiddenId())) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "区域ID已存在");
                return jsonObject;
            }
            if (uploadMap != null) {
                String uploadPath = "/uploads/map/";
                String filePath = UploadUtil.fileupload(request, uploadMap, uploadPath);
                map.setMapPath(filePath);
            }
            boolean result = mapService.update(map, Wrappers.<Map>lambdaUpdate().eq(Map::getMapHiddenId, map.getMapHiddenId()));
            if (result) {
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "修改成功");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "修改失败");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/map/update 错误:" + e.getMessage(), e);
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
            for (int i=0; i<deletes.length; i++) {
                Map map = mapService.getById(Long.valueOf(deletes[i]));
                if (map == null) {
                    jsonObject.put("result", "error");
                    jsonObject.put("msg", "数据不存在");
                    return jsonObject;
                }

            }
            for (int i = 0; i < deletes.length; i++) {
                // 首先删除所有关联摄像头
                Long deleteMapHiddenId = Long.valueOf(deletes[i]);
                List<Camera> cameras = cameraService.list(Wrappers.<Camera>lambdaQuery().eq(Camera::getMapHiddenId, deleteMapHiddenId));
                for (Camera camera : cameras) {
                    cameraService.update(camera, Wrappers.<Camera>lambdaUpdate().set(Camera::getMapHiddenId, null)
                            .eq(Camera::getCamHiddenId, camera.getCamHiddenId()));
                }
                mapService.removeById(deleteMapHiddenId);
            }
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "删除成功");
            return jsonObject;

        } catch (Exception e) {
            logger.error("/map/delete 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "删除出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
    public Object getOne(Long mapHiddenId) {
        JSONObject jsonObject = new JSONObject();

        try {
            Map map = mapService.getById(mapHiddenId);
            if (map != null) {
                jsonObject.put("result", "ok");
                jsonObject.put("map", map);
                jsonObject.put("msg", "获取成功");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "获取失败");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/map/getOne 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }


    @RequestMapping(value = "/queryData", produces = "application/json;charset=utf-8")
    public Object search(int pageNo, int pageSize) {
        JSONObject jsonObject = new JSONObject();

        try {
            // 分页构造器
            Page<Map> page = new Page<>(pageNo, pageSize);
            QueryWrapper<Map> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByAsc("map_id");
            // 执行分页
            IPage<Map> pageList = mapService.page(page, queryWrapper);
            // 返回结果
            if (pageList.getRecords().size() <= 0) {
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
            logger.error("/map/queryData 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "搜索出现错误");
            return jsonObject;
        }
    }

}

