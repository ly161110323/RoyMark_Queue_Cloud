package com.roymark.queue.controller;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roymark.queue.entity.Window;
import com.roymark.queue.service.WindowService;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/window")
public class WindowController {

    private static final Logger logger = LogManager.getLogger(WindowController.class);

    @Autowired
    private WindowService windowService;

    @RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
    public Object getAllWindows() {
        JSONObject jsonObject = new JSONObject();

        try {
            List<Window> windows = windowService.getAllWindow();
            if (windows.size() <= 0) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "暂无窗口");
                jsonObject.put("windows", windows);
                return jsonObject;
            }
            jsonObject.put("windows", windows);
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "获取成功");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/window/getAllWindows 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(Window window) {
        JSONObject jsonObject = new JSONObject();

        try {
            window.setWindowHiddenId(Long.valueOf(0));

            Window queryWindow = windowService.getOne(Wrappers.<Window>lambdaQuery().eq(Window::getWindowId, window.getWindowId()));
            if (queryWindow != null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "窗口ID已存在");
                return jsonObject;
            }
            boolean result = windowService.save(window);
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
            logger.error("/window/insert 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "添加出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(Window window) {
        JSONObject jsonObject = new JSONObject();

        try {

            Window queryWindow = windowService.getById(window.getWindowHiddenId());
            if (queryWindow == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "窗口不存在");
                return jsonObject;
            }
            queryWindow = windowService.getOne(Wrappers.<Window>lambdaQuery().eq(Window::getWindowId, window.getWindowId()));

            // 如果根据服务器名查询到的非空且其hiddenId与传入的hiddenId不一致，则表明服务器名已存在于另一项
            if (queryWindow != null && !queryWindow.getWindowHiddenId().equals(window.getWindowHiddenId())) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "窗口名已存在");
                return jsonObject;
            }
            boolean result;
            if (window.getFloorHiddenId() != null && window.getCamHiddenId() != null) {
                result = windowService.update(window, Wrappers.<Window>lambdaUpdate().eq(Window::getWindowHiddenId, window.getWindowHiddenId()));
            }
            else if (window.getCamHiddenId() != null) {
                result = windowService.update(window, Wrappers.<Window>lambdaUpdate().set(Window::getFloorHiddenId, null).eq(Window::getWindowHiddenId, window.getWindowHiddenId()));
            }
            else if (window.getFloorHiddenId() != null) {
                result = windowService.update(window, Wrappers.<Window>lambdaUpdate().set(Window::getCamHiddenId, null).eq(Window::getWindowHiddenId, window.getWindowHiddenId()));
            }
            else {
                result = windowService.update(window, Wrappers.<Window>lambdaUpdate().set(Window::getFloorHiddenId, null)
                        .set(Window::getCamHiddenId, null).eq(Window::getWindowHiddenId, window.getWindowHiddenId()));
            }


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
            logger.error("/window/update 错误:" + e.getMessage(), e);
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
            for (int i = 0; i < deletes.length; i++) {
                windowService.deletePreHiddenId(Long.valueOf(deletes[i]));
                windowService.removeById(Long.valueOf(deletes[i]));

            }
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "删除成功");
            return jsonObject;

        } catch (Exception e) {
            logger.error("/window/delete 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "删除出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
    public Object getOne(Long windowHiddenId) {
        JSONObject jsonObject = new JSONObject();

        try {
            Window window = windowService.getWindowByHiddenId(windowHiddenId);
            if (window != null) {
                jsonObject.put("result", "ok");
                jsonObject.put("window", window);
                jsonObject.put("msg", "获取成功");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "获取失败");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/window/getOne 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/queryData", produces = "application/json;charset=utf-8")
    public Object search(@RequestParam(required = false) String windowId,
                         @RequestParam(required = false) String windowName,
                         @RequestParam(required = false) String windowDepartment, int pageNo, int pageSize) {
        JSONObject jsonObject = new JSONObject();

        try {
            // 分页构造器
            Page<Window> page = new Page<Window>(pageNo, pageSize);
            QueryWrapper<Window> queryWrapper = new QueryWrapper<Window>();
            if (windowId != null)
                queryWrapper.like ("window_id",windowId);
            if (windowName != null)
                queryWrapper.like ("window_name",windowName);
            if (windowDepartment != null)
                queryWrapper.like ("window_department",windowDepartment);

            // 执行分页
            IPage<Window> pageList = windowService.page(page, queryWrapper);
            // 返回结果
            if (pageList.getTotal() <= 0) {
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
            logger.error("/window/queryData 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "搜索出现错误");
            return jsonObject;
        }
    }

}
