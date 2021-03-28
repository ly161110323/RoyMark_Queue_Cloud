package com.roymark.queue.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.roymark.queue.entity.Window;

@Repository
public interface WindowMapper extends BaseMapper<Window> {

    List<Window> getAllWindow();
}
