package com.roymark.queue.service.impl;

import com.roymark.queue.common.GlobalConstant;
import com.roymark.queue.dao.QueueWebaccountMapper;
import com.roymark.queue.entity.QueueUserinfo;
import com.roymark.queue.dao.QueueUserinfoMapper;
import com.roymark.queue.entity.QueueWebaccount;
import com.roymark.queue.entity.User;
import com.roymark.queue.service.QueueUserinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.util.GetDataLs;
import com.roymark.queue.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangfz
 * @since 2020-01-13
 */
@Service
public class QueueUserinfoServiceImpl extends ServiceImpl<QueueUserinfoMapper, QueueUserinfo> implements QueueUserinfoService {
    @Autowired
    private QueueUserinfoMapper queueUserinfoMapper;
    @Autowired
    private QueueWebaccountMapper queueWebaccountMapper;

    @Transactional
    @Override
    public int createwebaccount(String deleteLss) throws Exception {
        int result = 0;
        // 获取操作用户的用户名
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGIN_USER");
        String updateUser = user.getUserID();
        //进行修改操作

        String delLsStr = "";
        delLsStr = deleteLss.replaceAll("\\(", "");
        delLsStr = delLsStr.replaceAll("\\)", "");
        delLsStr = delLsStr.replaceAll("\"", "");
        String[] delLsArr = delLsStr.split(",");
//            int result =0;
//            int total=delLsArr.length;
        for (int i = 0; i < delLsArr.length; i++) {

            String primaryKeyLs = delLsArr[i];
            Integer intKey = Integer.valueOf(primaryKeyLs);
            QueueUserinfo tempEntity = queueUserinfoMapper.searchUserInfoById(intKey);
            QueueWebaccount accountEntity = new QueueWebaccount();
            //云平台的webaccount主键用uuid生成;
            accountEntity.setWebaccountLs(GetDataLs.getLs());
            accountEntity.setAreaLs(tempEntity.getAreaLs());
            String deptName = tempEntity.getDeptName() == null ? "" : tempEntity.getDeptName();
            String UserInfoName = tempEntity.getUserinfoName() == null ? "" : tempEntity.getUserinfoName();
            accountEntity.setWebaccountName(deptName + ">" + UserInfoName);
            accountEntity.setWebaccountCode(tempEntity.getUserinfoCode());
            accountEntity.setWebaccountLogin(tempEntity.getUserinfoCode());
            accountEntity.setUserinfoLs(tempEntity.getUserinfoLs());
            accountEntity.setWebaccountImage(tempEntity.getUserinfoImagepath());
            //对密码Md5加密
            accountEntity.setWebaccountPassword(Md5Util.EncoderByMd5(GlobalConstant.DEFAULT_PASSWORD));
            accountEntity.setCreateUser(updateUser);
            accountEntity.setCreateTime(LocalDateTime.now());
            accountEntity.setStatus("1");
            queueWebaccountMapper.insert(accountEntity);
        }
        result = 1;

        return result;

    }
}
