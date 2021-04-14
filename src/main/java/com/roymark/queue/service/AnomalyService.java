package com.roymark.queue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.roymark.queue.entity.Anomaly;
import java.util.List;

public interface AnomalyService extends IService<Anomaly> {

    List<Anomaly> getAllAnomalies();
    Anomaly getByHiddenId(Long hiddenId);
}