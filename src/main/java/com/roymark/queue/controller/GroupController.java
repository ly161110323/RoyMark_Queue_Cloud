package com.roymark.queue.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.Camera;
import com.roymark.queue.entity.Group;
import com.roymark.queue.service.CameraService;
import com.roymark.queue.service.GroupService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/group")
public class GroupController {
    private static final Logger logger = LogManager.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @Autowired
    private CameraService cameraService;

    @RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
    public Object getAll() {
        JSONObject jsonObject = new JSONObject();

        try {
            List<Group> groups = groupService.list();
            if (groups.size() <= 0) {
                jsonObject.put("groups", groups);
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "暂无楼层");
            }
            jsonObject.put("groups", groups);
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "获取成功");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/group/getAll 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(Group group) {
        JSONObject jsonObject = new JSONObject();

        try {
            group.setGroupHiddenId(Long.valueOf(0));

            Group queryGroup = groupService.getOne(Wrappers.<Group>lambdaQuery().eq(Group::getGroupId, group.getGroupId()));
            if (queryGroup != null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "组ID已存在");
                return jsonObject;
            }
            boolean result = groupService.save(group);

            if (result) {
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "添加成功");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "添加失败");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/group/insert 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "添加出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(Group group) {
        JSONObject jsonObject = new JSONObject();

        try {
            Group queryGroup = groupService.getById(group.getGroupHiddenId());

            if (queryGroup == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "楼层不存在");
                return jsonObject;
            }
            if (group.getGroupId() == null || group.getGroupId().equals("")) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "楼层ID不能为空");
                return jsonObject;
            }
            queryGroup = groupService.getOne(Wrappers.<Group>lambdaQuery().eq(Group::getGroupId, group.getGroupId()));
            if (queryGroup != null && !queryGroup.getGroupHiddenId().equals(group.getGroupHiddenId())) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "楼层ID已存在");
                return jsonObject;
            }

            boolean result = groupService.update(group, Wrappers.<Group>lambdaUpdate().eq(Group::getGroupHiddenId, group.getGroupHiddenId()));
            if (result) {
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "修改成功");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "修改失败");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/group/update 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "修改出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object delete(String deleteId) {
        JSONObject jsonObject = new JSONObject();
        try {
            String[] deletes = deleteId.split(",");
            if (deletes.length <= 0)
            {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "没有选中的删除项");
                return jsonObject;
            }
            for (int i=0; i<deletes.length; i++) {
                Group group = groupService.getById(Long.valueOf(deletes[i]));
                if (group == null) {
                    jsonObject.put("result", "error");
                    jsonObject.put("msg", "数据不存在");
                    return jsonObject;
                }
            }
            for (int i = 0; i < deletes.length; i++) {
                // 首先删除所有关联摄像头
                Long deleteGroupHiddenId = Long.valueOf(deletes[i]);
                List<Camera> cameras = cameraService.list(Wrappers.<Camera>lambdaQuery().eq(Camera::getGroupHiddenId, deleteGroupHiddenId));
                for (Camera camera : cameras) {
                    cameraService.update(camera, Wrappers.<Camera>lambdaUpdate().set(Camera::getGroupHiddenId, null)
                            .eq(Camera::getCamHiddenId, camera.getCamHiddenId()));
                }
                groupService.removeById(deleteGroupHiddenId);
            }
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "删除成功");
            return jsonObject;

        } catch (Exception e) {
            logger.error("/group/delete 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "删除出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
    public Object getOne(Long groupHiddenId) {
        JSONObject jsonObject = new JSONObject();

        try {
            Group group = groupService.getById(groupHiddenId);
            if (group != null) {
                jsonObject.put("result", "ok");
                jsonObject.put("group", group);
                jsonObject.put("msg", "获取成功");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "获取失败");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/group/getOne 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }


    @RequestMapping(value = "/queryData", produces = "application/json;charset=utf-8")
    public Object search(int pageNo, int pageSize) {
        JSONObject jsonObject = new JSONObject();

        try {
            // 分页构造器
            Page<Group> page = new Page<Group>(pageNo, pageSize);
            QueryWrapper<Group> queryWrapper = new QueryWrapper<Group>();
            queryWrapper.orderByAsc("group_id");
            // 执行分页
            IPage<Group> pageList = groupService.page(page, queryWrapper);
            // 返回结果
            if (pageList.getRecords().size() <= 0) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "搜素结果为空");
                jsonObject.put("pageList", pageList);
                return jsonObject;
            }
            else {
                jsonObject.put("pageList", pageList);
                jsonObject.put("result", "ok");
                jsonObject.put("msg", "搜索成功");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/group/queryData 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "搜索出现错误");
            return jsonObject;
        }
    }

}
