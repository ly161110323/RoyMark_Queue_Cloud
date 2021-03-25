package com.roymark.queue.controller;
import cn.hutool.core.util.StrUtil;

import com.roymark.queue.entity.QueueArea;
import com.roymark.queue.entity.QueueDept;
import com.roymark.queue.dao.QueueDeptMapper;
import com.roymark.queue.entity.User;
import com.roymark.queue.service.QueueDeptService;
import com.roymark.queue.util.GetDataLs;
import com.roymark.queue.util.Pinyin;
import com.roymark.queue.util.UploadUtil;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 * @author wangfz
 * @since 2020-02-06
 */
@RestController
@RequestMapping("/QueueDept")
public class QueueDeptController {



    private static final Logger logger = LogManager.getLogger(QueueDeptController.class);
    @Autowired
    private QueueDeptMapper queueDeptMapper;
    @Autowired
    private QueueDeptService queueDeptService;

    /**
         * 新增
         */
    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(QueueDept tempQueueDept,@RequestParam(value = "uploadinfo", required = false) MultipartFile uploadinfo) {
        JSONObject jsonObject = new JSONObject();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGIN_USER");
        try {

            String createUser = user.getUserID();
            tempQueueDept.setCreateUser(createUser);
            LocalDateTime createTime = LocalDateTime.now();
            tempQueueDept.setCreateTime(createTime);
            tempQueueDept.setStatus("1");
            String filePath = "";
            if (uploadinfo != null) {

                String areaLs = String.valueOf(tempQueueDept.getAreaLs());
                String uploadPath = "/RemoteQueue/"+areaLs+"/upload/dept/"+tempQueueDept.getDeptId()+"/";
                filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
            }
            tempQueueDept.setDeptImagepath(filePath);
            int result = queueDeptMapper.insert(tempQueueDept);
            if (result > 0) {
                jsonObject.put("result", "ok");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/QueueDept/insert错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(QueueDept tempQueueDept,@RequestParam(value = "uploadinfo", required = false) MultipartFile uploadinfo) {
        JSONObject jsonObject = new JSONObject();
        // 获取操作用户的用户名
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGIN_USER");
        String updateUser = user.getUserID();
        //进行修改操作
        try {

                tempQueueDept.setUpdateUser(updateUser);
                LocalDateTime updateTime = LocalDateTime.now();
                tempQueueDept.setUpdateTime(updateTime);
                tempQueueDept.setStatus("1");
                String filePath = "";//
                if (uploadinfo != null) {

                    String areaLs = String.valueOf(tempQueueDept.getAreaLs());
                    String uploadPath = "/RemoteQueue/"+areaLs+"/upload/dept/"+tempQueueDept.getDeptId()+"/";
                    filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
                    tempQueueDept.setDeptImagepath(filePath);
                }
                int result = queueDeptMapper.updateById(tempQueueDept);
                if (result > 0) {
                    jsonObject.put("result", "ok");
                } else {
                    jsonObject.put("result", "no");
                }

        }
        catch (Exception e) {
            logger.error("/QueueDept/update错误:" + e.getMessage(), e);
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
                QueueDept entity = new QueueDept();
                entity.setStatus("0");
                            queueDeptMapper.update(entity,
                                new QueryWrapper<QueueDept>().eq("Dept_Ls", deletes[i]));
            }
            //删除有很多处理逻辑,要用到事务,封装到service中
            int result=queueDeptService.deleteDeptAssociateTables(deleteLss);
            if(result>0) {
                jsonObject.put("result", "ok");
            }
            else
            {
                jsonObject.put("result", "error");
            }
            return jsonObject;
        } catch (Exception e) {
            logger.error("/QueueDept/delete错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }


    /**
    * 分页查询
    */
    @PostMapping("/list")
    public Object list(QueueDept entity,
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
            Page<QueueDept> page = new Page<QueueDept>(pageNo, pageSize);

            // 执行分页
            IPage<QueueDept> pageList = queueDeptMapper.selectDeptList(page, entity);
            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueDept/list错误:" + e.getMessage(), e);
            return "error";
        }

    }
    @RequestMapping("/listall")
    public Object listall(QueueDept entity ) {
        try {

            QueryWrapper<QueueDept> queryWrapper = new QueryWrapper<QueueDept>(entity);
            // 执行分页
            List<QueueDept> allList = queueDeptMapper.selectList(queryWrapper);
            // 返回结果
            return ApiReturnUtil.success(allList);
        } catch (Exception e) {
            logger.error("/QueueDept/listall错误:" + e.getMessage(), e);
            return "error";
        }

    }

    /**
* 分页查询
*/
    @RequestMapping(value = "/singletablelist", produces = "application/json;charset=utf-8")
    public Object singletablelist(QueueDept entity, int pageNo, int pageSize) {
        try {
            // 分页构造器
            Page<QueueDept> page = new Page<QueueDept>(pageNo, pageSize);
                    String primaryName = entity.getDeptName();
                    QueryWrapper<QueueDept> queryWrapper = new QueryWrapper<QueueDept>();
                    queryWrapper.eq("Status","1");
                    if(!StrUtil.isBlank(primaryName)) //根据诊室名称的查询条件不为空
                    {
                        queryWrapper.like ("Dept_Name",primaryName);
                    }
            // 执行分页
            IPage<QueueDept> pageList = queueDeptMapper.selectPage(page, queryWrapper);
            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueDept/singletablelist错误:" + e.getMessage(), e);
            return "error";
        }
    }



}
