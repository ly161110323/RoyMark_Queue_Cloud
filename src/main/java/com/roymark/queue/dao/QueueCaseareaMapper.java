package com.roymark.queue.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.QueueCasearea;
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
public interface QueueCaseareaMapper extends BaseMapper <QueueCasearea> {
    public List <QueueCasearea> searchCaseAreaList(@Param("casearea") QueueCasearea casearea);

    public IPage <QueueCasearea> searchCaseAreaList(Page page, @Param("casearea") QueueCasearea casearea);
}