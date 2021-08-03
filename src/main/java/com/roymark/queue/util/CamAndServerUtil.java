package com.roymark.queue.util;

import com.roymark.queue.entity.Camera;
import com.roymark.queue.entity.Server;
import com.roymark.queue.util.web.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;

@Slf4j
public class CamAndServerUtil {

    public static class CamStatusThread implements Runnable {
        private final Camera camera;
        public Thread thread;

        public CamStatusThread(Camera camera) {
            this.camera = camera;
        }

        public void start() {
            if (thread == null) {
                thread = new Thread (this, camera.getCamId());
                thread.start ();
            }
        }
        @Override
        public void run() {
            // 从rtsp流分离出IP和PORT
            String rtsp = camera.getCamVideoAddr();
            String[] strings = rtsp.split("@");
            if (strings.length < 2) {
                return;
            }
            String ipAndPortStr = strings[1].split("/")[0];
            String[] ipAndPort = ipAndPortStr.split(":");
            if (ipAndPort.length < 2) {
                return;
            }
            boolean result = HttpUtils.isSocketReachable(ipAndPort[0], ipAndPort[1], 1000);
            if (result) {
                camera.setCamStatus("正常");
            }
        }
    }

    public static class ServerStatusThread implements Runnable {
        private final Server server;
        public Thread thread;

        public ServerStatusThread(Server server) {
            this.server = server;
        }

        public void start() {
            if (thread == null) {
                thread = new Thread (this, server.getServerId());
                thread.start ();
            }
        }
        @Override
        public void run() {
            try {
                String ip_address = server.getServerIp();
                Long port = server.getServerPort();
                // IP格式匹配
                if (ip_address == null || !ip_address.matches("^((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))$")) {
                // 端口匹配
                } else if (port == null || port <= 0 || port > 65535) {
                } else {
                    // 判断服务器是否可用
                    String host = "http://" + ip_address + ":" + port;
                    String path = "/status";
                    if (HttpUtils.isReachable(ip_address, String.valueOf(port), 1000)) {
                        HttpResponse response = HttpUtils.doGet(host, path, "get", new HashMap<>(), null);
                        if (response.getStatusLine().getStatusCode() == 200) {
                            if ("on".equals(EntityUtils.toString(response.getEntity(), "UTF-8"))) {
                                server.setServerStatus("在线");
                                server.setProgramStatus("运行中");
                            } else {
                                server.setServerStatus("在线");
                                server.setProgramStatus("待机");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // 可忽略异常，表明服务器离线
                log.info(server.getServerId() + "服务器连接失败：", e);
            }
        }
    }
}
