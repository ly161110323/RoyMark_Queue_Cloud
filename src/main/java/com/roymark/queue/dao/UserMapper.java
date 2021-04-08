package com.roymark.queue.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.roymark.queue.entity.ActionUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Repository
public interface UserMapper extends BaseMapper<ActionUser> {
    List<ActionUser> getAllUser();
    ActionUser getUserByHiddenId(Long hiddenId);
    List<ActionUser> page(@Param("page")IPage<ActionUser> page, @Param(Constants.WRAPPER)Wrapper<ActionUser> queryWrapper);
}
