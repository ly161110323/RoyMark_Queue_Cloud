package com.roymark.queue.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.AbnomalyMapper;
import com.roymark.queue.entity.Abnomaly;
import com.roymark.queue.service.AbnomalyService;

@Service
public class AbnomalyServiceImpl extends ServiceImpl<AbnomalyMapper, Abnomaly> implements AbnomalyService {

    @Autowired
    private AbnomalyMapper abnomalyMapper;
    @Override
    public List<Abnomaly> getAllAbnomalies() {
        return abnomalyMapper.getAllAbnomaly();
    }

    @Override
    public List<Abnomaly> getByWindowId(String windowId) {
        return abnomalyMapper.getByWindowId(windowId);
    }

}
