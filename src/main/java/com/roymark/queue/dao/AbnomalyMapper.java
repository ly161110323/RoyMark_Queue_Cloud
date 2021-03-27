package com.roymark.queue.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.roymark.queue.entity.Abnomaly;

@Repository
public interface AbnomalyMapper extends BaseMapper<Abnomaly> {

    List<Abnomaly> getAllAbnomaly();

    List<Abnomaly> getByWindowId(String windowId);
}
