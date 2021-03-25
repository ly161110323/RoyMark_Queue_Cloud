package com.roymark.queue.controller;

import cn.hutool.core.util.StrUtil;

import com.roymark.queue.entity.QueueArea;
import com.roymark.queue.dao.QueueAreaMapper;

import com.roymark.queue.entity.QueueMenuinfo;
import com.roymark.queue.service.QueueMenuinfoService;
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
import java.util.ArrayList;
import java.util.List;

import com.roymark.queue.util.ApiReturnUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.roymark.queue.entity.User;

/**
 * @author wangfz
 * @since 2020-01-13
 */
@RestController
@RequestMapping("/QueueArea")
public class QueueAreaController {


    private static final Logger logger = LogManager.getLogger(QueueAreaController.class);
    @Autowired
    private QueueAreaMapper queueAreaMapper;

    @Autowired
    private QueueMenuinfoService queueMenuinfoService;

//    @Autowired
//    private HospitalqueueCaseService hospitalqueueCaseService;
//    @Autowired
//    private HospitalqueueTerminalexeconfigMapper hospitalqueueTerminalexeconfigMapper;

    /**
     * 新增
     */
    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(QueueArea tempQueueArea) {
        JSONObject jsonObject = new JSONObject();
        try {
            String primaryName = tempQueueArea.getAreaName();
            QueryWrapper <QueueArea> wrapper = new QueryWrapper <QueueArea>();
            wrapper.eq("Area_Name", primaryName);
            wrapper.eq("Status", "1");
            QueueArea dbEntity = queueAreaMapper.selectOne(wrapper);

            if (dbEntity != null) //数据库中已有同名数据，则重名
            {
                jsonObject.put("result", "repeat");
                return jsonObject;
            } else {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                HttpSession session = request.getSession();
//            User user = (User) session.getAttribute("LOGIN_USER");
//            String createUser = user.getUserID();
//            tempQueueArea.setCreateUser(createUser);
                LocalDateTime createTime = LocalDateTime.now();
                tempQueueArea.setCreateTime(createTime);
                tempQueueArea.setStatus("1");
                int result = queueAreaMapper.insert(tempQueueArea);
                if (result > 0) {
                    jsonObject.put("result", "ok");
                    return jsonObject;
                } else {
                    jsonObject.put("result", "no");
                    return jsonObject;
                }
            }
        } catch (Exception e) {
            logger.error("/QueueArea/insert错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(QueueArea tempQueueArea) {
        JSONObject jsonObject = new JSONObject();
        // 获取操作用户的用户名
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("LOGIN_USER");
//        String updateUser = user.getUserID();
        //进行修改操作
        try {

            String primaryName = tempQueueArea.getAreaName();
            QueryWrapper <QueueArea> wrapper = new QueryWrapper <QueueArea>();
            wrapper.eq("Area_Name", primaryName);
            wrapper.eq("Status", "1");
            QueueArea dbEntity = queueAreaMapper.selectOne(wrapper);

            if (dbEntity == null) //数据库中更没有同名的数据,可以直接修改
            {
                LocalDateTime updateTime = LocalDateTime.now();
                tempQueueArea.setUpdateTime(updateTime);
                tempQueueArea.setStatus("1");
                int result = queueAreaMapper.updateById(tempQueueArea);
                if (result > 0) {
                    jsonObject.put("result", "ok");
                } else {
                    jsonObject.put("result", "no");
                }

            } else {
                int dbAreaLs = dbEntity.getAreaLs().intValue();
                int tempAreaLs = tempQueueArea.getAreaLs().intValue();
                if (dbAreaLs == tempAreaLs) //数据库主键和传入主键相同可以更新，如不同说明数据库中有另一条同名数据，不能更新
                {
                    LocalDateTime updateTime = LocalDateTime.now();
                    tempQueueArea.setUpdateTime(updateTime);
                    tempQueueArea.setStatus("1");
                    int result = queueAreaMapper.updateById(tempQueueArea);
                    if (result > 0) {
                        jsonObject.put("result", "ok");
                    } else {
                        jsonObject.put("result", "no");
                    }
                } else {
                    jsonObject.put("result", "repeat");
                    return jsonObject;
                }

            }
        } catch (Exception e) {
            logger.error("/QueueArea/update错误:" + e.getMessage(), e);
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
                QueueArea entity = new QueueArea();
                entity.setStatus("0");
                queueAreaMapper.update(entity,
                        new QueryWrapper <QueueArea>().eq("Area_Ls", deletes[i]));
            }
            jsonObject.put("result", "ok");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/QueueArea/delete错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }


    /**
     * 分页查询
     */
    @RequestMapping("/list")
    public Object list(QueueArea entity,
                       @RequestParam(required = false, defaultValue = "0") int pageNo,
                       @RequestParam(required = false, defaultValue = "10") int pageSize) {
        try {
            // 分页构造器
            Page <QueueArea> page = new Page <QueueArea>(pageNo, pageSize);
            // 条件构造器

            String primaryName = entity.getAreaName();
            QueryWrapper <QueueArea> queryWrapper = new QueryWrapper <QueueArea>();
            queryWrapper.eq("Status", "1");
            if (!StrUtil.isBlank(primaryName)) //根据诊室名称的查询条件不为空
            {
                queryWrapper.like("Area_Name", primaryName);
            }
            // 执行分页
            IPage <QueueArea> pageList = queueAreaMapper.selectPage(page, queryWrapper);
            List <QueueArea> resultList = pageList.getRecords();
            for (QueueArea tempEntity : resultList) {
                Integer parentLs = tempEntity.getParentAreaLs();
                QueueArea parentEnity = queueAreaMapper.selectById(parentLs);
                if (parentEnity != null) {
                    tempEntity.setParentAreaName(parentEnity.getAreaName()); //设置父节点名称到List对象中
                } else {
                    tempEntity.setParentAreaName("总厅(无上级办事大厅)");
                }
            }

            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueArea/list错误:" + e.getMessage(), e);
            return "error";
        }

    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/singletablelist", produces = "application/json;charset=utf-8")
    public Object singletablelist(QueueArea entity, int pageNo, int pageSize) {
        try {
            // 分页构造器
            Page <QueueArea> page = new Page <QueueArea>(pageNo, pageSize);
            String primaryName = entity.getAreaName();
            QueryWrapper <QueueArea> queryWrapper = new QueryWrapper <QueueArea>();
            queryWrapper.eq("Status", "1");
            if (!StrUtil.isBlank(primaryName)) //根据诊室名称的查询条件不为空
            {
                queryWrapper.like("Area_Name", primaryName);
            }
            // 执行分页
            IPage <QueueArea> pageList = queueAreaMapper.selectPage(page, queryWrapper);
            // 返回结果
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueArea/singletablelist错误:" + e.getMessage(), e);
            return "error";
        }
    }

    /**
     * 维护queuearea表数据时递归获取所有节点(带上根节点总厅(无上级办事大厅))
     */
    @RequestMapping(value = "/gettreenodes", produces = "application/json;charset=utf-8")
    public Object getTreeNodes() {
        try {
            List <QueueArea> resultList = new ArrayList <QueueArea>();
            QueueArea rootArea = new QueueArea();
            rootArea.setAreaLs(0);
            rootArea.setAreaName("总厅(无上级办事大厅)");
            rootArea.setParentAreaLs(-1); //构造根节点
            resultList.add(rootArea);

            List <QueueArea> tempList = generateTree(0);
            resultList.addAll(tempList);
            // 返回结果
            return ApiReturnUtil.success(resultList);
        } catch (Exception e) {
            logger.error("/QueueArea/getTreeNodes错误:" + e.getMessage(), e);
            return "error";
        }
    }

    /**
     * 首页左侧菜单栏递归获取所有节点(没有根节点总厅(无上级办事大厅))
     */
    @RequestMapping(value = "/indextreenodes", produces = "application/json;charset=utf-8")
    public Object getIndexTreeNodes() {
        try {
            //从session中拿默认项目信息,如果是顶级项目，则parntAreaLs=0;

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            HttpSession session = request.getSession();
            Object objUser = session.getAttribute("LOGIN_USER");//当前登录用户
            User curUser = (User) objUser;
            Integer topAreaLs = curUser.getTopAreaLs();

            Object objProject = session.getAttribute("DEFAULT_PROJECT");//当前大厅对象
            QueueArea defaultArea = new QueueArea();
            if (objProject != null) {
                defaultArea = (QueueArea) objProject;
            }
            List <QueueArea> resultList = new ArrayList <QueueArea>();
            List <QueueArea> tempList = new ArrayList <QueueArea>();
            if (topAreaLs == 0) {
                tempList = generateTree(0);
            } else {
                tempList = generateTreeById(topAreaLs);
            }
            resultList.addAll(tempList);
            // 返回结果
            return ApiReturnUtil.success(resultList);
        } catch (Exception e) {
            logger.error("/QueueArea/getTreeNodes错误:" + e.getMessage(), e);
            return "error";
        }
    }


    @RequestMapping(value = "/changearea")
    @ResponseBody
    public Object changeArea(@RequestParam(value = "areaLs", required = true) String areaLs) {
        try {
//            System.out.println("areaLs:"+areaLs);
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            HttpSession session = request.getSession();


            QueryWrapper <QueueArea> searchAreaWrapper = new QueryWrapper <QueueArea>();
            searchAreaWrapper.eq("Area_Ls", Integer.valueOf(areaLs));
            searchAreaWrapper.eq("status", "1");
            QueueArea defaultArea = queueAreaMapper.selectOne(searchAreaWrapper);
            if (defaultArea == null) //如果没有默认项目,提示出错
            {
                return "error";
            } else {
                session.setAttribute("DEFAULT_PROJECT", defaultArea);
            }
            //刷新session中的菜单
            Object objUser=session.getAttribute ("LOGIN_USER");
            User curUser=(User)objUser;
           Integer curAreaLs=curUser.getTopAreaLs();//当前用户顶级菜单
            if(curAreaLs==0) //超级管理员
            {
                QueryWrapper<QueueMenuinfo> menuinfoQueryWrapper = new QueryWrapper<QueueMenuinfo>();
                menuinfoQueryWrapper.eq("Menu_IsShow", "1");
                menuinfoQueryWrapper.eq("status", "1");
//                改菜单的界面布局,临时放开显示所有菜单
                if(defaultArea!=null)
                {
                    menuinfoQueryWrapper.eq("Area_Ls",defaultArea.getAreaLs());
                    menuinfoQueryWrapper.or().eq("is_super","1");//is super为1的菜单跟大厅无关，可以直接引入
                }
                menuinfoQueryWrapper.orderByAsc("Menu_No");
                List<QueueMenuinfo> menuList = queueMenuinfoService
                        .searchMenuInfoList(menuinfoQueryWrapper, true);
                session.setAttribute("menuList", menuList);
            }
            else //非超管，只显示当前大厅对应菜单
            {
                QueryWrapper<QueueMenuinfo> menuinfoQueryWrapper = new QueryWrapper<QueueMenuinfo>();
                menuinfoQueryWrapper.eq("Menu_IsShow", "1");
                menuinfoQueryWrapper.eq("status", "1");
                List<QueueMenuinfo> menuList = queueMenuinfoService
                        .searchMenuInfoListByAreaLs(Integer.valueOf(areaLs), true);
                session.setAttribute("menuList", menuList);
            }


            return "success";
        } catch (Exception e) {
            logger.error("/QueueArea/changearea:" + e.getMessage(), e);
            return "error";
        }
    }

    /*根据父节点areaLs生成树*/
    public List <QueueArea> generateTree(Integer parentId) {
        List <QueueArea> resultList = new ArrayList <QueueArea>();

        try {
            QueryWrapper <QueueArea> queryWrapper = new QueryWrapper <QueueArea>();
            queryWrapper.eq("Status", "1");
            queryWrapper.eq("parent_area_ls", parentId);
            queryWrapper.orderByAsc("Area_OrderNo"); //增加排序字段
            List <QueueArea> list = queueAreaMapper.selectList(queryWrapper);
            for (QueueArea tempArea : list) {
                Integer tempParentId = tempArea.getAreaLs();
                resultList.add(tempArea);
                List <QueueArea> childList = generateTree(tempParentId);
                resultList.addAll(childList);//求两个数组的并集

            }
        } catch (Exception ex) {
            logger.error("/QueueArea/generateTree错误:" + ex.getMessage(), ex);
        }


        return resultList;
    }

    /*根据当前节点areaLs生成tree*/
    public List <QueueArea> generateTreeById(Integer areaLs) {
        List <QueueArea> resultList = new ArrayList <QueueArea>();

        try {
            QueryWrapper <QueueArea> queryWrapper = new QueryWrapper <QueueArea>();
            queryWrapper.eq("Status", "1");
            queryWrapper.eq("Area_Ls", areaLs);
            queryWrapper.orderByAsc("Area_OrderNo"); //增加排序字段
            List <QueueArea> list = queueAreaMapper.selectList(queryWrapper);
            for (QueueArea tempArea : list) {
                Integer tempParentId = tempArea.getAreaLs();
                resultList.add(tempArea);
                List <QueueArea> childList = generateTree(tempParentId);//这里要调用根据父节点添加子节点的方法，不然会死循环
                resultList.addAll(childList);//求两个数组的并集
            }
        } catch (Exception ex) {
            logger.error("/QueueArea/generateTreeById错误:" + ex.getMessage(), ex);
        }
        return resultList;
    }


}
