package com.roymark.queue.service.impl;

import com.roymark.queue.util.CamAndServerUtil.ServerStatusThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.ServerMapper;
import com.roymark.queue.entity.Server;
import com.roymark.queue.service.ServerService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, Server> implements ServerService {

    private static final Logger logger = LogManager.getLogger(ServerServiceImpl.class);


    @Override
    public void setServersStatus(List<Server> servers) {
        try {
            List<ServerStatusThread> serverStatusThreads = new ArrayList<>();
            long start = System.currentTimeMillis();
            for (Server server: servers) {
                if (server == null) {
                    continue;
                }
                // 填入默认值
                server.setServerStatus("离线");
                server.setProgramStatus("无");
                // 使用util中的线程来获取状态，加速获取
                ServerStatusThread serverStatusThread = new ServerStatusThread(server);
                serverStatusThread.start();
                serverStatusThreads.add(serverStatusThread);
            }
            boolean aliveFlag;
            long end;
            // 当aliveFlag为True且时间差值小于0.5s时，等待
            do {
                aliveFlag = false;
                for (ServerStatusThread serverStatusThread : serverStatusThreads) {
                    aliveFlag |= serverStatusThread.thread.isAlive();
                }
                if (!aliveFlag) {
                    break;
                }
                end = System.currentTimeMillis();
                // 减少CPU压力
                Thread.sleep(100);
            } while (end - start < 500);
        } catch (Exception e) {
            logger.error("CameraService.getCamStatus Exception");
            logger.error(e.getMessage());
        }
    }
}
