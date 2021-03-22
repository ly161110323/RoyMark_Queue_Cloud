package com.roymark.queue.controller;
import cn.hutool.core.util.StrUtil;

import com.roymark.queue.entity.QueueArea;
import com.roymark.queue.entity.QueueDictionary;
import com.roymark.queue.dao.QueueDictionaryMapper;
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
/**
 * @author wangfz
 * @since 2020-02-24
 */
@RestController
@RequestMapping("/QueueDictionary")
public class QueueDictionaryController {



    private static final Logger logger = LogManager.getLogger(QueueDictionaryController.class);
    @Autowired
    private QueueDictionaryMapper queueDictionaryMapper;


    /**
         * 新增
         */
    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(QueueDictionary tempQueueDictionary) {
        JSONObject jsonObject = new JSONObject();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGIN_USER");
        try {

            String createUser = user.getUserID();
            tempQueueDictionary.setCreateUser(createUser);
            LocalDateTime createTime = LocalDateTime.now();
            tempQueueDictionary.setCreateTime(createTime);
            tempQueueDictionary.setStatus("1");
            int result = queueDictionaryMapper.insert(tempQueueDictionary);
            if (result > 0) {
                jsonObject.put("result", "ok");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/QueueDictionary/insert错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(QueueDictionary tempQueueDictionary) {
        JSONObject jsonObject = new JSONObject();
        // 获取操作用户的用户名
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGIN_USER");
        String updateUser = user.getUserID();
        //进行修改操作
        try {
                tempQueueDictionary.setUpdateUser(updateUser);
                LocalDateTime updateTime = LocalDateTime.now();
                tempQueueDictionary.setUpdateTime(updateTime);
                tempQueueDictionary.setStatus("1");
                int result = queueDictionaryMapper.updateById(tempQueueDictionary);
                if (result > 0) {
                    jsonObject.put("result", "ok");
                } else {
                    jsonObject.put("result", "no");
                }

        }
        catch (Exception e) {
            logger.error("/QueueDictionary/update错误:" + e.getMessage(), e);
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
                QueueDictionary entity = new QueueDictionary();
                entity.setStatus("0");
                            queueDictionaryMapper.update(entity,
                                new QueryWrapper<QueueDictionary>().eq("Dictionary_Ls", deletes[i]));
            }
            jsonObject.put("result", "ok");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/QueueDictionary/delete错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }


    /**
    * 分页查询
    */
    @PostMapping("/list")
    public Object list(QueueDictionary entity,
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
            Page<QueueDictionary> page = new Page<QueueDictionary>(pageNo, pageSize);

            IPage<QueueDictionary> pageList = queueDictionaryMapper.searchDictionary(page, entity);
            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueDictionary/list错误:" + e.getMessage(), e);
            return "error";
        }

    }
    /**
     * 分页查询
     */
    @PostMapping("/listbytype")
    public Object listbytype(QueueDictionary entity ) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        QueueArea defaultProject = (QueueArea) session.getAttribute("DEFAULT_PROJECT");
        int areaLs = defaultProject.getAreaLs();
        entity.setAreaLs(areaLs);

        try {
            List<QueueDictionary> resultList = queueDictionaryMapper.getDictionaryByType(entity);
            // 返回结果
            return ApiReturnUtil.success(resultList);
        } catch (Exception e) {
            logger.error("/QueueDictionary/listbytype错误:" + e.getMessage(), e);
            return "error";
        }

    }
    /**
     * 分页查询
     */
    @PostMapping("/lisexetypedictionary")
    public Object lisexetypedictionary(QueueDictionary entity ) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        QueueArea defaultProject = (QueueArea) session.getAttribute("DEFAULT_PROJECT");
        int areaLs = defaultProject.getAreaLs();
        entity.setAreaLs(areaLs);

        try {
            List<QueueDictionary> resultList = queueDictionaryMapper.getExeTypeDicTionaryInfo(entity);
            // 返回结果
            return ApiReturnUtil.success(resultList);
        } catch (Exception e) {
            logger.error("/QueueDictionary/lisexetypedictionary错误:" + e.getMessage(), e);
            return "error";
        }

    }
    /**
* 分页查询
*/
    @RequestMapping(value = "/singletablelist", produces = "application/json;charset=utf-8")
    public Object singletablelist(QueueDictionary entity, int pageNo, int pageSize) {
        try {
            // 分页构造器
            Page<QueueDictionary> page = new Page<QueueDictionary>(pageNo, pageSize);
                    String primaryName = entity.getDictionaryName();
                    QueryWrapper<QueueDictionary> queryWrapper = new QueryWrapper<QueueDictionary>();
                    queryWrapper.eq("Status","1");
                    if(!StrUtil.isBlank(primaryName)) //根据诊室名称的查询条件不为空
                    {
                        queryWrapper.like ("Dictionary_Name",primaryName);
                    }
            // 执行分页
            IPage<QueueDictionary> pageList = queueDictionaryMapper.selectPage(page, queryWrapper);
            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueDictionary/singletablelist错误:" + e.getMessage(), e);
            return "error";
        }
    }



}
