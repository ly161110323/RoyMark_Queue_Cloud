package com.roymark.queue.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.roymark.queue.entity.Floor;
import com.roymark.queue.service.FloorService;
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

    @RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
    public Object getAllFloors() {
        JSONObject jsonObject = new JSONObject();

        try {
            List<Floor> floors = floorService.list();
            jsonObject.put("floors", floors);
            jsonObject.put("result", "ok");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/floor/getAllFloors 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(Floor floor) {
        JSONObject jsonObject = new JSONObject();

        try {
            boolean result = floorService.save(floor);
            if (result) {
                jsonObject.put("result", "ok");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/floor/insert 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(Floor floor) {
        JSONObject jsonObject = new JSONObject();

        try {
            boolean result = floorService.update(floor, Wrappers.<Floor>lambdaUpdate().eq(Floor::getFloorHiddenId, floor.getFloorHiddenId()));
            if (result) {
                jsonObject.put("result", "ok");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/floor/update 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object delete(Long floorId) {
        JSONObject jsonObject = new JSONObject();

        try {
            boolean result = floorService.removeById(floorId);
            if (result) {
                jsonObject.put("result", "ok");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/floor/delete 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
    public Object getOne(Long floorId) {
        JSONObject jsonObject = new JSONObject();

        try {
            Floor floor = floorService.getById(floorId);
            if (floor != null) {
                jsonObject.put("result", "ok");
                jsonObject.put("floor", floor);
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/floor/getOne 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }
}

