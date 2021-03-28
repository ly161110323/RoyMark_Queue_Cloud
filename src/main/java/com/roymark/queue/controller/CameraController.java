package com.roymark.queue.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.roymark.queue.entity.Window;
import com.roymark.queue.service.WindowService;
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

	@Autowired
	private WindowService windowService;
	
	@RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
	public Object getAllCameras() {
		JSONObject jsonObject = new JSONObject();
	
		try {
			List<Camera> cameras = cameraService.list();
			if (cameras.size() <= 0) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "暂无摄像头");
				return jsonObject;
			}
			for (Camera camera: cameras) {
				camera.setCamStatus("未启动");
			}
			jsonObject.put("cameras", cameras);
			jsonObject.put("result", "ok");
			jsonObject.put("msg", "获取成功");
			return jsonObject;
		} catch (Exception e) {
			logger.error("/camera/getAllCameras 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "获取出现错误");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
	public Object insert(Camera camera, String cameraBirthStr) {
		JSONObject jsonObject = new JSONObject();

		try {
			camera.setCamHiddenId(Long.valueOf(0));
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date cameraBirth = simpleDateFormat.parse(cameraBirthStr);
			camera.setCamBirth(cameraBirth);
			if (camera.getWindowId() == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "未设置窗口");
				return jsonObject;
			}
			if (windowService.getOne(Wrappers.<Window>lambdaQuery().eq(Window::getWindowId, camera.getWindowId())) == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "设置的窗口不存在");
				return jsonObject;
			}
			Camera queryCamera = cameraService.getOne(Wrappers.<Camera>lambdaQuery().eq(Camera::getCamId, camera.getCamId()));
			if (queryCamera != null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "摄像头ID已存在");
				return jsonObject;
			}
			boolean result = cameraService.save(camera);
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
			logger.error("/camera/insert 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "添加出现错误");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
	public Object update(Camera camera, String cameraBirthStr) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date cameraBirth = simpleDateFormat.parse(cameraBirthStr);
			camera.setCamBirth(cameraBirth);
			if (camera.getWindowId() == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "未设置窗口");
				return jsonObject;
			}
			if (windowService.getOne(Wrappers.<Window>lambdaQuery().eq(Window::getWindowId, camera.getWindowId())) == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "设置的窗口不存在");
				return jsonObject;
			}
			Camera queryCamera = cameraService.getById(camera.getCamHiddenId());
			if (queryCamera == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "摄像头不存在");
				return jsonObject;
			}

			queryCamera = cameraService.getOne(Wrappers.<Camera>lambdaQuery().eq(Camera::getCamId, camera.getCamId()));
			if (queryCamera != null && !queryCamera.getCamHiddenId().equals(camera.getCamHiddenId())) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "摄像头ID已存在");
				return jsonObject;
			}
			boolean result = cameraService.update(camera, Wrappers.<Camera>lambdaUpdate().eq(Camera::getCamHiddenId, camera.getCamHiddenId()));
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
			logger.error("/camera/update 错误:" + e.getMessage(), e);
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
				cameraService.removeById(deletes[i]);
			}
			jsonObject.put("result", "ok");
			jsonObject.put("msg", "删除成功");
			return jsonObject;

		} catch (Exception e) {
			logger.error("/server/delete 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "删除出现错误");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
	public Object getOne(Long cameraHiddenId) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			Camera camera = cameraService.getById(cameraHiddenId);
			if (camera != null) {
				jsonObject.put("result", "ok");
				jsonObject.put("camera", camera);
				jsonObject.put("msg", "获取成功");
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "获取失败");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/camera/getOne 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "获取出现错误");
			return jsonObject;
		}
	}
}
