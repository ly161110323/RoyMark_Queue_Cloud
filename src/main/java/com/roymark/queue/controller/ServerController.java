package com.roymark.queue.controller;

import java.util.List;

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
	
	@RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
	public Object getAllServers() {
		JSONObject jsonObject = new JSONObject();
	
		try {
			List<Server> servers = serverSerivce.list();
			jsonObject.put("servers", servers);
			jsonObject.put("result", "ok");
			return jsonObject;
		} catch (Exception e) {
			logger.error("/server/getAllServers 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
	public Object insert(Server server) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			boolean result = serverSerivce.save(server);
			if (result) {
				jsonObject.put("result", "ok");
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/server/insert 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
	public Object update(Server server) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			boolean result = serverSerivce.update(server, null);
			if (result) {
				jsonObject.put("result", "ok");
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/server/update 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
	public Object delete(Long serverId) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			boolean result = serverSerivce.removeById(serverId);
			if (result) {
				jsonObject.put("result", "ok");
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/server/delete 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
	public Object getOne(Long serverId) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			Server server = serverSerivce.getById(serverId);
			if (server != null) {
				jsonObject.put("result", "ok");
				jsonObject.put("server", server);
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/server/getOne 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

}
