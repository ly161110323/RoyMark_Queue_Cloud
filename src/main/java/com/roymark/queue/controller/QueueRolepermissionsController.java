package com.roymark.queue.controller;
import cn.hutool.core.util.StrUtil;

import com.roymark.queue.dao.QueueMenuinfoMapper;
import com.roymark.queue.entity.*;
import com.roymark.queue.dao.QueueRolepermissionsMapper;

import com.roymark.queue.service.QueueRolepermissionsService;
import com.roymark.queue.util.ApiReturnUtil;
import com.roymark.queue.util.GetDataLs;
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

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 * @author wangfz
 * @since 2020-01-13
 */
@RestController
@RequestMapping("/QueueRolepermissions")
public class QueueRolepermissionsController {



    private static final Logger logger = LogManager.getLogger(QueueRolepermissionsController.class);
    @Autowired
    private QueueRolepermissionsMapper queueRolepermissionsMapper;
    @Autowired
    private QueueMenuinfoMapper queueMenuinfoMapper;
    @Autowired
    private QueueRolepermissionsService queueRolepermissionsService;


    /**
         * 新增
         */
    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(QueueRolepermissions tempQueueRolepermissions) {
        JSONObject jsonObject = new JSONObject();
        try {

            // 定义主键_Ls
            String primaryKeyLs = GetDataLs.getLs();
            // 定义Name_PY

            // 设置新增数据
                    tempQueueRolepermissions.setRolepermissionsLs(primaryKeyLs);
//            tempQueueRolepermissions.setCaseLs(defaultProject.getCaseLs());



            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            HttpSession session = request.getSession();
//            User user = (User) session.getAttribute("LOGIN_USER");
//            String createUser = user.getUserID();
//            tempQueueRolepermissions.setCreateUser(createUser);
            LocalDateTime createTime = LocalDateTime.now();
            tempQueueRolepermissions.setCreateTime(createTime);
            tempQueueRolepermissions.setStatus("1");
            int result = queueRolepermissionsMapper.insert(tempQueueRolepermissions);
            if (result > 0) {
                jsonObject.put("result", "ok");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/QueueRolepermissions/insert错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(QueueRolepermissions tempQueueRolepermissions) {
        JSONObject jsonObject = new JSONObject();
        // 获取操作用户的用户名
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("LOGIN_USER");
//        String updateUser = user.getUserID();
        //进行修改操作
        try {

            List<QueueRolepermissions> searchList =null;
//            List<QueueRolepermissions> searchList = queueRolepermissionsMapper.selectList(wrapper);//同名的大于1个则说明有重名
            if (searchList!= null && searchList.size()>1) //
            {
                jsonObject.put("result", "repeat");
                return jsonObject;
            } else {
//                tempQueueRolepermissions.setCaseLs(defaultProject.getCaseLs());//默认项目
//                tempQueueRolepermissions.setUpdateUser(updateUser);
                LocalDateTime updateTime = LocalDateTime.now();
                tempQueueRolepermissions.setUpdateTime(updateTime);
                tempQueueRolepermissions.setStatus("1");
                int result = queueRolepermissionsMapper.updateById(tempQueueRolepermissions);
                if (result > 0) {
                    jsonObject.put("result", "ok");
                } else {
                    jsonObject.put("result", "no");
                }
            }
        }
        catch (Exception e) {
            logger.error("/QueueRolepermissions/update错误:" + e.getMessage(), e);
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
                QueueRolepermissions entity = new QueueRolepermissions();
                entity.setStatus("0");
                            queueRolepermissionsMapper.update(entity,
                                new QueryWrapper<QueueRolepermissions>().eq("RolePermissions_Ls", deletes[i]));
            }
            jsonObject.put("result", "ok");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/QueueRolepermissions/delete错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }


    /**
    * 分页查询
    */
    @PostMapping("/list")
    public Object list(QueueRolepermissions entity,
                       @RequestParam(required = false, defaultValue = "0") int pageNo,
                       @RequestParam(required = false, defaultValue = "10") int pageSize) {
        try {
            // 分页构造器
            Page<QueueRolepermissions> page = new Page<QueueRolepermissions>(pageNo, pageSize);
            // 条件构造器

            // 执行分页
//            IPage<QueueRolepermissions> pageList = queueRolepermissionsMapper.selectPage(page, queryWrapper);
            // 返回结果
            IPage<QueueRolepermissions> pageList =null;
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueRolepermissions/list错误:" + e.getMessage(), e);
            return "error";
        }

    }
    /**
* 分页查询
*/
    @RequestMapping(value = "/singletablelist", produces = "application/json;charset=utf-8")
    public Object singletablelist(QueueRolepermissions entity, int pageNo, int pageSize) {
        try {
            // 分页构造器
            Page<QueueRolepermissions> page = new Page<QueueRolepermissions>(pageNo, pageSize);
            // 执行分页
//            IPage<QueueRolepermissions> pageList = queueRolepermissionsMapper.selectPage(page, queryWrapper);
            // 返回结果
            IPage<QueueRolepermissions> pageList =null;
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueRolepermissions/singletablelist错误:" + e.getMessage(), e);
            return "error";
        }
    }




    /**
     * 角色菜单权限
     */
    @RequestMapping(value = "/roleMenuPermissionSet",produces = "application/json;charset=utf-8")
    public Object roleMenuPermissionSet(QueueRolepermissions rolepermissions) {
        JSONObject jsonObject =new JSONObject();
        List<TreeJsonBean> treeJsonBeans = new ArrayList<TreeJsonBean>();
        try {
            String allCheckedFlag="1";
            TreeJsonBean tBean;
            //查询角色拥有的菜单权限
            List<QueueRolepermissions> rpList = new ArrayList<QueueRolepermissions>();
            QueryWrapper<QueueRolepermissions>queryWrapper = new QueryWrapper<QueueRolepermissions>();
            queryWrapper.lambda().eq(QueueRolepermissions::getStatus, "1")
                    .eq(QueueRolepermissions::getRoleLs, rolepermissions.getRoleLs())
                    .eq(QueueRolepermissions::getPermissionsType, rolepermissions.getPermissionsType());
            rpList = queueRolepermissionsMapper.selectList(queryWrapper);
            //获取菜单列表
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
//                    .getRequestAttributes();
//            HttpServletRequest request = attributes.getRequest();
//            HttpSession session = request.getSession();
//            QueueArea defaultArea=(QueueArea)session.getAttribute("DEFAULT_PROJECT");
//            Integer defaultAreaLs=defaultArea.getAreaLs();
            List<QueueMenuinfo>menulList =queueMenuinfoMapper
                    .selectList(new QueryWrapper<QueueMenuinfo>().eq("Status", "1").eq("Area_Ls",rolepermissions.getAreaLs()));
            String menuLs = "";
            for (int i = 0; i < menulList.size(); i++) {
                tBean=new TreeJsonBean();
                String upId = "60";
                if (menulList.get(i).getMenuUp()!=null) {
                    upId=menulList.get(i).getMenuUp();
                }
                tBean.setId(menulList.get(i).getMenuLs());
                tBean.setpId(upId);
                tBean.setName(menulList.get(i).getMenuName());
                tBean.setClick("true");
                tBean.setOpen("true");
                if (rpList.size()>0) {
                    tBean.setClick("false");
                    int count=0;
                    for (QueueRolepermissions rp : rpList) {
                        if (menulList.get(i).getMenuLs().equals(rp.getMenuLs())) {
                            menuLs += menulList.get(i).getMenuLs()+",";
                            count++;
                            break;
                        }
                    }
                    if (count>0) {
                        tBean.setChecked("true");
                    }else {
                        allCheckedFlag="0";
                        tBean.setChecked("false");
                    }
                }
                treeJsonBeans.add(tBean);
            }
            jsonObject.put("list", treeJsonBeans);
            jsonObject.put("caseLs", menuLs);
            jsonObject.put("allCheckedFlag", allCheckedFlag);
        } catch (Exception e) {
            logger.error("/QueueRolepermissions/roleMenuPermissionSet错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
        }
        return jsonObject;
    }

    /**
     * 修改角色菜单权限
     */
    @RequestMapping(value = "/updateRoleMenuPermission",produces = "application/json;charset=utf-8")
    public Object updateRoleMenuPermission(QueueRolepermissions rolepermissions,String menuLss) {
        JSONObject jsonObject = new JSONObject();
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("LOGIN_USER");
            String userID = user.getUserID();
            //先删除用户下的角色
            rolepermissions.setStatus("0");
            rolepermissions.setUpdateUser(userID);
            LocalDateTime updateTime = LocalDateTime.now();
            rolepermissions.setUpdateTime(updateTime);
            QueryWrapper<QueueRolepermissions>queryWrapper=new QueryWrapper<QueueRolepermissions>();
            queryWrapper.lambda().eq(QueueRolepermissions::getRoleLs, rolepermissions.getRoleLs())
                    .eq(QueueRolepermissions::getPermissionsType, rolepermissions.getPermissionsType());
            queueRolepermissionsMapper.update(rolepermissions, queryWrapper);
            //重新添加新的数据
            String[] menus = menuLss.split(",");
            int result=0;
            QueueRolepermissions rp;
            for (int i = 0; i < menus.length; i++) {
                rp=new QueueRolepermissions();
                rp.setRolepermissionsLs(GetDataLs.getLs());
                rp.setMenuLs(menus[i]);
                rp.setRoleLs(rolepermissions.getRoleLs());
                rp.setPermissionsType(rolepermissions.getPermissionsType());
                int status = queueRolepermissionsService.updateRolePermission(rp, userID);
                if (status>0) {
                    result++;
                }
            }
            if (result>0) {
                jsonObject.put("result", "ok");
            }else {
                jsonObject.put("result", "no");
            }
        } catch (Exception e) {
            logger.error("/QueueRolepermissions/updateRoleMenuPermission错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
        return jsonObject;
    }


}
