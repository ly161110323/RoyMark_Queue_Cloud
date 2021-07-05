package com.roymark.queue.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.*;
import com.roymark.queue.service.CameraService;
import com.roymark.queue.service.ParameterService;
import com.roymark.queue.service.WindowService;
import com.roymark.queue.util.web.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roymark.queue.service.ServerService;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/server")
public class ServerController {
	private static final Logger logger = LogManager.getLogger(ServerController.class);
    
	@Autowired
    private ServerService serverService;

	@Autowired
	private CameraService cameraService;

	@Autowired
	private ParameterService parameterService;

	@Autowired
	private WindowService windowService;

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
			for (int i=0; i<deletes.length; i++) {
				Server server = serverService.getById(Long.valueOf(deletes[i]));
				if (server == null) {
					jsonObject.put("result", "error");
					jsonObject.put("msg", "数据不存在");
					return jsonObject;
				}
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

			queryWrapper.orderByAsc("server_id");
			// 执行分页
			IPage<Server> pageList = serverService.page(page, queryWrapper);

			// 返回结果
			if (pageList.getRecords().size() <= 0) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "搜素结果为空");
				jsonObject.put("pageList", pageList);
				return jsonObject;
			}
			StringBuilder msg = new StringBuilder();
			for (Server server: pageList.getRecords()) {
				String ip_address = server.getServerIp();
				Long port = server.getServerPort();
				if(ip_address==null || !ip_address.matches("^((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))$")){
					server.setServerStatus("离线");
					server.setProgramStatus("无");
					msg.append(server.getServerName()+ "服务器ip地址不正确;\n");
				}
				else if (port == null || port <= 0 || port > 65535) {
					server.setServerStatus("离线");
					server.setProgramStatus("无");
					msg.append(server.getServerName() + "服务器端口不正确;\n");
				}
				else {
					// 判断服务器是否可用
					String host = "http://" + ip_address + ":" + port;
					String path = "/status";
					try {
						boolean reachable = HttpUtils.isReachable(host, 500);
						if (!reachable){
							server.setServerStatus("离线");
							server.setProgramStatus("无");
						}
						HttpResponse response = HttpUtils.doGet(host, path, "get", new HashMap<>(), null);
						if(response.getStatusLine().getStatusCode() == 200){
							if("on".equals(EntityUtils.toString(response.getEntity(),"UTF-8"))){
								server.setServerStatus("在线");
								server.setProgramStatus("运行中");
							}else {
								server.setServerStatus("在线");
								server.setProgramStatus("待机");
							}
						}else {
							server.setServerStatus("离线");
							server.setProgramStatus("无");
						}
					} catch (IOException e) {			// 连接异常
						server.setServerStatus("离线");
						server.setProgramStatus("无");
					}

				}

			}
			if (msg.equals("")){
				msg.append("搜索成功");
			}
			jsonObject.put("pageList", pageList);
			jsonObject.put("result", "ok");
			jsonObject.put("msg", msg.toString());
			return jsonObject;

		} catch (Exception e) {
			logger.error("/server/queryData 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "搜索出现错误");
			return jsonObject;
		}
	}

	// 批量启动图像服务器的方法
	@RequestMapping(value = "/batchStartServers",produces = "application/json;charset=utf-8")
	public Object batchStartServers(String startId){
		JSONObject jsonObject = new JSONObject();
		StringBuilder msg = new StringBuilder();
		// 查询启动服务器的参数
		String[] startIdList = startId.split(",");
		if (startIdList.length <= 0) {
			jsonObject.put("result", "no");
			jsonObject.put("msg", "没有选中的删除项");
			return jsonObject;
		}

		// 设置参数
		List<Parameter> parameters = parameterService.list();
		JSONObject params = new JSONObject();
		for (Parameter parameter : parameters) {
			params.put(parameter.getParamName(), parameter.getParamValue());
		}

		for (int i=0; i<startIdList.length; i++) {
			Long serverHiddenId = Long.valueOf(startIdList[i]);
			Server startServer = serverService.getById(serverHiddenId);
			if (startServer != null) {
				String ip_address = startServer.getServerIp();
				Long port = startServer.getServerPort();
				String serverName = startServer.getServerName();
				if(ip_address==null || !ip_address.matches("^((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))$")){
					msg.append(serverName+ "启动失败，服务器ip地址不正确;\n");
				}
				else if (port == null || port <= 0 || port > 65535) {
					msg.append(serverName + "启动失败，服务器端口不正确;\n");
				}
				else {
					// 处理发送地址
					String host = "http://"+ ip_address+":"+ port;// 请求域名或ip
					String path = "/start";// 请求路径

					try {
						boolean reachable = HttpUtils.isReachable(host, 500);
						if (!reachable) {
							msg.append(serverName + "启动失败,服务器不可用;\n");
						}else {

							// 获取当前服务器对应的摄像头以及窗口
							List<Camera> cameras = cameraService.list(Wrappers.<Camera>lambdaQuery().eq(Camera::getServerHiddenId, serverHiddenId));

							List<CamAndWinInfo> camAndWinInfos = new ArrayList<>();
							for (Camera camera : cameras) {
								// 异常的摄像头不处理
//								boolean result = HttpUtils.isHostReachable(camera.getCamIp(), 500);
//								if (!result) {
//									continue;
//								}
								CamAndWinInfo temp = new CamAndWinInfo();
								List<Window> windows = windowService.list(Wrappers.<Window>lambdaQuery().eq(Window::getCamHiddenId, camera.getCamHiddenId()));
								// 移除未开启行为分析的
								windows.removeIf(window -> !window.getWindowActionAnalysis());
								if (windows.size() > 0) {
									temp.setCamera(camera);
									temp.setWindows(windows);
									camAndWinInfos.add(temp);
								}
							}
							JSONObject requestData = new JSONObject();
							requestData.put("params", params);
							requestData.put("camAndWin", camAndWinInfos);
							HashMap<String, String> header = new HashMap<>();
							header.put("Content-Type", "application/json");// 设置请求头信息
							String body = JSONObject.toJSONString(requestData);// 设置请求体信息

							HttpResponse response = HttpUtils.doPost(host, path, "post", header, null, body);
							if(response.getStatusLine().getStatusCode() == 200){
								msg.append(serverName+"启动成功;\n");
							}else {
								msg.append(serverName+"启动失败,启动响应未正常返回;\n");
							}
						}
					} catch (IOException e) {
						msg.append(serverName+"启动失败,服务器不可用;\n");
						logger.error(e.getMessage());
					} catch (Exception e) {
						logger.error(e.getMessage());
						msg.append(serverName+"启动失败,启动过程出现异常;\n");
						jsonObject.put("result", "error");
						jsonObject.put("msg", msg);
						return jsonObject;
					}
				}
			}
		}
		jsonObject.put("result", "ok");
		jsonObject.put("msg", msg);
		return jsonObject;
	}

	// 停止服务器的方法
	@RequestMapping(value = "/batchStopServers",produces = "application/json;charset=utf-8")
	public Object stopServer(String stopId){
		JSONObject jsonObject = new JSONObject();
		StringBuilder msg = new StringBuilder();
		// 查询启动服务器的参数
		String[] stopIdList = stopId.split(",");
		if (stopIdList.length <= 0) {
			jsonObject.put("result", "no");
			jsonObject.put("msg", "没有选中的删除项");
			return jsonObject;
		}

		for (int i=0; i<stopIdList.length; i++) {
			Server stopServer = serverService.getById(Long.valueOf(stopIdList[i]));
			if (stopServer != null) {
				String ip_address = stopServer.getServerIp();
				Long port = stopServer.getServerPort();
				String serverName = stopServer.getServerName();
				if(ip_address==null || !ip_address.matches("^((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))$")){
					msg.append(serverName+ "停止失败，服务器ip地址不正确;\n");
				}
				else if (port == null || port <= 0 || port > 65535) {
					msg.append(serverName + "停止失败，服务器端口不正确;\n");
				}
				else {
					String host = "http://" + ip_address + ":" + port;
					String path = "/stop";
					try {
						boolean reachable = HttpUtils.isReachable(host, 500);
						if (!reachable) {
							msg.append(serverName + "停止失败,服务器不可用;\n");
						}else {
							HttpResponse response = HttpUtils.doGet(host, path, "get", new HashMap<>(), null);
							if(response.getStatusLine().getStatusCode() == 200){
								msg.append(serverName+"停止成功;\n");
							}else {
								msg.append(serverName+"停止失败,停止响应未正常返回;\n");
							}
						}
					} catch (IOException e) {
						msg.append(serverName+"停止失败,服务器不可用;\n");
						logger.error(e.getMessage());
					} catch (Exception e) {
						logger.error(e.getMessage());
						msg.append(serverName+"停止失败,停止过程出现异常;\n");
						jsonObject.put("result", "error");
						jsonObject.put("msg", msg);
						return jsonObject;
					}
				}
			}
		}
		jsonObject.put("result", "ok");
		jsonObject.put("msg", msg);
		return jsonObject;
	}

}
