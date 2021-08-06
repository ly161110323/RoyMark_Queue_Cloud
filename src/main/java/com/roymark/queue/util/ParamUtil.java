package com.roymark.queue.util;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.roymark.queue.entity.Parameter;
import com.roymark.queue.service.ParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class ParamUtil {
    @Autowired
    ParameterService parameterService;

    public static ParamUtil paramUtil;

    @PostConstruct
    public void init() {
        paramUtil = this;
    }

    // 根据名称获取参数值
    public static String getParamValueByName(String name) {
        // 调用service的方法
        Parameter parameter = paramUtil.parameterService.getOne(Wrappers.<Parameter>lambdaQuery().eq(Parameter::getParamName, name));
        if (parameter == null) {
            return "";
        } else if (parameter.getParamValue() != null && !parameter.getParamValue().equals("")) {
            return parameter.getParamValue();
        } else if (parameter.getParamDefault() != null && !parameter.getParamDefault().equals("")) {
            return parameter.getParamDefault();
        }
        return "";
    }



}
