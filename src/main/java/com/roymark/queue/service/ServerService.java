package com.roymark.queue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.roymark.queue.entity.Server;

import java.util.List;

public interface ServerService  extends IService<Server> {

    void setServersStatus(List<Server> servers, int timeout);
}
