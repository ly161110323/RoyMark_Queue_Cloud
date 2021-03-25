package com.roymark.queue.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.QueueSelectarea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author wangfz
 * @since 2020-03-23
 */
@Repository
public interface QueueSelectareaMapper extends BaseMapper<QueueSelectarea> {
        public List<QueueSelectarea> searchSelectAreaList(@Param("selectarea") QueueSelectarea selectarea);

        public IPage<QueueSelectarea> searchSelectAreaList(Page page, @Param("selectarea") QueueSelectarea selectarea);
        }