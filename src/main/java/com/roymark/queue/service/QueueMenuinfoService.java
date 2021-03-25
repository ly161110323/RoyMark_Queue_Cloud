package com.roymark.queue.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.QueueMenuinfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangfz
 * @since 2020-01-13
 */
public interface QueueMenuinfoService extends IService<QueueMenuinfo> {
    public List<QueueMenuinfo> searchMenuInfoList(QueryWrapper<QueueMenuinfo> menuinfoQueryWrapper, boolean isSearchChildren);
    public int searchIsHaveChildren(String menuLs);
    public List<QueueMenuinfo> searchMenuInfoListByAccountLs(String webAccountLs, boolean isSearchChildren);

    public List <QueueMenuinfo> searchMenuInfoListByAccountLsAndAreaLs(String webAccountLs, Integer areaLs, boolean isSearchChildren);

    public List<QueueMenuinfo> searchMenuInfoListByAreaLs(Integer areaLs, boolean isSearchChildren);

    IPage<QueueMenuinfo> searchPageMenuInfoList(Page page, @Param("menu") QueueMenuinfo menu);
}
