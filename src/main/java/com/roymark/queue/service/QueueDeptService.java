package com.roymark.queue.service;

import com.roymark.queue.entity.QueueDept;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangfz
 * @since 2020-02-06
 */
public interface QueueDeptService extends IService<QueueDept> {


    public int deleteDeptAssociateTables(String value);
}
