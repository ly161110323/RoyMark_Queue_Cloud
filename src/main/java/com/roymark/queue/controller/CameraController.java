package com.roymark.queue.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.util.web.HttpUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roymark.queue.entity.Camera;
import com.roymark.queue.service.CameraService;

import com.alibaba.fastjson.JSONObject;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/camera")
public class CameraController {
	private static final Logger logger = LogManager.getLogger(CameraController.class);

	@Autowired
	private CameraService cameraService;

	@RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
	public Object getAllCameras() {
		JSONObject jsonObject = new JSONObject();

		try {
			List<Camera> cameras = cameraService.getAllCamera();
			if (cameras.size() <= 0) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "暂无摄像头");
				jsonObject.put("cameras", cameras);
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
			boolean result;
			if (camera.getServerHiddenId() != null) {
				result = cameraService.update(camera, Wrappers.<Camera>lambdaUpdate().eq(Camera::getCamHiddenId, camera.getCamHiddenId()));
			}
			else {
				result = cameraService.update(camera, Wrappers.<Camera>lambdaUpdate().set(Camera::getServerHiddenId, null).eq(Camera::getCamHiddenId, camera.getCamHiddenId()));
			}

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
			logger.error("/camera/delete 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "删除出现错误");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
	public Object getOne(Long cameraHiddenId) {
		JSONObject jsonObject = new JSONObject();

		try {
			Camera camera = cameraService.getCameraByHiddenId(cameraHiddenId);
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

	@RequestMapping(value = "/queryData", produces = "application/json;charset=utf-8")
	public Object search(@RequestParam(required = false) String camId,
						 @RequestParam(required = false) String serverId, int pageNo, int pageSize) {
		JSONObject jsonObject = new JSONObject();

		try {
			// 分页构造器
			Page<Camera> page = new Page<Camera>(pageNo, pageSize);
			QueryWrapper<Camera> queryWrapper = new QueryWrapper<Camera>();
			if (camId != null)
				queryWrapper.like ("cam_id",camId);
			if (serverId != null)
				queryWrapper.like("server_id", serverId);
			// 执行分页
			IPage<Camera> pageList = cameraService.page(page, queryWrapper);
			for (Camera camera: pageList.getRecords()) {
				boolean result = HttpUtils.isHostReachable(camera.getCamIp(), 500);
				if (result) {
					camera.setCamStatus("正常");
				}
				else {
					camera.setCamStatus("异常");
				}
			}
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
			logger.error("/camera/queryData 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "搜索出现错误");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/getCurrentPic", produces = "application/json;charset=utf-8")
	public Object getCurrentPic(HttpServletRequest request, Long cameraHiddenId) {
		JSONObject jsonObject = new JSONObject();

		try {
			Camera camera = cameraService.getById(cameraHiddenId);
			if (camera == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "摄像头不存在");
				return jsonObject;
			}
			FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(camera.getCamVideoAddr());
			if (grabber == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "grabber创建失败");
				return jsonObject;
			}
			grabber.setOption("rtsp_transport", "tcp");

			//设置帧率
			grabber.setFrameRate(25);
			//设置获取的视频宽度
			grabber.setImageWidth(960);
			//设置获取的视频高度
			grabber.setImageHeight(540);
			//设置视频bit率
			grabber.setVideoBitrate(3000000);

			grabber.start();
			Frame frame = null;
			for (int i=0; i<10; i++)
				frame = grabber.grabImage();
			if (frame == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "捕获失败");
				return jsonObject;
			}
			BufferedImage image = new Java2DFrameConverter().getBufferedImage(frame);

			grabber.stop();
			String path = "/uploads/camera/" + camera.getCamId()+".jpg";

			ImageIO.write(image, "jpg", new File(request.getServletContext().getRealPath("") + path));

			jsonObject.put("path", path);
			jsonObject.put("result", "ok");
			jsonObject.put("msg", "捕获成功");
			return jsonObject;


		} catch (Exception e) {
			logger.error("/camera/getCurrentPic 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "捕获出现错误");
			return jsonObject;
		}
	}

}
