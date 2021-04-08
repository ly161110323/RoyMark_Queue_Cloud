package com.roymark.queue.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.roymark.queue.entity.Abnomaly;

@Repository
public interface AbnomalyMapper extends BaseMapper<Abnomaly> {

    List<Abnomaly> getAllAbnomaly();

    Abnomaly getAbnomalyByHiddenId(Long hiddenId);

    List<Abnomaly> page(@Param("page")IPage<Abnomaly> page, @Param(Constants.WRAPPER)Wrapper<Abnomaly> queryWrapper);
}
