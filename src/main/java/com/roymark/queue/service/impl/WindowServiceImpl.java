package com.roymark.queue.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.WindowMapper;
import com.roymark.queue.entity.Window;
import com.roymark.queue.service.WindowService;

@Service
public class WindowServiceImpl extends ServiceImpl<WindowMapper, Window> implements WindowService {

}
