package com.roymark.queue.controller;

import java.sql.Wrapper;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roymark.queue.entity.Window;
import com.roymark.queue.service.WindowService;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/window")
public class WindowController {

	private static final Logger logger = LogManager.getLogger(WindowController.class);
    
	@Autowired
    private WindowService windowSerivce;
	
	@RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
	public Object getAllWindows() {
		System.out.println("fuck");
		JSONObject jsonObject = new JSONObject();
	
		try {
			List<Window> windows = windowSerivce.list();
			jsonObject.put("windows", windows);
			jsonObject.put("result", "ok");
			return jsonObject;
		} catch (Exception e) {
			logger.error("/window/getAllWindows 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
	public Object insert(Window window) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			boolean result = windowSerivce.save(window);
			if (result) {
				jsonObject.put("result", "ok");
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/window/insert 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
	public Object update(Window window) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			// boolean result = windowSerivce.update(window, new UpdateWrapper<Window>().eq("window_id", window.getId()));
			boolean result = windowSerivce.update(window, Wrappers.<Window>lambdaUpdate().eq(Window::getWindowHiddenId, window.getWindowHiddenId()));

			if (result) {
				jsonObject.put("result", "ok");
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/window/update 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
	public Object delete(Long windowId) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			boolean result = windowSerivce.removeById(windowId);
			if (result) {
				jsonObject.put("result", "ok");
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/window/delete 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
	public Object getOne(Long windowId) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			Window window = windowSerivce.getById(windowId);
			if (window != null) {
				jsonObject.put("result", "ok");
				jsonObject.put("window", window);
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/window/getOne 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

}
