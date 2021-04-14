package com.roymark.queue.controller;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.Camera;
import com.roymark.queue.service.CameraService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roymark.queue.entity.Server;
import com.roymark.queue.service.ServerService;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/server")
public class ServerController {
	private static final Logger logger = LogManager.getLogger(ServerController.class);
    
	@Autowired
    private ServerService serverService;

	@Autowired
	private CameraService cameraService;

	@RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
	public Object getAllServers() {
		JSONObject jsonObject = new JSONObject();

		try {
			List<Server> servers = serverService.list();
			if (servers.size() <= 0) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "暂无服务器");
				jsonObject.put("servers", servers);
				return jsonObject;
			}
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
			server.setServerHiddenId(Long.valueOf(0));
			Server queryServer = serverService.getOne(Wrappers.<Server>lambdaQuery().eq(Server::getServerId, server.getServerId()));
			if (queryServer != null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "服务器ID已存在");
				return jsonObject;
			}
			boolean result = serverService.save(server);
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
			Server queryServer = serverService.getById(server.getServerHiddenId());
			if (queryServer == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "服务器不存在");
				return jsonObject;
			}

			queryServer = serverService.getOne(Wrappers.<Server>lambdaQuery().eq(Server::getServerId, server.getServerId()));

			// 如果根据服务器名查询到的非空且其hiddenId与传入的hiddenId不一致，则表明服务器名已存在于另一项
			if (queryServer != null && !queryServer.getServerHiddenId().equals(server.getServerHiddenId())) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "服务器名已存在");
				return jsonObject;
			}

			boolean result = serverService.update(server, Wrappers.<Server>lambdaUpdate().eq(Server::getServerHiddenId, server.getServerHiddenId()));
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
				// 将对应Camera的serverHiddenId置空
				List<Camera> cameras = cameraService.list(Wrappers.<Camera>lambdaQuery().eq(Camera::getServerHiddenId, Long.valueOf(deletes[i])));
				for (Camera camera : cameras) {
					cameraService.update(null, Wrappers.<Camera>lambdaUpdate().set(Camera::getServerHiddenId, null)
							.eq(Camera::getCamHiddenId, camera.getCamHiddenId()));
				}
				serverService.removeById(deletes[i]);
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
			Server server = serverService.getById(serverHiddenId);
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

	@RequestMapping(value = "/queryData", produces = "application/json;charset=utf-8")
	public Object search(@RequestParam(required = false) String serverId, @RequestParam(required = false) String serverName, int pageNo, int pageSize) {
		JSONObject jsonObject = new JSONObject();

		try {
			// 分页构造器
			Page<Server> page = new Page<Server>(pageNo, pageSize);
			QueryWrapper<Server> queryWrapper = new QueryWrapper<Server>();
			if (serverId != null)
				queryWrapper.like ("server_id",serverId);
			if (serverName != null)
				queryWrapper.like("server_name", serverName);
			// 执行分页
			IPage<Server> pageList = serverService.page(page, queryWrapper);
			// 返回结果
			if (pageList.getTotal() <= 0) {
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
			logger.error("/server/queryData 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "搜索出现错误");
			return jsonObject;
		}
	}

}
