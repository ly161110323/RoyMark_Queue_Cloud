package com.roymark.queue.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    public void deletePreHiddenId(Long windowHiddenId) {

        List<Abnomaly> abnomalyList = abnomalyMapper.selectList(Wrappers.<Abnomaly>lambdaQuery().eq(Abnomaly::getWindowHiddenId, windowHiddenId));
        for (Abnomaly abnomaly : abnomalyList) {
            abnomalyMapper.update(null, Wrappers.<Abnomaly>lambdaUpdate().set(Abnomaly::getWindowHiddenId, null)
                    .eq(Abnomaly::getAbnomalyHiddenId, abnomaly.getAbnomalyHiddenId()));
        }

        List<Camera> cameraList = cameraMapper.selectList(Wrappers.<Camera>lambdaQuery().eq(Camera::getWindowHiddenId, windowHiddenId));
        for (Camera camera : cameraList) {
            cameraMapper.update(null, Wrappers.<Camera>lambdaUpdate().set(Camera::getWindowHiddenId, null)
                    .eq(Camera::getCamHiddenId, camera.getCamHiddenId()));
        }

        List<ActionUser> actionUserList = userMapper.selectList(Wrappers.<ActionUser>lambdaQuery().eq(ActionUser::getWindowHiddenId, windowHiddenId));
        for (ActionUser user : actionUserList) {
            userMapper.update(null, Wrappers.<ActionUser>lambdaUpdate().set(ActionUser::getWindowHiddenId, null)
                    .eq(ActionUser::getUserHiddenId, user.getUserHiddenId()));
        }

    }

    @Override
    public Window getWindowByHiddenId(Long windowHiddenId) {
        return windowMapper.getWindowByHiddenId(windowHiddenId);
    }

    @Override
    public IPage<Window> page(IPage<Window> page, Wrapper<Window> queryWrapper) {
        return page.setRecords(this.baseMapper.page(page, queryWrapper));
    }
}
