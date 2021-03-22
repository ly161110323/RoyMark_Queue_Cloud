package com.roymark.queue;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.roymark.queue.dao.QueueAreaMapper;
import com.roymark.queue.entity.QueueArea;
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
public class QueueAreaDemo {

    @Autowired
    private QueueAreaMapper queueAreaMapper;

    @Test
    public void demo1(){
//        SysUser tempUser=new SysUser();
//        tempUser.setId(1L);
//        tempUser.setName("Marry");
//        tempUser.setType(1);
//        tempUser.setAge(18);
//        tempUser.setCtime(LocalDateTime.now());
//        sysUserMapper.insert(tempUser);
//        SysUser tempUser2 = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id",1));
//        tempUser2.setName("Marry update");
//        sysUserMapper.updateById(tempUser2);
//        sysUserMapper.deleteById(1);
        QueueArea areaEntity=new QueueArea();
        areaEntity.setAreaName("双龙街");
        areaEntity.setStatus("1");
        areaEntity.setAreaOrderno(1);
        areaEntity.setParentAreaLs(1);
        areaEntity.setDbconnect("双龙街数据库连接字符串");
        queueAreaMapper.insert(areaEntity);
        Page<QueueArea> page = new Page<QueueArea>(1,2);
        //条件构造器
        QueryWrapper<QueueArea> queryWrapperw = new QueryWrapper<QueueArea>();
        //执行分页
        IPage<QueueArea> pageList = queueAreaMapper.selectPage(page, queryWrapperw);
       List<QueueArea> userList=pageList.getRecords();
        //返回结果
    for(QueueArea area:userList)
    {
        System.out.println("user name:"+area.getAreaName());
    }


    System.out.println("****************************************");
    }
}
