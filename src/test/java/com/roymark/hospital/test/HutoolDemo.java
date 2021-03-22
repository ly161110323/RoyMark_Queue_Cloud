package com.roymark.hospital.test;
import cn.hutool.core.date.DateUtil;
import org.junit.Test;


import java.util.Date;


/**
 * <p>
 * </p>
 *
 * @author yuxiaobin
 * @date 2018/5/3
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring/spring.xml"})
public class HutoolDemo {


//    @Autowired
//    private HospitalqueueMenuinfoMapper hospitalqueueMenuinfoMapper;
//    @Autowired
//    private HospitalqueueMenuinfoService hospitalqueueMenuinfoService;
    @Test
    public void demo1(){
      Date dt=new Date();
    String format = DateUtil.format(dt, "yyyyMMddHHmmssSSS");
    System.out.println("format:"+format);
    }

}
