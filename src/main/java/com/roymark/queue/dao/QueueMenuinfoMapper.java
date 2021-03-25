package com.roymark.queue.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.QueueMenuinfo;
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
 * @since 2020-01-13
 */
@Repository
public interface QueueMenuinfoMapper extends BaseMapper <QueueMenuinfo> {

    public List <QueueMenuinfo> searchMenuInfoListByAccountLs(String webAccountLs);

    public List <QueueMenuinfo> searchMenuInfoListByAccountLsAndAreaLs(String webAccountLs, Integer areaLs);

    public List<QueueMenuinfo> searchMenuInfoListByAreaLs(Integer areaLs);

    public IPage <QueueMenuinfo> searchPageMenuInfoList(Page page, @Param("menu") QueueMenuinfo menu);
}