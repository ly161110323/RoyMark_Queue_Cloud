package com.roymark.queue.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.roymark.queue.entity.Anomaly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.AnomalyMapper;
import com.roymark.queue.service.AnomalyService;

@Service
public class AnomalyServiceImpl extends ServiceImpl<AnomalyMapper, Anomaly> implements AnomalyService {

    @Autowired
    private AnomalyMapper anomalyMapper;
    @Override
    public List<Anomaly> getAllAnomalies() {
        return anomalyMapper.getAllAnomaly();
    }

    @Override
    public Anomaly getByHiddenId(Long hiddenId) {
        return anomalyMapper.getAnomalyByHiddenId(hiddenId);
    }


    @Override
    public IPage<Anomaly> page(IPage<Anomaly> page, Wrapper<Anomaly> queryWrapper) {
        return page.setRecords(this.baseMapper.page(page, queryWrapper));
    }
}
