package com.roymark.queue.controller;
import cn.hutool.core.util.StrUtil;

import com.roymark.queue.entity.QueueCasearea;
import com.roymark.queue.dao.QueueCaseareaMapper;
import com.roymark.queue.entity.User;
import com.roymark.queue.util.GetDataLs;
import com.roymark.queue.util.Pinyin;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.time.LocalDateTime;
import java.util.List;
import com.roymark.queue.util.ApiReturnUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.roymark.queue.entity.QueueArea;
/**
 * @author wangfz
 * @since 2020-03-23
 */
@RestController
@RequestMapping("/QueueCasearea")
public class QueueCaseareaController {



    private static final Logger logger = LogManager.getLogger(QueueCaseareaController.class);
    @Autowired
    private QueueCaseareaMapper queueCaseareaMapper;


    /**
         * 新增
         */
    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(QueueCasearea tempQueueCasearea) {
        JSONObject jsonObject = new JSONObject();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGIN_USER");
        try {

            String createUser = user.getUserID();
            tempQueueCasearea.setCreateUser(createUser);
            LocalDateTime createTime = LocalDateTime.now();
            tempQueueCasearea.setCreateTime(createTime);
            tempQueueCasearea.setStatus("1");
            int result = queueCaseareaMapper.insert(tempQueueCasearea);
            if (result > 0) {
                jsonObject.put("result", "ok");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/QueueCasearea/insert错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(QueueCasearea tempQueueCasearea) {
        JSONObject jsonObject = new JSONObject();
        // 获取操作用户的用户名
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGIN_USER");
        String updateUser = user.getUserID();
        //进行修改操作
        try {

                tempQueueCasearea.setUpdateUser(updateUser);
                LocalDateTime updateTime = LocalDateTime.now();
                tempQueueCasearea.setUpdateTime(updateTime);
                tempQueueCasearea.setStatus("1");
                int result = queueCaseareaMapper.updateById(tempQueueCasearea);
                if (result > 0) {
                    jsonObject.put("result", "ok");
                } else {
                    jsonObject.put("result", "no");
                }

        }
        catch (Exception e) {
            logger.error("/QueueCasearea/update错误:" + e.getMessage(), e);
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
                QueueCasearea entity = new QueueCasearea();
                entity.setStatus("0");
                            queueCaseareaMapper.update(entity,
                                new QueryWrapper<QueueCasearea>().eq("CaseArea_Ls", deletes[i]));
            }
            jsonObject.put("result", "ok");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/QueueCasearea/delete错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }


    /**
    * 分页查询
    */
    @PostMapping("/list")
    public Object list(QueueCasearea entity,
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
            Page<QueueCasearea> page = new Page<QueueCasearea>(pageNo, pageSize);

            IPage<QueueCasearea> pageList = queueCaseareaMapper.searchCaseAreaList(page, entity);
            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueCasearea/list错误:" + e.getMessage(), e);
            return "error";
        }

    }

    @PostMapping("/listall")
    public Object listall(QueueCasearea entity,HttpServletRequest request) {
        HttpSession session = request.getSession();
        QueueArea defaultProject = (QueueArea) session.getAttribute("DEFAULT_PROJECT");
        int areaLs = defaultProject.getAreaLs();
        entity.setAreaLs(areaLs);
        try {

            // 执行分页
            List<QueueCasearea> resultList =  queueCaseareaMapper.searchCaseAreaList(entity);
            // 返回结果
            return ApiReturnUtil.success(resultList);
        } catch (Exception e) {
            logger.error("/QueueCasearea/listall错误:" + e.getMessage(), e);
            return "error";
        }

    }
    /**
* 分页查询
*/
    @RequestMapping(value = "/singletablelist", produces = "application/json;charset=utf-8")
    public Object singletablelist(QueueCasearea entity, int pageNo, int pageSize) {
        try {
            // 分页构造器
            Page<QueueCasearea> page = new Page<QueueCasearea>(pageNo, pageSize);
                    String primaryName = entity.getCaseareaName();
                    QueryWrapper<QueueCasearea> queryWrapper = new QueryWrapper<QueueCasearea>();
                    queryWrapper.eq("Status","1");
                    if(!StrUtil.isBlank(primaryName))
                    {
                        queryWrapper.like ("CaseArea_Name",primaryName);
                    }
            // 执行分页
            IPage<QueueCasearea> pageList = queueCaseareaMapper.selectPage(page, queryWrapper);
            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueCasearea/singletablelist错误:" + e.getMessage(), e);
            return "error";
        }
    }



}
