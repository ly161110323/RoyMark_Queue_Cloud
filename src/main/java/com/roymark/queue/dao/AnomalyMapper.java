package com.roymark.queue.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.roymark.queue.entity.Anomaly;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Repository
public interface AnomalyMapper extends BaseMapper<Anomaly> {

    List<Anomaly> getAllAnomaly();

    Anomaly getAnomalyByHiddenId(Long hiddenId);

    List<Anomaly> page(@Param("page")IPage<Anomaly> page, @Param(Constants.WRAPPER)Wrapper<Anomaly> queryWrapper);

    List<Anomaly> list(@Param(Constants.WRAPPER)Wrapper<Anomaly> queryWrapper);
}
