package com.roymark.queue.service.impl;

import com.roymark.queue.entity.QueueWebaccountpermissions;
import com.roymark.queue.dao.QueueWebaccountpermissionsMapper;
import com.roymark.queue.service.QueueWebaccountpermissionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangfz
 * @since 2020-01-13
 */
@Service
public class QueueWebaccountpermissionsServiceImpl extends ServiceImpl<QueueWebaccountpermissionsMapper, QueueWebaccountpermissions> implements QueueWebaccountpermissionsService {
    @Override
    public int updateWebAccountPermission(QueueWebaccountpermissions rp, String userID) {
        int status = 0;
        // 新增
        QueueWebaccountpermissions webaccountpermissions = new QueueWebaccountpermissions();
        webaccountpermissions.setWebaccountpermissionsLs(rp.getWebaccountpermissionsLs());
        webaccountpermissions.setWebaccountLs(rp.getWebaccountLs());
        webaccountpermissions.setAreaLs(rp.getAreaLs());
        webaccountpermissions.setRoleLs(rp.getRoleLs());
        webaccountpermissions.setStatus("1");
        webaccountpermissions.setCreateUser(userID);
        LocalDateTime createTime = LocalDateTime.now();
        webaccountpermissions.setCreateTime(createTime);
        status = baseMapper.insert(webaccountpermissions);
        return status;
    }
}
