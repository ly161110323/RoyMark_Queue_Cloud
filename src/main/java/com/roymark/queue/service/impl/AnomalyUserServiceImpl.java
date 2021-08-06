package com.roymark.queue.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.AnomalyMapper;
import com.roymark.queue.dao.AnomalyUserMapper;
import com.roymark.queue.dao.UserMapper;
import com.roymark.queue.entity.Anomaly;
import com.roymark.queue.entity.AnomalyUser;
import com.roymark.queue.service.AnomalyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnomalyUserServiceImpl extends ServiceImpl<AnomalyUserMapper, AnomalyUser> implements AnomalyUserService {

    @Autowired
    private AnomalyUserMapper anomalyUserMapper;

    @Autowired
    private AnomalyMapper anomalyMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void checkInsert(AnomalyUser anomalyUser) {
        Long anomalyHiddenId = anomalyUser.getAnomalyHiddenId();
        Long userHiddenId = anomalyUser.getUserHiddenId();
        if (anomalyHiddenId == null || userHiddenId == null) {
            return;
        }
        // 如果异常或用户不存在，不执行
        if (anomalyMapper.selectById(anomalyHiddenId)==null || userMapper.selectById(userHiddenId)==null) {
            return;
        }
        if (anomalyUserMapper.selectOne(Wrappers.<AnomalyUser>lambdaQuery()         // 如果已存在，则不必添加
                .eq(AnomalyUser::getAnomalyHiddenId, anomalyHiddenId)
                .eq(AnomalyUser::getUserHiddenId, userHiddenId)) == null) {

            Anomaly anomaly = anomalyMapper.selectById(anomalyHiddenId);

            anomalyUser.setId((long)0);
            anomalyUserMapper.insert(anomalyUser);

            // 将异常表中的默认用户标志置false
            if (!anomaly.getDefaultUserFlag()) {
                anomaly.setDefaultUserFlag(false);
                anomalyMapper.updateById(anomaly);
            }
        }
    }
}
