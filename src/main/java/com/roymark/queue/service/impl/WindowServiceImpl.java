package com.roymark.queue.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.roymark.queue.dao.AbnomalyMapper;
import com.roymark.queue.dao.CameraMapper;
import com.roymark.queue.dao.UserMapper;
import com.roymark.queue.entity.Abnomaly;
import com.roymark.queue.entity.ActionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.WindowMapper;
import com.roymark.queue.entity.Camera;
import com.roymark.queue.entity.Window;
import com.roymark.queue.service.WindowService;

import java.util.List;

@Service
public class WindowServiceImpl extends ServiceImpl<WindowMapper, Window> implements WindowService {

    @Autowired
    private WindowMapper windowMapper;

    @Autowired
    private AbnomalyMapper abnomalyMapper;

    @Autowired
    private CameraMapper cameraMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Window> getAllWindow() {
        return windowMapper.getAllWindow();
    }

    @Override
    public boolean deleteByWindowHiddenId(Long windowHiddenId) {

        String windowId = windowMapper.selectOne(Wrappers.<Window>lambdaQuery().eq(Window::getWindowHiddenId, windowHiddenId)).getWindowId();
        abnomalyMapper.delete(Wrappers.<Abnomaly>lambdaQuery().eq(Abnomaly::getWindowId, windowId));
        cameraMapper.delete(Wrappers.<Camera>lambdaQuery().eq(Camera::getWindowId, windowId));
        userMapper.delete(Wrappers.<ActionUser>lambdaQuery().eq(ActionUser::getWindowId, windowId));
        return windowMapper.deleteById(windowHiddenId)>0;
    }
}
