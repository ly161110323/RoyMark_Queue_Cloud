package com.roymark.queue;


import com.roymark.queue.entity.Window;
import com.roymark.queue.service.WindowService;

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
    private WindowService windowService;
    
    @Test
    public void demo1(){
        List <Window> windows =windowService.list();
        for(Window window : windows)
        {
            System.out.println("window name: "+window.getName());
        }
    System.out.println("****************************************");
    }

}
