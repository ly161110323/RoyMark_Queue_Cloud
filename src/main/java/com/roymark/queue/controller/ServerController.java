package com.roymark.queue.controller;

import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.roymark.queue.entity.Camera;
import com.roymark.queue.service.CameraService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roymark.queue.entity.Server;
import com.roymark.queue.service.ServerService;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/server")
public class ServerController {
	private static final Logger logger = LogManager.getLogger(ServerController.class);
    
	@Autowired
    private ServerService serverSerivce;

	@Autowired
	private CameraService cameraService;
	
	@RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
	public Object getAllServers() {
		JSONObject jsonObject = new JSONObject();
	
		try {
			List<Server> servers = serverSerivce.list();
			for (Server server: servers) {
				server.setServerStatus("离线");
				server.setProgramStatus("未启动");
			}
			jsonObject.put("servers", servers);
			jsonObject.put("result", "ok");
			jsonObject.put("msg", "获取成功");
			return jsonObject;
		} catch (Exception e) {
			logger.error("/server/getAllServers 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "获取出现错误");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
	public Object insert(Server server) {
		JSONObject jsonObject = new JSONObject();

		try {
			Server queryServer = serverSerivce.getOne(Wrappers.<Server>lambdaQuery().eq(Server::getId, server.getId()));
			if (queryServer != null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "服务器ID重复");
				return jsonObject;
			}
			boolean result = serverSerivce.save(server);
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
			logger.error("/server/insert 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "添加出现错误");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
	public Object update(Server server) {
		JSONObject jsonObject = new JSONObject();

		try {
			Server queryServer = serverSerivce.getById(server.getHiddenId());
			if (queryServer == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "服务器不存在");
				return jsonObject;
			}

			boolean result = serverSerivce.update(server, Wrappers.<Server>lambdaUpdate().eq(Server::getHiddenId, server.getHiddenId()));
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
			logger.error("/server/update 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "修改出现错误");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
	public Object delete(String deleteLss) {
		JSONObject jsonObject = new JSONObject();


		try {
			String[] deletes = deleteLss.split(",");
			if (deletes.length <= 0)
			{
				jsonObject.put("result", "no");
				jsonObject.put("msg", "无选中删除项");
				return jsonObject;
			}
			for (int i = 0; i < deletes.length; i++) {
				cameraService.remove(Wrappers.<Camera>lambdaQuery().eq(Camera::getServerId, Long.valueOf(deletes[i])));
				serverSerivce.removeById(deletes[i]);
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
	public Object getOne(Long serverHiddenId) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			Server server = serverSerivce.getById(serverHiddenId);
			if (server != null) {
				jsonObject.put("result", "ok");
				jsonObject.put("server", server);
				jsonObject.put("msg", "获取成功");
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "获取失败");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/server/getOne 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "获取出现错误");
			return jsonObject;
		}
	}

}
