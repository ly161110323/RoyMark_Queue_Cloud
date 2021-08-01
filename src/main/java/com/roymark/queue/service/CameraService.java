package com.roymark.queue.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.roymark.queue.entity.Camera;

public interface CameraService  extends IService<Camera> {
    List<Camera> getAllCamera();
    Camera getCameraByHiddenId(Long camHiddenId);
    void setCamsStatus(List<Camera> camera, int timeout);
}
