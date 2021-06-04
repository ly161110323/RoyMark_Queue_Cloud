package com.roymark.queue.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.MapMapper;
import com.roymark.queue.entity.Map;
import com.roymark.queue.service.MapService;

@Service
public class MapServiceImpl extends ServiceImpl<MapMapper, Map> implements MapService {

}
