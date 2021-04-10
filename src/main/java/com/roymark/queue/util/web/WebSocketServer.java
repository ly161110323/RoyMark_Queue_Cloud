package com.roymark.queue.util.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author liucl
 * @create 2021-01-14 10:41
 */
@Slf4j
@ServerEndpoint(value = "/webSocketService",encoders = {ImageEncoder.class})
public class WebSocketServer{
    // 此处不可使用spring的自动注入功能，原因是spring为防止多线程的安全问题是防注入的
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    // rtsp服务器地址
    private String rtspUrl = "";

    // 用来标志视频抓取器开启与否
    private boolean isStart = false;

    private Thread thread = null;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        Map<String, List<String>> map = session.getRequestParameterMap();
        List<String> list = map.get("video_address");
        if(list != null && list.size() > 0){
            this.rtspUrl = list.get(0);
        }
        log.info("rtsp:"+rtspUrl);
        log.info("用户连接");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "用户连接上了");
        jsonObject.put("code", 0);
        sendMessageByStr(JSON.toJSONString(jsonObject));

        CompletableFuture.supplyAsync(()->{
            this.thread = Thread.currentThread();
            return this;
        }, EXECUTOR_SERVICE).thenAccept(server->{
            server.live();
        });
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(this.thread != null){
            this.thread.interrupt();
            log.info("一个连接已关闭！");
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息,必须是json串
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息,报文:" + message);
        //可以群发消息
        //消息保存到数据库、redis
        /*if (StringUtils.isNotBlank(message)) {
            //解析发送的报文
            JSONObject jsonObject = JSON.parseObject(message);
            //追加发送人(防止串改)
            if (jsonObject != null) {
                jsonObject.put("fromUserId", this.userId);
                String toUserId = jsonObject.getString("toUserId");
                //传送给对应toUserId用户的websocket
                if (StringUtils.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)) {
                    webSocketMap.get(toUserId).sendMessageByStr(jsonObject.toJSONString());
                } else {
                    log.error("请求的userId:" + toUserId + "不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            }
        }*/
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("错误,原因:" + error.getMessage());
        log.error("websocket error: ", error);
    }

    public void sendMessageByStr(String message) {
        if (StringUtils.isNotBlank(message)) {
            try {
                if (this.session.isOpen()) {
                    this.session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                log.error("发送信息失败 ，信息是：" + message);
                log.error("websocket send str msg exception: ", e);
            }
        }
    }

    public void sendMessageByObject(Object message) {
        if (message != null) {
            try {
                this.session.getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException e) {
                log.error("发送信息失败 ，信息是：" + message);
                log.error("websocket send object msg exception: ", e);
            }
        }
    }

    public void sendBinary(ByteBuffer message) {
        if (message != null) {
            try {
                this.session.getBasicRemote().sendBinary(message);
            } catch (IOException e) {
                log.error("发送信息失败 ，信息是：" + message);
                log.error("websocket send byteBuffer msg exception: ", e);
            }
        }
    }


    /**
     * 开启获取rtsp流，通过websocket传输数据
     */
    public void live() {
        log.info("连接rtsp："+rtspUrl+",开始创建grabber");
        FFmpegFrameGrabber grabber = createGrabber(rtspUrl);
        if (grabber != null) {
            log.info("创建grabber成功");
        } else {
            log.info("创建grabber失败");
        }
        startCameraPush(grabber);
    }

    /**
     * 构造视频抓取器
     *
     * @param rtsp 拉流地址
     * @return
     */
    public FFmpegFrameGrabber createGrabber(String rtsp) {
        // 获取视频源
        try {
            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rtsp);
            grabber.setOption("rtsp_transport", "tcp");
            //设置帧率
            grabber.setFrameRate(25);
            //设置获取的视频宽度
            grabber.setImageWidth(960);
            //设置获取的视频高度
            grabber.setImageHeight(540);
            //设置视频bit率
            grabber.setVideoBitrate(3000000);
            return grabber;
        } catch (FrameGrabber.Exception e) {
            log.error("创建解析rtsp FFmpegFrameGrabber 失败");
            log.error("create rtsp FFmpegFrameGrabber exception: ", e);
            return null;
        }
    }

    /**
     * 推送图片（摄像机直播）
     */
    public void startCameraPush(FFmpegFrameGrabber grabber) {
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        while (!Thread.currentThread().isInterrupted()) {
            if (grabber == null) {
                log.info("重试连接rtsp："+rtspUrl+",开始创建grabber");
                grabber = createGrabber(rtspUrl);
                log.info("创建grabber成功");
            }
            try {
                if (grabber != null && !this.isStart) {
                    grabber.start();
                    this.isStart = true;
                    log.info("启动grabber成功");
                }
                if (grabber != null){
                    Frame frame = grabber.grabImage();
                    if (null == frame) {
                        continue;
                    }
                    BufferedImage bufferedImage = java2DFrameConverter.getBufferedImage(frame);
                    byte[] bytes = imageToBytes(bufferedImage, "jpg");
                    //使用websocket发送视频帧数据
                    sendMessageByObject(new Image(bytes));
                }
            } catch (FrameGrabber.Exception | RuntimeException e) {
                log.error("因为异常，grabber关闭，rtsp连接断开，尝试重新连接");
                log.error("exception : " , e);
                if (grabber != null) {
                    try {
                        grabber.stop();
                    } catch (FrameGrabber.Exception ex) {
                        log.error("grabber stop exception: ", ex);
                    } finally {
                        grabber = null;
                        this.isStart = false;
                    }
                }
                break;
            } catch (Exception e){
                log.error("当前websocket连接已关闭", e);
                break;
            }
        }
    }

    /**
     * 图片转字节数组
     *
     * @param bImage 图片数据
     * @param format 格式
     * @return 图片字节码
     */
    private byte[] imageToBytes(BufferedImage bImage, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, format, out);
        } catch (IOException e) {
            log.error("bufferImage 转 byte 数组异常");
            log.error("bufferImage transfer byte[] exception: ", e);
            return null;
        }
        return out.toByteArray();
    }


}