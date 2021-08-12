package com.roymark.queue.task;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.roymark.queue.entity.Anomaly;
import com.roymark.queue.service.AnomalyService;
import com.roymark.queue.util.AnomalyDateControlUtil;
import com.roymark.queue.util.web.HttpUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@EnableScheduling
public class BRTask {
    private final Logger logger = LogManager.getLogger(BRTask .class);

    @Autowired
    private AnomalyService anomalyService;

    // 20秒执行一次
    @Scheduled(cron = "0/30 * * * * ?")
    public void anomalyDateControlTask() {
        try {
            // System.out.println("定时任务");
            Date date = new Date();
            Map<Long, Date> anomalyControlMap = AnomalyDateControlUtil.getAnomalyControlMap();
            Map<Long, Date> idAndDateMap = new HashMap<>();
            // 将接收时间后1分钟未收到消息的，清除
            Iterator<Map.Entry<Long, Date>> iterator = anomalyControlMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, Date> entry = iterator.next();
                if (date.getTime() - entry.getValue().getTime() > 1000 * 60) {
                    idAndDateMap.put(entry.getKey(), entry.getValue());
                    iterator.remove();
                }
            }
            // 将获取到的异常id和最近接收时间的映射更新进数据库

            for (Map.Entry<Long, Date> entry : idAndDateMap.entrySet()) {
                LambdaUpdateWrapper<Anomaly> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(Anomaly::getAnomalyHiddenId, entry.getKey())
                        .set(Anomaly::getAnomalyEndDate, entry.getValue())
                        .set(Anomaly::getAnomalyEndDateValid, false);
                anomalyService.update(null, lambdaUpdateWrapper);
            }

        } catch (Exception e) {
            logger.error("BRTask exception", e);
        }

        // Socket清除任务
        Date date = new Date();
        Iterator<Map.Entry<String, Date>> iterator = HttpUtils.getValidSocketMap().entrySet().iterator();
        while (iterator.hasNext()) {
            Date socketConnectDate = iterator.next().getValue();
            if (date.getTime() - socketConnectDate.getTime() > 60000) {
                iterator.remove();
            }
        }
    }
}
