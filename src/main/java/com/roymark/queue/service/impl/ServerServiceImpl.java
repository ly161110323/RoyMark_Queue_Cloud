package com.roymark.queue.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.ServerMapper;
import com.roymark.queue.entity.Server;
import com.roymark.queue.service.ServerService;

@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, Server> implements ServerService {

}
