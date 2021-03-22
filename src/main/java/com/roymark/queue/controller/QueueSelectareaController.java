package com.roymark.queue.controller;
import cn.hutool.core.util.StrUtil;

import com.roymark.queue.entity.QueueSelectarea;
import com.roymark.queue.dao.QueueSelectareaMapper;
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
@RequestMapping("/QueueSelectarea")
public class QueueSelectareaController {



    private static final Logger logger = LogManager.getLogger(QueueSelectareaController.class);
    @Autowired
    private QueueSelectareaMapper queueSelectareaMapper;


    /**
         * 新增
         */
    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(QueueSelectarea tempQueueSelectarea) {
        JSONObject jsonObject = new JSONObject();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGIN_USER");
        try {

            String createUser = user.getUserID();
            tempQueueSelectarea.setCreateUser(createUser);
            LocalDateTime createTime = LocalDateTime.now();
            tempQueueSelectarea.setCreateTime(createTime);
            tempQueueSelectarea.setStatus("1");
            int result = queueSelectareaMapper.insert(tempQueueSelectarea);
            if (result > 0) {
                jsonObject.put("result", "ok");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/QueueSelectarea/insert错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(QueueSelectarea tempQueueSelectarea) {
        JSONObject jsonObject = new JSONObject();
        // 获取操作用户的用户名
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGIN_USER");
        String updateUser = user.getUserID();
        //进行修改操作
        try {

                tempQueueSelectarea.setUpdateUser(updateUser);
                LocalDateTime updateTime = LocalDateTime.now();
                tempQueueSelectarea.setUpdateTime(updateTime);
                tempQueueSelectarea.setStatus("1");
                int result = queueSelectareaMapper.updateById(tempQueueSelectarea);
                if (result > 0) {
                    jsonObject.put("result", "ok");
                } else {
                    jsonObject.put("result", "no");
                }

        }
        catch (Exception e) {
            logger.error("/QueueSelectarea/update错误:" + e.getMessage(), e);
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
                QueueSelectarea entity = new QueueSelectarea();
                entity.setStatus("0");
                            queueSelectareaMapper.update(entity,
                                new QueryWrapper<QueueSelectarea>().eq("SelectArea_Ls", deletes[i]));
            }
            jsonObject.put("result", "ok");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/QueueSelectarea/delete错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }


    /**
    * 分页查询
    */
    @PostMapping("/list")
    public Object list(QueueSelectarea entity,
                       @RequestParam(required = false, defaultValue = "0") int pageNo,
                       @RequestParam(required = false, defaultValue = "10") int pageSize) {
        try {
            // 分页构造器
            Page<QueueSelectarea> page = new Page<QueueSelectarea>(pageNo, pageSize);
            // 条件构造器

                    String primaryName = entity.getSelectareaName();
                    QueryWrapper<QueueSelectarea> queryWrapper = new QueryWrapper<QueueSelectarea>();
                    queryWrapper.eq("Status","1");
                    if(!StrUtil.isBlank(primaryName))
                    {
                        queryWrapper.like ("SelectArea_Name",primaryName);
                    }
            // 执行分页
            IPage<QueueSelectarea> pageList = queueSelectareaMapper.selectPage(page, queryWrapper);
            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueSelectarea/list错误:" + e.getMessage(), e);
            return "error";
        }

    }

    @PostMapping("/listall")
    public Object listall(QueueSelectarea entity,HttpServletRequest request) {
        HttpSession session = request.getSession();
        QueueArea defaultProject = (QueueArea) session.getAttribute("DEFAULT_PROJECT");
        int areaLs = defaultProject.getAreaLs();
        entity.setAreaLs(areaLs);
        try {

            List<QueueSelectarea> resultList =  queueSelectareaMapper.searchSelectAreaList(entity);
            // 返回结果
            return ApiReturnUtil.success(resultList);
        } catch (Exception e) {
            logger.error("/QueueSelectarea/listall错误:" + e.getMessage(), e);
            return "error";
        }

    }
    /**
* 分页查询
*/
    @RequestMapping(value = "/singletablelist", produces = "application/json;charset=utf-8")
    public Object singletablelist(QueueSelectarea entity, int pageNo, int pageSize) {
        try {
            // 分页构造器
            Page<QueueSelectarea> page = new Page<QueueSelectarea>(pageNo, pageSize);
                    String primaryName = entity.getSelectareaName();
                    QueryWrapper<QueueSelectarea> queryWrapper = new QueryWrapper<QueueSelectarea>();
                    queryWrapper.eq("Status","1");
                    if(!StrUtil.isBlank(primaryName))
                    {
                        queryWrapper.like ("SelectArea_Name",primaryName);
                    }
            // 执行分页
            IPage<QueueSelectarea> pageList = queueSelectareaMapper.selectPage(page, queryWrapper);
            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueSelectarea/singletablelist错误:" + e.getMessage(), e);
            return "error";
        }
    }



}
