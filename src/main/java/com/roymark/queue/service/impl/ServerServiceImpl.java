package com.roymark.queue.service.impl;

import com.roymark.queue.util.web.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.ServerMapper;
import com.roymark.queue.entity.Server;
import com.roymark.queue.service.ServerService;

import java.util.HashMap;

@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, Server> implements ServerService {

    private static final Logger logger = LogManager.getLogger(ServerServiceImpl.class);

    @Autowired
    private ServerMapper serverMapper;

    @Override
    public Boolean getServerOnStatus(Long serverHiddenId) {
        try {
            Server server = serverMapper.selectById(serverHiddenId);
            String host = "http://" + server.getServerIp() + ":" + server.getServerPort();
            String path = "/status";
            boolean reachable = HttpUtils.isReachable(server.getServerIp(), String.valueOf(server.getServerPort()), 500);
            if (!reachable) {
                return false;
            }
            HttpResponse response = HttpUtils.doGet(host, path, "get", new HashMap<>(), null);
            if(response.getStatusLine().getStatusCode() == 200) {
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("ServerService.getServerOnStatus Exception");
            logger.error(e.getMessage());
            return false;
        }
    }
}
