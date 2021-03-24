package com.roymark.queue.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.ParameterMapper;
import com.roymark.queue.entity.Parameter;
import com.roymark.queue.service.ParameterService;

@Service
public class ParameterServiceImpl extends ServiceImpl<ParameterMapper, Parameter> implements ParameterService {

}
