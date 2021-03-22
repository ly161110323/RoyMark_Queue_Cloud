package com.roymark.queue.service.impl;

import com.roymark.queue.entity.QueueRolepermissions;
import com.roymark.queue.dao.QueueRolepermissionsMapper;
import com.roymark.queue.service.QueueRolepermissionsService;
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
public class QueueRolepermissionsServiceImpl extends ServiceImpl<QueueRolepermissionsMapper, QueueRolepermissions> implements QueueRolepermissionsService {

    @Override
    public int updateRolePermission(QueueRolepermissions rp,String userID) {
        int status=0;
        if (rp.getPermissionsType().equals("case")) {
            //再新增用户角色
            QueueRolepermissions rolepermissions = new QueueRolepermissions();
            rolepermissions.setRolepermissionsLs(rp.getRolepermissionsLs());
            rolepermissions.setAreaLs(rp.getAreaLs());
            rolepermissions.setRoleLs(rp.getRoleLs());
            rolepermissions.setPermissionsType(rp.getPermissionsType());
            rolepermissions.setStatus("1");
            rolepermissions.setCreateUser(userID);
            LocalDateTime createTime = LocalDateTime.now();
            rolepermissions.setCreateTime(createTime);
            status = baseMapper.insert(rolepermissions);
        }else if(rp.getPermissionsType().equals("menu")) {
            //再新增用户角色
            QueueRolepermissions rolepermissions = new QueueRolepermissions();
            rolepermissions.setRolepermissionsLs(rp.getRolepermissionsLs());
            rolepermissions.setMenuLs(rp.getMenuLs());
            rolepermissions.setRoleLs(rp.getRoleLs());
            rolepermissions.setPermissionsType(rp.getPermissionsType());
            rolepermissions.setStatus("1");
            rolepermissions.setCreateUser(userID);
            LocalDateTime createTime = LocalDateTime.now();
            rolepermissions.setCreateTime(createTime);
            status = baseMapper.insert(rolepermissions);
        }
        return status;
    }

}
