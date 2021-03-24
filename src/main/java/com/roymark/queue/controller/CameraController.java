package com.roymark.queue.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roymark.queue.entity.Camera;
import com.roymark.queue.service.CameraService;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/camera")
public class CameraController {
private static final Logger logger = LogManager.getLogger(CameraController.class);
    
	@Autowired
    private CameraService cameraService;
	
	@RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
	public Object getAllServers() {
		JSONObject jsonObject = new JSONObject();
	
		try {
			List<Camera> cameras = cameraService.list();
			jsonObject.put("cameras", cameras);
			jsonObject.put("result", "ok");
			return jsonObject;
		} catch (Exception e) {
			logger.error("/camera/getAllCameras 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
	public Object insert(Camera camera) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			boolean result = cameraService.save(camera);
			if (result) {
				jsonObject.put("result", "ok");
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/camera/insert 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
	public Object update(Camera camera) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			boolean result = cameraService.update(camera, null);
			if (result) {
				jsonObject.put("result", "ok");
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/camera/update 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
	public Object delete(Long cameraId) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			boolean result = cameraService.removeById(cameraId);
			if (result) {
				jsonObject.put("result", "ok");
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/camera/delete 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
	public Object getOne(Long cameraId) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			Camera camera = cameraService.getById(cameraId);
			if (camera != null) {
				jsonObject.put("result", "ok");
				jsonObject.put("camera", camera);
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/camera/getOne 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}
}
