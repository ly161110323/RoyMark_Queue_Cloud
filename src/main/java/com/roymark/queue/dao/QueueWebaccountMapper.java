package com.roymark.queue.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.QueueWebaccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author wangfz
 * @since 2020-01-13
 */
@Repository
public interface QueueWebaccountMapper extends BaseMapper <QueueWebaccount> {
    public IPage <QueueWebaccount> searchPageWebaccountInfoList(Page page,
                                                                @Param("webaccount") QueueWebaccount webaccount);

}