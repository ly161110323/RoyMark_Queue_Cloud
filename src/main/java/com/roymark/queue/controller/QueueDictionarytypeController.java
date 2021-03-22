package com.roymark.queue.controller;
import cn.hutool.core.util.StrUtil;

import com.roymark.queue.entity.QueueDictionarytype;
import com.roymark.queue.dao.QueueDictionarytypeMapper;
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
@RequestMapping("/QueueDictionarytype")
public class QueueDictionarytypeController {




    private static final Logger logger = LogManager.getLogger(QueueDictionarytypeController.class);
    @Autowired
    private QueueDictionarytypeMapper queueDictionarytypeMapper;


    /**
         * 新增
         */
    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(QueueDictionarytype tempQueueDictionarytype) {
        JSONObject jsonObject = new JSONObject();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGIN_USER");
        try {

            String createUser = user.getUserID();
            tempQueueDictionarytype.setCreateUser(createUser);
            LocalDateTime createTime = LocalDateTime.now();
            tempQueueDictionarytype.setCreateTime(createTime);
            tempQueueDictionarytype.setStatus("1");
            int result = queueDictionarytypeMapper.insert(tempQueueDictionarytype);
            if (result > 0) {
                jsonObject.put("result", "ok");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/QueueDictionarytype/insert错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(QueueDictionarytype tempQueueDictionarytype) {
        JSONObject jsonObject = new JSONObject();
        // 获取操作用户的用户名
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGIN_USER");
        String updateUser = user.getUserID();
        //进行修改操作
        try {


                tempQueueDictionarytype.setUpdateUser(updateUser);
                LocalDateTime updateTime = LocalDateTime.now();
                tempQueueDictionarytype.setUpdateTime(updateTime);
                tempQueueDictionarytype.setStatus("1");
                int result = queueDictionarytypeMapper.updateById(tempQueueDictionarytype);
                if (result > 0) {
                    jsonObject.put("result", "ok");
                } else {
                    jsonObject.put("result", "no");
                }

        }
        catch (Exception e) {
            logger.error("/QueueDictionarytype/update错误:" + e.getMessage(), e);
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
                QueueDictionarytype entity = new QueueDictionarytype();
                entity.setStatus("0");
                            queueDictionarytypeMapper.update(entity,
                                new QueryWrapper<QueueDictionarytype>().eq("DictionaryType_Ls", deletes[i]));
            }
            jsonObject.put("result", "ok");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/QueueDictionarytype/delete错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }


    /**
    * 分页查询
    */
    @PostMapping("/list")
    public Object list(QueueDictionarytype entity,
                       @RequestParam(required = false, defaultValue = "0") int pageNo,
                       @RequestParam(required = false, defaultValue = "10") int pageSize) {
        try {
            // 分页构造器
            Page<QueueDictionarytype> page = new Page<QueueDictionarytype>(pageNo, pageSize);
            // 条件构造器

                    String primaryName = entity.getDictionarytypeName();
                    QueryWrapper<QueueDictionarytype> queryWrapper = new QueryWrapper<QueueDictionarytype>();
                    queryWrapper.eq("Status","1");
                    if(!StrUtil.isBlank(primaryName)) //根据诊室名称的查询条件不为空
                    {
                        queryWrapper.like ("DictionaryType_Name",primaryName);
                    }
//                    String primaryName = entity.getFieldName();
//                    QueryWrapper<QueueDictionarytype> queryWrapper = new QueryWrapper<QueueDictionarytype>();
//                    queryWrapper.eq("Status","1");
//                    if(!StrUtil.isBlank(primaryName)) //根据诊室名称的查询条件不为空
//                    {
//                        queryWrapper.like ("Field_Name",primaryName);
//                    }
            // 执行分页
            IPage<QueueDictionarytype> pageList = queueDictionarytypeMapper.selectPage(page, queryWrapper);
            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueDictionarytype/list错误:" + e.getMessage(), e);
            return "error";
        }

    }
    /**
     * 分页查询
     */
    @PostMapping("/listall")
    public Object listall(QueueDictionarytype entity ) {
        try {
            String primaryName = entity.getDictionarytypeName();
            QueryWrapper<QueueDictionarytype> queryWrapper = new QueryWrapper<QueueDictionarytype>();
            queryWrapper.eq("Status","1");
//            queryWrapper.eq("DictionaryType_IsShow","1");
            if(!StrUtil.isBlank(primaryName))
            {
                queryWrapper.like ("DictionaryType_Name",primaryName);
            }
            // 执行分页
            List<QueueDictionarytype> resultList = queueDictionarytypeMapper.selectList(queryWrapper);
            // 返回结果
            return ApiReturnUtil.success(resultList);
        } catch (Exception e) {
            logger.error("/QueueDictionarytype/listall错误:" + e.getMessage(), e);
            return "error";
        }

    }
    /**
* 分页查询
*/
    @RequestMapping(value = "/singletablelist", produces = "application/json;charset=utf-8")
    public Object singletablelist(QueueDictionarytype entity, int pageNo, int pageSize) {
        try {
            // 分页构造器
            Page<QueueDictionarytype> page = new Page<QueueDictionarytype>(pageNo, pageSize);
                    String primaryName = entity.getDictionarytypeName();
                    QueryWrapper<QueueDictionarytype> queryWrapper = new QueryWrapper<QueueDictionarytype>();
                    queryWrapper.eq("Status","1");
                    if(!StrUtil.isBlank(primaryName)) //根据诊室名称的查询条件不为空
                    {
                        queryWrapper.like ("DictionaryType_Name",primaryName);
                    }
//                    String primaryName = entity.getFieldName();
//                    QueryWrapper<QueueDictionarytype> queryWrapper = new QueryWrapper<QueueDictionarytype>();
//                    queryWrapper.eq("Status","1");
//                    if(!StrUtil.isBlank(primaryName)) //根据诊室名称的查询条件不为空
//                    {
//                        queryWrapper.like ("Field_Name",primaryName);
//                    }
            // 执行分页
            IPage<QueueDictionarytype> pageList = queueDictionarytypeMapper.selectPage(page, queryWrapper);
            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueDictionarytype/singletablelist错误:" + e.getMessage(), e);
            return "error";
        }
    }



}
