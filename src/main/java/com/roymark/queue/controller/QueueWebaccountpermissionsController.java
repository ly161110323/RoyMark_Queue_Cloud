package com.roymark.queue.controller;
import cn.hutool.core.util.StrUtil;
import com.roymark.queue.dao.QueueRoleinfoMapper;
import com.roymark.queue.entity.QueueRoleinfo;
import com.roymark.queue.entity.QueueWebaccountpermissions;
import com.roymark.queue.dao.QueueWebaccountpermissionsMapper;
import com.roymark.queue.entity.TreeJsonBean;
import com.roymark.queue.entity.User;
import com.roymark.queue.service.QueueWebaccountpermissionsService;
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
/**
 * @author wangfz
 * @since 2020-01-13
 */
@RestController
@RequestMapping("/QueueWebaccountpermissions")
public class QueueWebaccountpermissionsController {



    private static final Logger logger = LogManager.getLogger(QueueWebaccountpermissionsController.class);
    @Autowired
    private QueueWebaccountpermissionsMapper queueWebaccountpermissionsMapper;
    @Autowired
    private QueueRoleinfoMapper queueRoleinfoMapper;
    @Autowired
    private QueueWebaccountpermissionsService queueWebaccountpermissionsService;


    /**
         * 新增
         */
    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(QueueWebaccountpermissions tempQueueWebaccountpermissions) {
        JSONObject jsonObject = new JSONObject();
        try {

            // 定义主键_Ls
            String primaryKeyLs = GetDataLs.getLs();
            // 定义Name_PY

            // 设置新增数据
                    tempQueueWebaccountpermissions.setWebaccountpermissionsLs(primaryKeyLs);
//            tempQueueWebaccountpermissions.setCaseLs(defaultProject.getCaseLs());

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            HttpSession session = request.getSession();
//            User user = (User) session.getAttribute("LOGIN_USER");
//            String createUser = user.getUserID();
//            tempQueueWebaccountpermissions.setCreateUser(createUser);
            LocalDateTime createTime = LocalDateTime.now();
            tempQueueWebaccountpermissions.setCreateTime(createTime);
            tempQueueWebaccountpermissions.setStatus("1");
            int result = queueWebaccountpermissionsMapper.insert(tempQueueWebaccountpermissions);
            if (result > 0) {
                jsonObject.put("result", "ok");
                return jsonObject;
            } else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/QueueWebaccountpermissions/insert错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(QueueWebaccountpermissions tempQueueWebaccountpermissions) {
        JSONObject jsonObject = new JSONObject();
        // 获取操作用户的用户名
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("LOGIN_USER");
//        String updateUser = user.getUserID();
        //进行修改操作
        try {

            List<QueueWebaccountpermissions> searchList =null;
//            List<QueueWebaccountpermissions> searchList = queueWebaccountpermissionsMapper.selectList(wrapper);//同名的大于1个则说明有重名
            if (searchList!= null && searchList.size()>1) //
            {
                jsonObject.put("result", "repeat");
                return jsonObject;
            } else {
//                tempQueueWebaccountpermissions.setCaseLs(defaultProject.getCaseLs());//默认项目
//                tempQueueWebaccountpermissions.setUpdateUser(updateUser);
                LocalDateTime updateTime = LocalDateTime.now();
                tempQueueWebaccountpermissions.setUpdateTime(updateTime);
                tempQueueWebaccountpermissions.setStatus("1");
                int result = queueWebaccountpermissionsMapper.updateById(tempQueueWebaccountpermissions);
                if (result > 0) {
                    jsonObject.put("result", "ok");
                } else {
                    jsonObject.put("result", "no");
                }
            }
        }
        catch (Exception e) {
            logger.error("/QueueWebaccountpermissions/update错误:" + e.getMessage(), e);
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
                QueueWebaccountpermissions entity = new QueueWebaccountpermissions();
                entity.setStatus("0");
                            queueWebaccountpermissionsMapper.update(entity,
                                new QueryWrapper<QueueWebaccountpermissions>().eq("WebAccountPermissions_Ls", deletes[i]));
            }
            jsonObject.put("result", "ok");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/QueueWebaccountpermissions/delete错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }


    /**
    * 分页查询
    */
    @PostMapping("/list")
    public Object list(QueueWebaccountpermissions entity,
                       @RequestParam(required = false, defaultValue = "0") int pageNo,
                       @RequestParam(required = false, defaultValue = "10") int pageSize) {
        try {
            // 分页构造器
            Page<QueueWebaccountpermissions> page = new Page<QueueWebaccountpermissions>(pageNo, pageSize);
            // 条件构造器

            // 执行分页
//            IPage<QueueWebaccountpermissions> pageList = queueWebaccountpermissionsMapper.selectPage(page, queryWrapper);
            // 返回结果
            IPage<QueueWebaccountpermissions> pageList =null;
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueWebaccountpermissions/list错误:" + e.getMessage(), e);
            return "error";
        }

    }
    /**
* 分页查询
*/
    @RequestMapping(value = "/singletablelist", produces = "application/json;charset=utf-8")
    public Object singletablelist(QueueWebaccountpermissions entity, int pageNo, int pageSize) {
        try {
            // 分页构造器
            Page<QueueWebaccountpermissions> page = new Page<QueueWebaccountpermissions>(pageNo, pageSize);
            // 执行分页
//            IPage<QueueWebaccountpermissions> pageList = queueWebaccountpermissionsMapper.selectPage(page, queryWrapper);
            // 返回结果
            IPage<QueueWebaccountpermissions> pageList =null;
            return ApiReturnUtil.success(pageList);
        } catch (Exception e) {
            logger.error("/QueueWebaccountpermissions/singletablelist错误:" + e.getMessage(), e);
            return "error";
        }
    }

    /**
     * 账号权限
     */
    @RequestMapping(value = "/webaccountpermissionset", produces = "application/json;charset=utf-8")
    public Object webaccountpermissionset(QueueWebaccountpermissions webaccountpermissions) {
        JSONObject jsonObject = new JSONObject();
        List<TreeJsonBean> treeJsonBeans = new ArrayList<TreeJsonBean>();
        try {
            String allCheckedFlag = "1";
            TreeJsonBean tBean;
            // 查询账号拥有的项目权限
            List<QueueWebaccountpermissions> rpList = new ArrayList<QueueWebaccountpermissions>();
            QueryWrapper<QueueWebaccountpermissions> queryWrapper = new QueryWrapper<QueueWebaccountpermissions>();
            queryWrapper.lambda().eq(QueueWebaccountpermissions::getStatus, "1")
                    .eq(QueueWebaccountpermissions::getWebaccountLs, webaccountpermissions.getWebaccountLs());
            rpList = queueWebaccountpermissionsMapper.selectList(queryWrapper);
            // 获取项目列表
            List<QueueRoleinfo> roleList = queueRoleinfoMapper
                    .selectList(new QueryWrapper<QueueRoleinfo>().eq("Status", "1")
                            .eq("Role_IsShow", "1").eq("area_Ls",webaccountpermissions.getAreaLs()));
            String roleLs = "";
            for (int i = 0; i < roleList.size(); i++) {
                tBean = new TreeJsonBean();
                String upId = "0";
                tBean.setId(roleList.get(i).getRoleLs());
                tBean.setpId(upId);
                tBean.setName(roleList.get(i).getRoleName());
                tBean.setClick("true");
                tBean.setOpen("true");
                if (rpList.size() > 0) {
                    tBean.setClick("false");
                    int count = 0;
                    for (QueueWebaccountpermissions rp : rpList) {
                        if (roleList.get(i).getRoleLs().equals(rp.getRoleLs())) {
                            roleLs += roleList.get(i).getRoleName() + ",";
                            count++;
                            break;
                        }
                    }
                    if (count > 0) {
                        tBean.setChecked("true");
                    } else {
                        allCheckedFlag = "0";
                        tBean.setChecked("false");
                    }
                }
                treeJsonBeans.add(tBean);
            }
            jsonObject.put("list", treeJsonBeans);
            jsonObject.put("roleLs", roleLs);
            jsonObject.put("allCheckedFlag", allCheckedFlag);
        } catch (Exception e) {
            logger.error("/QueueWebaccountpermissions/webaccountpermissionset错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
        }
        return jsonObject;
    }

    /**
     * 修改账号权限
     */
    @RequestMapping(value = "/updatewebaccountpermission",produces = "application/json;charset=utf-8")
    public Object updatewebaccountpermission(QueueWebaccountpermissions webaccountpermissions,String roleLss) {
        JSONObject jsonObject = new JSONObject();
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("LOGIN_USER");
            String userID = user.getUserID();
            //先删除用户下的角色
            webaccountpermissions.setStatus("0");
            webaccountpermissions.setUpdateUser(userID);
            LocalDateTime updateTime = LocalDateTime.now();
            webaccountpermissions.setUpdateTime(updateTime);
            QueryWrapper<QueueWebaccountpermissions>queryWrapper=new QueryWrapper<QueueWebaccountpermissions>();
            queryWrapper.lambda().eq(QueueWebaccountpermissions::getWebaccountLs, webaccountpermissions.getWebaccountLs());
            queueWebaccountpermissionsMapper.update(webaccountpermissions, queryWrapper);
            //重新添加新的数据
            String[] roles = roleLss.split(",");
            int result=0;
            QueueWebaccountpermissions rp;
            for (int i = 0; i < roles.length; i++) {
                rp=new QueueWebaccountpermissions();
                rp.setWebaccountpermissionsLs(GetDataLs.getLs());
                rp.setRoleLs(roles[i]);
                rp.setWebaccountLs(webaccountpermissions.getWebaccountLs());
                rp.setAreaLs(webaccountpermissions.getAreaLs());
                int status = queueWebaccountpermissionsService.updateWebAccountPermission(rp, userID);
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
            logger.error("/QueueWebaccountpermissions/updatewebaccountpermission错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
        return jsonObject;
    }





}
