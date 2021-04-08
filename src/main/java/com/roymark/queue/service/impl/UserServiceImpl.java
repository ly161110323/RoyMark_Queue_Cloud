package com.roymark.queue.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.UserMapper;
import com.roymark.queue.entity.ActionUser;
import com.roymark.queue.service.UserService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, ActionUser> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<ActionUser> getAllUser() {
        return userMapper.getAllUser();
    }

    @Override
    public ActionUser getUserByHiddenId(Long hiddenId) {
        return userMapper.getUserByHiddenId(hiddenId);
    }

    @Override
    public IPage<ActionUser> page(IPage<ActionUser> page, Wrapper<ActionUser> queryWrapper) {
        return page.setRecords(this.baseMapper.page(page, queryWrapper));
    }
}
