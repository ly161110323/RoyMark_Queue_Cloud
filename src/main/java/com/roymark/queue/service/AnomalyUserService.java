package com.roymark.queue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.roymark.queue.entity.AnomalyUser;

public interface AnomalyUserService extends IService<AnomalyUser> {
    void checkInsert(AnomalyUser anomalyUser);
}
