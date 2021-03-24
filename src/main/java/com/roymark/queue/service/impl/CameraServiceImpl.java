package com.roymark.queue.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.CameraMapper;
import com.roymark.queue.entity.Camera;
import com.roymark.queue.service.CameraService;

@Service("cameraService")
public class CameraServiceImpl extends ServiceImpl<CameraMapper, Camera> implements CameraService  {

}
