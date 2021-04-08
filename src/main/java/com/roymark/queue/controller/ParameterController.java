package com.roymark.queue.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.Parameter;
import com.roymark.queue.service.ParameterService;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/param")
public class ParameterController {
    private static final Logger logger = LogManager.getLogger(ParameterController.class);

    @Autowired
    private ParameterService parameterService;

    @RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
    public Object getAllParams() {
        JSONObject jsonObject = new JSONObject();

        try {
            List<Parameter> params = parameterService.list();
            if (params.size() <= 0) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "获取结果为空");
                jsonObject.put("params", params);
                return jsonObject;
            }
            jsonObject.put("params", params);
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "获取成功");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/param/getAllParams 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(Parameter parameter) {
        JSONObject jsonObject = new JSONObject();

        try {
            parameter.setParamHiddenId(Long.valueOf(0));
            Parameter queryParameter = parameterService.getOne(Wrappers.<Parameter>lambdaQuery().eq(Parameter::getParamId, parameter.getParamId()));

            if (queryParameter != null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "参数ID已存在");
            }

            boolean result = parameterService.save(parameter);
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
            logger.error("/param/insert 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "添加出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(Parameter parameter) {
        JSONObject jsonObject = new JSONObject();

        try {
            Parameter queryParameter = parameterService.getById(parameter.getParamHiddenId());
            if (queryParameter == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "参数不存在");
                return jsonObject;
            }

            queryParameter = parameterService.getOne(Wrappers.<Parameter>lambdaQuery().eq(Parameter::getParamId, parameter.getParamId()));
            if (queryParameter != null && !queryParameter.getParamHiddenId().equals(parameter.getParamHiddenId())) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "参数ID已存在");
                return jsonObject;
            }

            boolean result = parameterService.update(parameter, Wrappers.<Parameter>lambdaUpdate().eq(Parameter::getParamHiddenId, parameter.getParamHiddenId()));
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
            logger.error("/param/update 错误:" + e.getMessage(), e);
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
                // 首先删除所有关联窗口
                Long deleteFloorHiddenId = Long.valueOf(deletes[i]);
                parameterService.removeById(deleteFloorHiddenId);
            }
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "删除成功");
            return jsonObject;

        } catch (Exception e) {
            logger.error("/param/delete 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "删除出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
    public Object getOne(Long paramHiddenId) {
        JSONObject jsonObject = new JSONObject();

        try {
            Parameter parameter = parameterService.getById(paramHiddenId);
            if (parameter != null) {
                jsonObject.put("result", "ok");
                jsonObject.put("param", parameter);
                jsonObject.put("msg", "获取成功");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "获取失败");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/param/getOne 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "获取出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/queryData", produces = "application/json;charset=utf-8")
    public Object searchByServerId(String paramName, int pageNo, int pageSize) {
        JSONObject jsonObject = new JSONObject();

        try {
            // 分页构造器
            Page<Parameter> page = new Page<Parameter>(pageNo, pageSize);
            QueryWrapper<Parameter> queryWrapper = new QueryWrapper<Parameter>();

            queryWrapper.like ("param_name",paramName);
            // 执行分页
            IPage<Parameter> pageList = parameterService.page(page, queryWrapper);
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
            logger.error("/param/queryData 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "搜索出现错误");
            return jsonObject;
        }
    }

}
