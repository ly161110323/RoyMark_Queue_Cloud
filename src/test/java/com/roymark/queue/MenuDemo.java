package com.roymark.queue;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.dao.QueueMenuinfoMapper;
import com.roymark.queue.entity.QueueMenuinfo;
import com.roymark.queue.service.QueueMenuinfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yuxiaobin
 * @date 2018/5/3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring.xml"})
public class MenuDemo {


    @Autowired
    private QueueMenuinfoMapper queueMenuinfoMapper;
    @Autowired
    private QueueMenuinfoService queueMenuinfoService;
    @Test
    public void demo1(){
        List <QueueMenuinfo> menuList=queueMenuinfoService.searchMenuInfoListByAccountLsAndAreaLs("7665f65747a44b8a87ded0a729e713d2",9,true);
        for(QueueMenuinfo menu:menuList)
        {
            System.out.println("menuName:"+menu.getMenuName());
        }
    System.out.println("****************************************");
    }
//    @Test
//    public void demo2(){
//        QueryWrapper<HospitalqueueMenuinfo> menuinfoQueryWrapper=new QueryWrapper<HospitalqueueMenuinfo>();
//        menuinfoQueryWrapper.eq("Menu_IsShow","1");
//        menuinfoQueryWrapper.eq("status","1");
//        List<HospitalqueueMenuinfo>     menuList=hospitalqueueMenuinfoMapper.selectList(menuinfoQueryWrapper);
//        for(HospitalqueueMenuinfo menu:menuList)
//        {
//            System.out.println("menuName:"+menu.getMenuName());
//        }
//        System.out.println("****************************************");
//    }

//    @Test
//    public void demo3(){
//        QueryWrapper<HospitalqueueMenuinfo> menuinfoQueryWrapper=new QueryWrapper<HospitalqueueMenuinfo>();
//        menuinfoQueryWrapper.eq("Menu_IsShow","1");
//        menuinfoQueryWrapper.eq("status","1");
//        List<HospitalqueueMenuinfo>     menuList= hospitalqueueMenuinfoService.searchMenuInfoList(menuinfoQueryWrapper,true);
//        for(HospitalqueueMenuinfo menu:menuList)
//        {
//            System.out.println("menuName:"+menu.getMenuName()+"是否有子菜单:"+menu.getIsHaveChildren());
//        }
//        System.out.println("****************************************");
//    }

//    @Test
//    public void demo4(){
//
//        List<HospitalqueueMenuinfo>     menuList= hospitalqueueMenuinfoService.searchMenuInfoListByAccountLs("13",true);
//        for(HospitalqueueMenuinfo menu:menuList)
//        {
//            System.out.println("menuName:"+menu.getMenuName()+"是否有子菜单:"+menu.getIsHaveChildren());
//        }
//        System.out.println("****************************************");
//    }

//    @Test
//    public void demo5(){
//        Page<HospitalqueueMenuinfo> page = new Page<HospitalqueueMenuinfo>(1,2);
//        HospitalqueueMenuinfo menu=new HospitalqueueMenuinfo();
//        menu.setMenuLevel("1");
//
//
//    IPage<HospitalqueueMenuinfo> pageList=hospitalqueueMenuinfoService.searchPageMenuInfoList(page,menu);
//    List<HospitalqueueMenuinfo> menuList=pageList.getRecords();
//        for(HospitalqueueMenuinfo tempMenu:menuList)
//        {
//            System.out.println("menuName:"+tempMenu.getMenuName());
//        }
//        System.out.println("****************************************");
//    }
}
