package com.roymark.queue.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.roymark.queue.entity.Floor;
import com.roymark.queue.entity.Window;
import com.roymark.queue.service.FloorService;
import com.roymark.queue.service.WindowService;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/floor")
public class FloorController {

    private static final Logger logger = LogManager.getLogger(FloorController.class);

    @Autowired
    private FloorService floorService;

    @Autowired
    private WindowService windowService;

    @RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
    public Object getAllFloors() {
        JSONObject jsonObject = new JSONObject();

        try {
            List<Floor> floors = floorService.list();
            if (floors.size() <= 0) {
                jsonObject.put("floors", floors);
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "暂无楼层");
            }
            jsonObject.put("floors", floors);
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "获取成功");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/floor/getAllFloors 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(Floor floor) {
        JSONObject jsonObject = new JSONObject();

        try {
            floor.setFloorHiddenId(Long.valueOf(0));
            Floor queryFloor = floorService.getOne(Wrappers.<Floor>lambdaQuery().eq(Floor::getFloorId, floor.getFloorId()));
            if (queryFloor != null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "楼层ID已存在");
                return jsonObject;
            }
            boolean result = floorService.save(floor);
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
            logger.error("/floor/insert 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "添加出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(Floor floor) {
        JSONObject jsonObject = new JSONObject();

        try {
            Floor queryFloor = floorService.getById(floor.getFloorHiddenId());

            if (queryFloor == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "楼层不存在");
                return jsonObject;
            }

            queryFloor = floorService.getOne(Wrappers.<Floor>lambdaQuery().eq(Floor::getFloorId, floor.getFloorId()));
            if (queryFloor != null && !queryFloor.getFloorHiddenId().equals(floor.getFloorHiddenId())) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "楼层ID已存在");
                return jsonObject;
            }
            boolean result = floorService.update(floor, Wrappers.<Floor>lambdaUpdate().eq(Floor::getFloorHiddenId, floor.getFloorHiddenId()));
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
            logger.error("/floor/update 错误:" + e.getMessage(), e);
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
                // 首先删除所有关联窗口
                Long deleteFloorHiddenId = Long.valueOf(deletes[i]);
                List<Window> windows = windowService.list(Wrappers.<Window>lambdaQuery().eq(Window::getFloorHiddenId, deleteFloorHiddenId));
                for (Window window : windows) {
                    windowService.deleteByWindowHiddenId(window.getWindowHiddenId());
                }
                floorService.removeById(deleteFloorHiddenId);
            }
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "删除成功");
            return jsonObject;

        } catch (Exception e) {
            logger.error("/floor/delete 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "删除出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
    public Object getOne(Long floorHiddenId) {
        JSONObject jsonObject = new JSONObject();

        try {
            Floor floor = floorService.getById(floorHiddenId);
            if (floor != null) {
                jsonObject.put("result", "ok");
                jsonObject.put("floor", floor);
                jsonObject.put("msg", "获取成功");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "获取失败");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/floor/getOne 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }
}

