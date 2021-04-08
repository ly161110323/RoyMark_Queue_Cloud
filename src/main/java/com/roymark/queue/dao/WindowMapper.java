package com.roymark.queue.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.roymark.queue.entity.Window;

@Repository
public interface WindowMapper extends BaseMapper<Window> {

    List<Window> getAllWindow();
    Window getWindowByHiddenId(Long windowHiddenId);
    List<Window> page(@Param("page")IPage<Window> page, @Param(Constants.WRAPPER)Wrapper<Window> queryWrapper);
}
