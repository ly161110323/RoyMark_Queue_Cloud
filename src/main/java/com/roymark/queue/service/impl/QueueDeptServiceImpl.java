package com.roymark.queue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.roymark.queue.dao.*;
import com.roymark.queue.entity.*;
import com.roymark.queue.service.QueueDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangfz
 * @since 2020-02-06
 */
@Service
public class QueueDeptServiceImpl extends ServiceImpl<QueueDeptMapper, QueueDept> implements QueueDeptService {
    private static final Logger logger = LogManager.getLogger(QueueDeptServiceImpl.class);
    @Autowired
    private QueueDeptMapper queueDeptMapper;

    @Override
    @Transactional
    public int deleteDeptAssociateTables(String value) {
        int result = 1;
        List <String> list = new ArrayList <String>();
        String[] strings = value.split(",");
        for (int i = 0; i < strings.length; i++) {
            list.add(strings[i]);
        }
        //部分关联表没有deptLs字段，要通过关联关系改变为删除状态
//        if (list != null && list.size() > 0) {
//            for (String deptls : list) {
//
//                QueryWrapper <QueueMatter> matterQueryWrapper = new QueryWrapper <QueueMatter>();
//                matterQueryWrapper.eq("Status", "1");
//                matterQueryWrapper.eq("Dept_Ls", Integer.parseInt(deptls));
//
//            }
//        }
        return result;
    }

}
