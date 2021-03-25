package com.roymark.queue.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.UserMapper;
import com.roymark.queue.entity.ActionUser;
import com.roymark.queue.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, ActionUser> implements UserService {

}
