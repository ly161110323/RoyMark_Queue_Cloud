package com.roymark.queue.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.roymark.queue.util.web.HttpUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.CameraMapper;
import com.roymark.queue.entity.Camera;
import com.roymark.queue.service.CameraService;

import java.util.List;

@Service("cameraService")
public class CameraServiceImpl extends ServiceImpl<CameraMapper, Camera> implements CameraService  {
    private static final Logger logger = LogManager.getLogger(CameraServiceImpl.class);

    @Autowired
    private CameraMapper cameraMapper;

    @Override
    public List<Camera> getAllCamera() {
        return cameraMapper.getAllCamera();
    }

    @Override
    public Camera getCameraByHiddenId(Long camHiddenId) {
        return cameraMapper.getCameraByHiddenId(camHiddenId);
    }

    @Override
    public Boolean getCamStatus(Camera camera) {
        try {
            return HttpUtils.isHostReachable(camera.getCamIp(), 500);
        } catch (Exception e) {
            logger.error("CameraService.getCamStatus Exception");
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public IPage<Camera> page(IPage<Camera> page, Wrapper<Camera> queryWrapper) {
        return page.setRecords(this.baseMapper.page(page, queryWrapper));
    }
}
