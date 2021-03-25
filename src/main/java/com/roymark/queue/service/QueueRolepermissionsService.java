package com.roymark.queue.service;

import com.roymark.queue.entity.QueueRolepermissions;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangfz
 * @since 2020-01-13
 */
public interface QueueRolepermissionsService extends IService<QueueRolepermissions> {
    public int updateRolePermission(QueueRolepermissions rolepermissions, String userID);
}
