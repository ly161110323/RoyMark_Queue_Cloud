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
			if (windows.size() <= 0) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "暂无窗口");
				return jsonObject;
			}
			jsonObject.put("windows", windows);
			jsonObject.put("result", "ok");
			jsonObject.put("msg", "获取成功");
			return jsonObject;
		} catch (Exception e) {
			logger.error("/window/getAllWindows 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "获取出现错误");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
	public Object insert(Window window) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			Window queryWindow = windowSerivce.getOne(Wrappers.<Window>lambdaQuery().eq(Window::getWindowId, window.getWindowId()));
			if (queryWindow != null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "窗口ID已存在");
				return jsonObject;
			}
			boolean result = windowSerivce.save(window);
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
			logger.error("/window/insert 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "添加出现错误");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
	public Object update(Window window) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			Window queryWindow = windowSerivce.getById(window.getWindowHiddenId());
			if (queryWindow == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "窗口不存在");
				return jsonObject;
			}
			queryWindow = windowSerivce.getOne(Wrappers.<Window>lambdaQuery().eq(Window::getWindowId, window.getWindowId()));

			// 如果根据服务器名查询到的非空且其hiddenId与传入的hiddenId不一致，则表明服务器名已存在于另一项
			if (queryWindow != null && !queryWindow.getWindowHiddenId().equals(window.getWindowHiddenId())) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "窗口名已存在");
				return jsonObject;
			}

			boolean result = windowSerivce.update(window, Wrappers.<Window>lambdaUpdate().eq(Window::getWindowHiddenId, window.getWindowHiddenId()));

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
			logger.error("/window/update 错误:" + e.getMessage(), e);
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
				windowSerivce.removeById(deletes[i]);
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
	public Object getOne(Long windowHiddenId) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			Window window = windowSerivce.getById(windowHiddenId);
			if (window != null) {
				jsonObject.put("result", "ok");
				jsonObject.put("window", window);
				jsonObject.put("msg", "获取成功");
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "获取失败");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/window/getOne 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "获取出现错误");
			return jsonObject;
		}
	}

}
