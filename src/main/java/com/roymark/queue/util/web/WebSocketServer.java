package com.roymark.queue.util.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.roymark.queue.util.WaterMarkUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import javax.websocket.server.ServerEndpoint;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.websocket.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author liucl
 * @create 2021-01-14 10:41
 */
@Slf4j
@ServerEndpoint(value = "/webSocketService",encoders = {ImageEncoder.class})
public class WebSocketServer{

    // 最大读取图片线程数量
    public static int maxReadThreadCount = 9;

    // 使用的读取图片线程的次数,以CamId作Key
    public static Map<String, ReadPicThreadInfo> globalReadPicThreadMap = new HashMap<>();

    public static class ReadPicThreadInfo {
        private int useCount;
        private final ReadPicThread readPicThread;
        public ReadPicThreadInfo(ReadPicThread readPicThread, int useCount) {
            this.readPicThread = readPicThread;
            this.useCount = useCount;
        }
        public int getUseCount() {
            return useCount;
        }
        public void setUseCount(int useCount) {
            this.useCount = useCount;
        }
        public ReadPicThread getReadPicThread() {
            return readPicThread;
        }
    }
    // 读取图片线程
    public static class ReadPicThread implements Runnable {
        private Thread t;
        private final String threadName;
        private final int picWidth;
        private final int picHeight;
        private FFmpegFrameGrabber picGrabber;
        private final String picRtspUrl;
        private BufferedImage image;
        public volatile boolean flag = true;

        public String getThreadName() {
            return this.threadName;
        }

        public BufferedImage getImage() {
            return image;
        }

        public void start () {
            log.info("Starting " +  threadName );
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            }
        }

        // 初始化
        public ReadPicThread(String threadName, int picWidth, int picHeight, FFmpegFrameGrabber picGrabber, String picRtspUrl) {
            this.threadName = threadName;
            this.picWidth = picWidth;
            this.picHeight = picHeight;
            this.picGrabber = picGrabber;
            this.picRtspUrl = picRtspUrl;
            this.image = null;
            image = new BufferedImage(picWidth, picHeight, BufferedImage.TYPE_INT_RGB);
        }

        @Override
        public void run() {
            try {
                // 启动，不能使用start而是startUnSafe
                picGrabber.startUnsafe();
            } catch (Exception e) {
                log.error("grabber start exception: ", e);
                return;
            }
            // 获取摄像头图片
            while (flag) {
                Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
                if (picGrabber == null) {
                    log.info("重试连接rtsp："+picRtspUrl+",开始创建grabber");
                    picGrabber = createGrabber(picRtspUrl, picWidth, picHeight);
                    log.info("创建grabber成功");
                }

                if (picGrabber != null) {
                    try {
                        Frame frame = picGrabber.grabImage();
                        if (frame == null) {
                            log.info("frame null");
                            image = null;
                        } else {
                            image = java2DFrameConverter.getBufferedImage(frame);
                        }
                    } catch (Exception e) {
                        // System.out.println(e.getMessage());
                        log.error(e.getMessage());
                    }
                }
            }
            // 资源释放
            if (picGrabber != null) {
                try {
                    picGrabber.stop();
                    picGrabber.close();
                }catch (Exception e) {
                    log.info(e.getMessage());
                }

            }
        }
    }

    // 合成图片并推流
    public static class ProductFinalPicThread implements Runnable {
        private final Session session;

        // rtsp服务器地址
        private List<String> rtspUrls;

        // CameraId集合
        private List<String> camIds;

        private volatile Boolean openFlag;

        // 读取帧的宽度
        private int width;
        // 读取帧的高度
        private int height;

        // 单个框的宽度
        private final int singleWidth;

        // 单个框的高度
        private final int singleHeight;

        // x轴上的图片个数
        private final int xPicNum;

        // y轴上的图片个数
        private final int yPicNum;

        List<FFmpegFrameGrabber> grabbers;

        private final String threadName;

        private Thread t;

        public ProductFinalPicThread(String threadName, Session session) {
            this.threadName = threadName;
            this.openFlag = true;
            avutil.av_log_set_level(avutil.AV_LOG_ERROR);
            grabbers = new ArrayList<>();
            rtspUrls = new ArrayList<>();
            camIds = new ArrayList<>();
            this.session = session;
            Map<String, List<String>> map = session.getRequestParameterMap();
            List<String> rtspList = map.get("video_address");
            List<String> camIdList = map.get("cam_id");

            // 参数预处理
            if (map.containsKey("x")) {
                xPicNum = Integer.parseInt(map.get("x").get(0));
            }
            else {
                xPicNum = 3;
            }
            if (map.containsKey("y")) {
                yPicNum = Integer.parseInt(map.get("y").get(0));
            }
            else {
                yPicNum = 3;
            }
            if (map.containsKey("width")) {
                width = Integer.parseInt(map.get("width").get(0));
            }
            else {
                width = 960;
            }
            if (map.containsKey("height")) {
                height = Integer.parseInt(map.get("height").get(0));
            }
            else {
                height = 540;
            }
            singleWidth = width / xPicNum;
            singleHeight = height /yPicNum;

            // 重构width和height (可能不能除尽)
            width = singleWidth * xPicNum;
            height = singleHeight * yPicNum;

            String rtspUrlsStr = "";
            if(rtspList != null && rtspList.size() > 0){
                rtspUrlsStr = rtspList.get(0);
            }
            String camIdStr = "";
            if (camIdList != null && camIdList.size() > 0) {
                camIdStr = camIdList.get(0);
            }

            rtspUrls = Arrays.asList(rtspUrlsStr.split(","));
            camIds = Arrays.asList(camIdStr.split(","));

            log.info("用户连接");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "用户连接上了");
            jsonObject.put("code", 0);
            sendMessageByStr(this.session, JSON.toJSONString(jsonObject));
        }

        public void start () {
            log.info("Starting " +  threadName );
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            }

        }

        @Override
        public void run() {
            // 由于多线程读取创建grabber并不慢，所以预加载grabbers,复用再释放
            this.grabbers = getGrabberByRtsp(rtspUrls, singleWidth, singleHeight);
            StringBuilder grabberErrorMsg = new StringBuilder();
            StringBuilder exceededMsg = new StringBuilder();

            boolean delayFlag = true;

            // grabber和读取线程加载
            List<ReadPicThread> readPicThreads = new ArrayList<>();
            for (int i=0; i<grabbers.size(); i++) {
                try {
                    // grabber为空，则在当前线程列表里置空
                    if (grabbers.get(i) == null) {
                        grabberErrorMsg.append(camIds.get(i)).append("、");
                        readPicThreads.add(null);
                    }
                    // 超出最大数量则不再能创建新读取图片线程，线程列表置空
                    else if (globalReadPicThreadMap.size() >= maxReadThreadCount) {
                        if (delayFlag) {        // 当超出最大线程数且延迟标志为空，尝试等待1s重试
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                log.info("Thread Sleep Interrupt:", e);
                            }
                            delayFlag = false;
                            i--;
                        }
                        else {
                            exceededMsg.append(camIds.get(i)).append("、");
                            readPicThreads.add(null);
                            grabbers.get(i).release(); // 释放预加载grabber
                        }

                    }
                    else {
                        ReadPicThread readPicThread;
                        String readPicThreadName = "thread"+camIds.get(i);
                        // 尝试从读取图片线程中去寻找线程
                        if (globalReadPicThreadMap.containsKey(readPicThreadName)) {
                            ReadPicThreadInfo readPicThreadInfo = globalReadPicThreadMap.get(readPicThreadName);
                            readPicThread =  readPicThreadInfo.getReadPicThread(); // 从map中复用已存在线程
                            int count = readPicThreadInfo.getUseCount() + 1; // 使用次数加一
                            readPicThreadInfo.setUseCount(count);
                            // 全局读取线程已存在，释放预加载grabber
                            grabbers.get(i).release();
                        }
                        else {
                            // 只加入有效线程
                            readPicThread = new ReadPicThread(readPicThreadName, singleWidth, singleHeight, grabbers.get(i), rtspUrls.get(i));
                            readPicThread.start();
                            // 加入map中，
                            globalReadPicThreadMap.put(readPicThreadName, new ReadPicThreadInfo(readPicThread, 1));
                            readPicThreads.add(readPicThread);
                        }
                    }

                }catch (Exception e) {  // 无法启动grabber时，跳过该线程捕获
                    log.error("rtsp:"+rtspUrls.get(i)+",线程创建失败", e);
                }
            }
            // 最终消息
            StringBuilder finalMsg = new StringBuilder();
            // 删掉最后一个间隔符
            if (grabberErrorMsg.length() > 0) {
                grabberErrorMsg.deleteCharAt(grabberErrorMsg.length()-1);
                finalMsg.append(grabberErrorMsg).append(" 读取失败，请检查RTSP流");
            }
            if (exceededMsg.length() > 0) {
                if (finalMsg.length() > 0) {
                    finalMsg.append(";");
                }
                exceededMsg.deleteCharAt(exceededMsg.length()-1);
                finalMsg.append(exceededMsg).append(" 因超出最大读取数量而不能显示");
            }
            if (finalMsg.length() > 0) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", finalMsg);
                jsonObject.put("code", -1);
                sendMessageByStr(this.session, JSON.toJSONString(jsonObject));
            }


            log.info("推送图片流开始");

            // 时间间隔优化
            long start = 0;
            long end = 100;
            while (this.openFlag) {
                try {
                    long diff = end - start;
                    if (diff < 100) {
                        Thread.sleep(100 - diff);
                    }
                    start = System.currentTimeMillis();
                    BufferedImage returnImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                    int currentIndex = 0; // 当前页的起始点

                    int rtspNum = rtspUrls.size();
                    for (int i = 0; i < yPicNum; i++) {
                        for (int j = 0; j < xPicNum; j++) {
                            if (currentIndex >= readPicThreads.size()) {
                                break;
                            }
                            BufferedImage partImage = null;
                            if (readPicThreads.get(currentIndex) != null) {    // 当rtsp流正确时菜获取
                                partImage = readPicThreads.get(currentIndex).getImage();
                            }
                            if (partImage == null) {
                                partImage = new BufferedImage(singleWidth, singleHeight, BufferedImage.TYPE_INT_RGB);
                            }
                            // 绘制CAM ID
                            WaterMarkUtil.mark(partImage, Color.RED, camIds.get(currentIndex));
                            int[] imageArray = new int[width * height];
                            imageArray = partImage.getRGB(0, 0, singleWidth, singleHeight, imageArray, 0, singleWidth);
                            returnImg.setRGB(j * singleWidth, i * singleHeight, singleWidth, singleHeight, imageArray, 0, singleWidth);
                            currentIndex++;
                        }
                        if (currentIndex >= readPicThreads.size()) {
                            break;
                        }
                    }
                    byte[] bytes = imageToBytes(returnImg, "jpg");
                    sendMessageByObject(this.session, new Image(bytes));
                    end = System.currentTimeMillis();
                } catch (Exception e) {
                    log.error("因为异常，grabber关闭，rtsp连接断开");
                    log.error("exception : " , e);
                    break;
                }
            }

            // 资源释放，先停止图片读取再关闭抓图器，否则触发抓图null错误
            log.info("推流结束");
            for (ReadPicThread readPicThread: readPicThreads) {
                if (readPicThread != null) {
                    String readThreadName = readPicThread.getThreadName();
                    // 从线程资源Map中去寻找
                    if (globalReadPicThreadMap.containsKey(readThreadName)) {
                        // 如果只有一个，则表明只有当前在使用，进行释放
                        ReadPicThreadInfo readPicThreadInfo = globalReadPicThreadMap.get(readThreadName);
                        int useCount = readPicThreadInfo.getUseCount();
                        if (useCount <= 1) {
                            globalReadPicThreadMap.remove(readThreadName);
                            readPicThread.flag = false;
                        }
                        // 否则不进行释放，将使用次数减一
                        else {
                            readPicThreadInfo.setUseCount(useCount-1);
                        }
                    }
                    else {
                        readPicThread.flag = false;
                    }
                }

            }

        }
    }


    private ProductFinalPicThread thread;

    public boolean flag;
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session){
        // 必须新建线程去发送图片，否则无法接收来自客户端的消息
        thread = new ProductFinalPicThread("websocket_thread", session);
        thread.start();
        flag = true;
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        // 终止进程
        log.info("OnClose");
        this.flag = false;
        this.thread.openFlag = false;
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息,必须是json串
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("消息：" + message);
        log.info("用户消息,报文:" + message);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        // onClose();
        log.error("错误,原因:" + error.getMessage());
        log.error("websocket error: ", error);
    }

    public static void sendMessageByStr(Session session, String message) {
        if (StringUtils.isNotBlank(message)) {
            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                // log.error("发送信息失败 ，信息是：" + message);
                log.error("websocket send str msg exception: ", e);
            }
        }
    }

    public static void sendMessageByObject(Session session, Object message) {
        if (message != null) {
            try {
                if (session != null && session.isOpen()) {
                    session.getBasicRemote().sendObject(message);
                }
            } catch (IOException | EncodeException e) {
                // log.error("发送信息失败 ，信息是：" + message);
                log.error("websocket send object msg exception: ", e);
            }
        }
    }

    /**
     * 开启获取rtsp流，通过websocket传输数据
     */
    public static List<FFmpegFrameGrabber> getGrabberByRtsp(List<String> rtspUrls, int singleWidth, int singleHeight) {
        List<FFmpegFrameGrabber> grabbers = new ArrayList<>();
        try {
            // 以线程创建，避免因为探测ip和端口导致开启延时很长
            List<CreateGrabberThread> createGrabberThreads = new ArrayList<>();
            for (String rtspUrl : rtspUrls) {
                log.info("连接rtsp："+rtspUrl+",开始创建grabber");
                CreateGrabberThread createGrabberThread = new CreateGrabberThread(rtspUrl, singleWidth, singleHeight);
                createGrabberThread.start();
                createGrabberThreads.add(createGrabberThread);
            }

            // 等待Grabber线程全部完成
            try {
                for (CreateGrabberThread thread: createGrabberThreads) {
                    thread.thread.join();
                }
            } catch (InterruptedException e) {
                log.error("Thread join error:", e);
            }

            // 获得生成的grabber
            for (int i=0; i<createGrabberThreads.size(); i++) {
                grabbers.add(createGrabberThreads.get(i).getGrabber());
            }

            if (grabbers.size() > 0) {
                log.info("创建grabber成功");
            } else {
                log.info("创建grabber失败");
            }
            return grabbers;
        } catch (Exception e) {
            log.error("websocket getGrabberByRtsp:", e);
            return grabbers;
        }

    }

    /**
     * 构造视频抓取器
     *
     * @param rtsp 拉流地址
     * @return
     */
    static public FFmpegFrameGrabber createGrabber(String rtsp, int width, int height) {
        // 获取视频源
        try {
            // System.out.println(rtsp);
            // 首先对rtsp流格式、ip地址和端口做一个检测，如果有问题，则返回null
            if (rtsp == null || rtsp.equals("")) {
                log.info("0");
                return null;
            }
            String[] strings = rtsp.split("@");
            if (strings.length < 2) {
                log.info("1");
                return null;
            }
            String ipAndPortStr = strings[1].split("/")[0];

            String[] ipAndPort = ipAndPortStr.split(":");
            if (ipAndPort.length < 2) {
                log.info("2");
                return null;
            }
            // socket探测
            if (!HttpUtils.isSocketReachable(ipAndPort[0], ipAndPort[1], 1000)) {
                log.info(rtsp + "Socket连接不可达");
                return null;
            }

            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rtsp);
            grabber.setOption("rtsp_transport", "tcp");
            //设置帧率
            grabber.setFrameRate(25);
            //设置获取的视频宽度
            grabber.setImageWidth(width);
            //设置获取的视频高度
            grabber.setImageHeight(height);
            //设置视频bit率
            grabber.setVideoBitrate(3000000);
            //设置超时
            grabber.setTimeout(5000);
            return grabber;
        } catch (Exception e) {
            log.error("创建解析rtsp FFmpegFrameGrabber 失败");
            log.error("create rtsp FFmpegFrameGrabber exception: ", e);
            return null;
        }
    }

    /**
     * 图片转字节数组
     *
     * @param bImage 图片数据
     * @param format 格式
     * @return 图片字节码
     */
    private static byte[] imageToBytes(BufferedImage bImage, String format) {
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

    public static class CreateGrabberThread implements Runnable {
        private final String rtsp;
        private final int width;
        private final int height;
        private FFmpegFrameGrabber grabber;
        public Thread thread;

        public FFmpegFrameGrabber getGrabber() {
            return this.grabber;
        }

        public CreateGrabberThread(String rtsp, int width, int height) {
            this.rtsp = rtsp;
            this.width = width;
            this.height = height;
            this.grabber = null;
            this.thread = null;
        }
        public void start() {
            if (this.thread == null) {
                this.thread = new Thread(this, this.rtsp);
                this.thread.start();
            }
        }
        @Override
        public void run() {
            this.grabber = createGrabber(rtsp, width, height);
        }
    }

}