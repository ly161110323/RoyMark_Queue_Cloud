package com.roymark.queue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.QueueMenuinfo;
import com.roymark.queue.dao.QueueMenuinfoMapper;
import com.roymark.queue.service.QueueMenuinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangfz
 * @since 2020-01-13
 */
@Service
public class QueueMenuinfoServiceImpl extends ServiceImpl<QueueMenuinfoMapper, QueueMenuinfo> implements QueueMenuinfoService {
    @Override
    public List<QueueMenuinfo> searchMenuInfoList(QueryWrapper<QueueMenuinfo> menuinfoQueryWrapper, boolean isSearchChildren) {
        List <QueueMenuinfo> menuList = baseMapper.selectList(menuinfoQueryWrapper);
        if (isSearchChildren) {
            if (menuList != null && menuList.size() > 0) {
                for (QueueMenuinfo menuinfo : menuList) {
                    int haveChildren = this.searchIsHaveChildren(menuinfo.getMenuLs());
                    if (haveChildren > 0) {
                        menuinfo.setIsHaveChildren(1);
                    } else {
                        menuinfo.setIsHaveChildren(0);
                    }
                }
            }
        }
        return menuList;
    }

    @Override
    public int searchIsHaveChildren(String menuLs) {
        QueryWrapper <QueueMenuinfo> menuinfoQueryWrapper = new QueryWrapper <QueueMenuinfo>();
//        select count(1) from queue_MenuInfo where Menu_Up = #value#
//        and Status = '1' and Menu_IsShow='1'
        menuinfoQueryWrapper.eq("Status", "1");
        menuinfoQueryWrapper.eq("Menu_IsShow", "1");
        menuinfoQueryWrapper.eq("Menu_Up", menuLs);
        return baseMapper.selectCount(menuinfoQueryWrapper);

    }

    @Override
    public List<QueueMenuinfo> searchMenuInfoListByAccountLs(String webAccountLs, boolean isSearchChildren)
    {

        List<QueueMenuinfo> list= baseMapper.searchMenuInfoListByAccountLs(webAccountLs);
        if(isSearchChildren){
            if(list!=null && list.size()>0){
                for (QueueMenuinfo queueMenuInfo : list) {
                    int haveChildren = this.searchIsHaveChildren(queueMenuInfo.getMenuLs());
                    if(haveChildren>0){
                        queueMenuInfo.setIsHaveChildren(1);
                    }else{
                        queueMenuInfo.setIsHaveChildren(0);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<QueueMenuinfo> searchMenuInfoListByAccountLsAndAreaLs(String webAccountLs,Integer areaLs, boolean isSearchChildren)
    {

        List<QueueMenuinfo> list= baseMapper.searchMenuInfoListByAccountLsAndAreaLs(webAccountLs,areaLs);
        if(isSearchChildren){
            if(list!=null && list.size()>0){
                for (QueueMenuinfo queueMenuInfo : list) {
                    int haveChildren = this.searchIsHaveChildren(queueMenuInfo.getMenuLs());
                    if(haveChildren>0){
                        queueMenuInfo.setIsHaveChildren(1);
                    }else{
                        queueMenuInfo.setIsHaveChildren(0);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<QueueMenuinfo> searchMenuInfoListByAreaLs(Integer areaLs,boolean isSearchChildren)
    {
        List<QueueMenuinfo> list= baseMapper.searchMenuInfoListByAreaLs(areaLs);
        if(isSearchChildren){
            if(list!=null && list.size()>0){
                for (QueueMenuinfo queueMenuInfo : list) {
                    int haveChildren = this.searchIsHaveChildren(queueMenuInfo.getMenuLs());
                    if(haveChildren>0){
                        queueMenuInfo.setIsHaveChildren(1);
                    }else{
                        queueMenuInfo.setIsHaveChildren(0);
                    }
                }
            }
        }
        return list;
    }
    @Override
    public IPage<QueueMenuinfo> searchPageMenuInfoList(Page page, @Param("menu") QueueMenuinfo menu)
    {
        return baseMapper.searchPageMenuInfoList(page,menu);
    }
}
