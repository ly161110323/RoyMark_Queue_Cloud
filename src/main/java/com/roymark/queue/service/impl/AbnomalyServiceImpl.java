package com.roymark.queue.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.AbnomalyMapper;
import com.roymark.queue.entity.Abnomaly;
import com.roymark.queue.service.AbnomalyService;

@Service
public class AbnomalyServiceImpl extends ServiceImpl<AbnomalyMapper, Abnomaly> implements AbnomalyService {

}
