package com.roymark.queue.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.FloorMapper;
import com.roymark.queue.entity.Floor;
import com.roymark.queue.service.FloorService;

@Service
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor> implements FloorService {

}
