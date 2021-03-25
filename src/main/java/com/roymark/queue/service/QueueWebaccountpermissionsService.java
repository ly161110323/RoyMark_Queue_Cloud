package com.roymark.queue.service;

import com.roymark.queue.entity.QueueWebaccountpermissions;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangfz
 * @since 2020-01-13
 */
public interface QueueWebaccountpermissionsService extends IService<QueueWebaccountpermissions> {
    public int updateWebAccountPermission(QueueWebaccountpermissions rp, String userID);
}
