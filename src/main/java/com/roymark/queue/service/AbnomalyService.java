package com.roymark.queue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.roymark.queue.entity.Abnomaly;
import java.util.List;

public interface AbnomalyService extends IService<Abnomaly> {

    List<Abnomaly> getAllAbnomalies();
    Abnomaly getByHiddenId(Long hiddenId);
}