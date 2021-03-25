package com.roymark.queue.service;

import com.roymark.queue.entity.QueueUserinfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangfz
 * @since 2020-01-13
 */
public interface QueueUserinfoService extends IService<QueueUserinfo> {
   public int createwebaccount(String deleteLss) throws Exception;
}
