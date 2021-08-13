package com.roymark.queue.util;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
* 对异常的接收时间和结束时间进行控制，目的在于避免服务器端未发送结束时间时，一段时间后自动填充结束时间
* */

public class AnomalyDateControlUtil {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("10.249.41.66", 555), 3000);
            socket.sendUrgentData(0xFF);
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    // 以AnomalyHiddenId索引快速查找时间
    private static final Map<Long, Date> anomalyControlMap = new HashMap<>();

    public static Map<Long, Date> getAnomalyControlMap() {
        return anomalyControlMap;
    }

    /*
    将新增的处理，超出时间的由BRTask定时处理
    */
    public void deal(Long anomalyHiddenId, Date endDate) {
        Date date = new Date();

        // 如果接收到的异常消息中包含结束时间，则在map中删除
        if (endDate != null) {
            anomalyControlMap.remove(anomalyHiddenId);
        }
        else {
            // 如果存在，则通过put覆盖接受时间,若不存在，则通过put新增
            anomalyControlMap.put(anomalyHiddenId, date);

        }


//        Map<Long, Date> returnMap = new HashMap<>();
//        // 将接收时间后1分钟未收到消息的，清除
//        Iterator<Map.Entry<Long, Date>> iterator = anomalyControlMap.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<Long, Date> entry = iterator.next();
//            if (date.getTime() - entry.getValue().getTime() > 1000 * 60) {
//                returnMap.put(entry.getKey(), entry.getValue());
//                iterator.remove();
//            }
//        }
//        return returnMap;
    }
}
