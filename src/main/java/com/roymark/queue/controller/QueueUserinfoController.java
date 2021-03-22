package com.roymark.queue.controller;
import cn.hutool.core.util.StrUtil;
import com.roymark.queue.common.GlobalConstant;
import com.roymark.queue.dao.QueueWebaccountMapper;
import com.roymark.queue.entity.QueueArea;
import com.roymark.queue.entity.QueueUserinfo;
import com.roymark.queue.dao.QueueUserinfoMapper;
import com.roymark.queue.entity.QueueWebaccount;
import com.roymark.queue.entity.User;
import com.roymark.queue.service.QueueUserinfoService;
import com.roymark.queue.util.*;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 * @author wangfz
 * @since 2020-01-13
 */
@RestController
@RequestMapping("/QueueUserinfo")
public class QueueUserinfoController {



    private static final Logger logger = LogManager.getLogger(QueueUserinfoController.class);
    @Autowired
    private QueueUserinfoMapper queueUserinfoMapper;
    @Autowired
    private QueueUserinfoService queueUserinfoService;


    /**
         * 新增
         */
    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(QueueUserinfo tempQueueUserinfo,@RequestParam(value = "uploadinfo", required = false) MultipartFile uploadinfo) {
        JSONObject jsonObject = new JSONObject();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGIN_USER");
        try {
            String createUser = user.getUserID();
            tempQueueUserinfo.setCreateUser(createUser);
            LocalDateTime createTime = LocalDateTime.now();
            tempQueueUserinfo.setCreateTime(createTime);
            tempQueueUserinfo.setStatus("1");
            String filePath = "";// 头像的路径
            if (uploadinfo != null) {
                // 账号的附件路径参考医院和政务的后台代码
                String areaLs = String.valueOf(tempQueueUserinfo.getAreaLs());
                String uploadPath = "/RemoteQueue/"+areaLs+"/upload/employee/";
                filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
            }
            tempQueueUserinfo.setUserinfoImagepath(filePath);
            int result = queueUserinfoMapper.insert(tempQueueUserinfo);
            if (result > 0) {
                jsonObject.put("result", "ok");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/QueueUserinfo/insert错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(QueueUserinfo tempQueueUserinfo,@RequestParam(value = "uploadinfo", required = false) MultipartFile uploadinfo) {
        JSONObject jsonObject = new JSONObject();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGIN_USER");
        String updateUser = user.getUserID();

        try {
                tempQueueUserinfo.setUpdateUser(updateUser);
                LocalDateTime updateTime = LocalDateTime.now();
                tempQueueUserinfo.setUpdateTime(updateTime);
                tempQueueUserinfo.setStatus("1");
                String filePath = "";// 头像的路径
                if (uploadinfo != null) {
                    // 账号的附件路径参考医院和政务的后台代码
                    String areaLs = String.valueOf(tempQueueUserinfo.getAreaLs());
                    String uploadPath = "/RemoteQueue/"+areaLs+"/upload/employee/";
                    filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
                    tempQueueUserinfo.setUserinfoImagepath(filePath);
                }
                int result = queueUserinfoMapper.updateById(tempQueueUserinfo);
                if (result > 0) {
                    jsonObject.put("result", "ok");
                } else {
                    jsonObject.put("result", "no");
                }

        }
        catch (Exception e) {
            logger.error("/QueueUserinfo/update错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
        }
        return jsonObject;


    }



    /**
     * 删除
     */
    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object delete(String deleteLss) {
        JSONObject jsonObject = new JSONObject();
        try {
            String[] deletes = deleteLss.split(",");
            for (int i = 0; i < deletes.length; i++) {
                QueueUserinfo entity = new QueueUserinfo();
                entity.setStatus("0");
                            queueUserinfoMapper.update(entity,
                                new QueryWrapper<QueueUserinfo>().eq("UserInfo_Ls", deletes[i]));
            }
            jsonObject.put("result", "ok");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/QueueUserinfo/delete错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }


    /**
    * 分页查询
    */
    @PostMapping("/list")
    public Object list(QueueUserinfo entity,
                       @RequestParam(required = false, defaultValue = "0") int pageNo,
                       @RequestParam(required = false, defaultValue = "10") int pageSize) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        QueueArea defaultProject = (QueueArea) session.getAttribute("DEFAULT_PROJECT");
        int areaLs = defaultProject.getAreaLs();
        entity.setAreaLs(areaLs);
        try {
            // 分页构造器
            Page<QueueUserinfo> page = new Page<QueueUserinfo>(pageNo, pageSize);

            // 执行分页
            IPage<QueueUserinfo> pageList = queueUserinfoMapper.searchUserInfoList(page, entity);
            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueUserinfo/list错误:" + e.getMessage(), e);
            return "error";
        }

    }

    @PostMapping("/listall")
    public Object listall(QueueUserinfo entity ) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        QueueArea defaultProject = (QueueArea) session.getAttribute("DEFAULT_PROJECT");
        int areaLs = defaultProject.getAreaLs();
        entity.setAreaLs(areaLs);

        try {

            // 执行查询
            List<QueueUserinfo> resultList = queueUserinfoMapper.searchUserInfoList(entity);
            // 返回结果
            return ApiReturnUtil.success(resultList);
        } catch (Exception e) {
            logger.error("/QueueUserinfo/listall错误:" + e.getMessage(), e);
            return "error";
        }

    }
    /**
* 分页查询
*/
    @RequestMapping(value = "/singletablelist", produces = "application/json;charset=utf-8")
    public Object singletablelist(QueueUserinfo entity, int pageNo, int pageSize) {
        try {
            // 分页构造器
            Page<QueueUserinfo> page = new Page<QueueUserinfo>(pageNo, pageSize);
                    String primaryName = entity.getUserinfoName();
                    QueryWrapper<QueueUserinfo> queryWrapper = new QueryWrapper<QueueUserinfo>();
                    queryWrapper.eq("Status","1");
                    if(!StrUtil.isBlank(primaryName)) //根据诊室名称的查询条件不为空
                    {
                        queryWrapper.like ("UserInfo_Name",primaryName);
                    }
            // 执行分页
            IPage<QueueUserinfo> pageList = queueUserinfoMapper.selectPage(page, queryWrapper);
            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueUserinfo/singletablelist错误:" + e.getMessage(), e);
            return "error";
        }
    }




    /**
     * 生成账号
     */
    @RequestMapping(value = "/createwebaccount", produces = "application/json;charset=utf-8")
    public Object createwebaccount(String deleteLss) {
        JSONObject jsonObject = new JSONObject();
        //删除有很多处理逻辑,要用到事务,封装到service中
        try {
            int result = queueUserinfoService.createwebaccount(deleteLss);
            if (result > 0) {
                jsonObject.put("result", "ok");
            } else {
                jsonObject.put("result", "error");
            }
        }
        catch(Exception e)
        {
          logger.error("/QueueUserinfo/createwebaccount错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
        }

        return jsonObject;


    }


}
