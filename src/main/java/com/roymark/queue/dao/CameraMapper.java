package com.roymark.queue.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.roymark.queue.entity.Camera;

@Repository
public interface CameraMapper extends BaseMapper<Camera> {

    List<Camera> getAllCamera();

    Camera getCameraByHiddenId(Long camHiddenId);

    List<Camera> page(@Param("page")IPage<Camera> page, @Param(Constants.WRAPPER)Wrapper<Camera> queryWrapper);
}
