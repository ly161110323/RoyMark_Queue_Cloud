package com.roymark.queue.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.roymark.queue.util.CamAndServerUtil.CamStatusThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.CameraMapper;
import com.roymark.queue.entity.Camera;
import com.roymark.queue.service.CameraService;

import java.util.ArrayList;
import java.util.List;

@Service("cameraService")
public class CameraServiceImpl extends ServiceImpl<CameraMapper, Camera> implements CameraService {
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
    public void setCamsStatus(List<Camera> cameras, int timeout) {
        try {
            List<CamStatusThread> camStatusThreads = new ArrayList<>();
            long start = System.currentTimeMillis();
            for (Camera camera : cameras) {
                if (camera == null)
                    continue;
                // 填入默认值
                camera.setCamStatus("异常");
                // 使用Util中线程探测其ip，达到加速探测的目的
                CamStatusThread camStatusThread = new CamStatusThread(camera);
                camStatusThread.start();
                camStatusThreads.add(camStatusThread);// 超时判定
            }
            boolean aliveFlag;
            long end;
            // 当aliveFlag为True且时间差值小于1s时，等待
            do {
                aliveFlag = false;
                for (CamStatusThread camStatusThread : camStatusThreads) {
                    aliveFlag |= camStatusThread.thread.isAlive();
                }
                if (!aliveFlag) {
                    break;
                }
                end = System.currentTimeMillis();
                // 减少CPU压力
                Thread.sleep(100);
            } while (end - start < timeout);
        } catch (Exception e) {
            logger.error("CameraService.getCamStatus Exception");
            logger.error(e.getMessage());
        }
    }

    @Override
    public IPage<Camera> page(IPage<Camera> page, Wrapper<Camera> queryWrapper) {
        return page.setRecords(this.baseMapper.page(page, queryWrapper));
    }
}
