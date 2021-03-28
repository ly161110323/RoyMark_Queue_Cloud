package com.roymark.queue.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
            jsonObject.put("params", params);
            jsonObject.put("result", "ok");
            return jsonObject;
        } catch (Exception e) {
            logger.error("/param/getAllParams 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(Parameter parameter) {
        JSONObject jsonObject = new JSONObject();

        try {
            boolean result = parameterService.save(parameter);
            if (result) {
                jsonObject.put("result", "ok");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/param/insert 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(Parameter parameter) {
        JSONObject jsonObject = new JSONObject();

        try {
            boolean result = parameterService.update(parameter, Wrappers.<Parameter>lambdaUpdate().eq(Parameter::getParamHiddenId, parameter.getParamHiddenId()));
            if (result) {
                jsonObject.put("result", "ok");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/param/update 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object delete(Long paramId) {
        JSONObject jsonObject = new JSONObject();

        try {
            boolean result = parameterService.removeById(paramId);
            if (result) {
                jsonObject.put("result", "ok");
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/param/delete 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
    public Object getOne(Long parameterId) {
        JSONObject jsonObject = new JSONObject();

        try {
            Parameter parameter = parameterService.getById(parameterId);
            if (parameter != null) {
                jsonObject.put("result", "ok");
                jsonObject.put("param", parameter);
                return jsonObject;
            }
            else {
                jsonObject.put("result", "no");
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("/param/getOne 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            return jsonObject;
        }
    }
}
