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
        }
        else if (parameter.getParamValue()!=null && !parameter.getParamValue().equals("")) {
            return parameter.getParamValue();
        }
        else if (parameter.getParamDefault()!=null && !parameter.getParamDefault().equals("")) {
            return parameter.getParamDefault();
        }
        return "";
    }

    // -1代表格式不正确，0代表不在工作时间，1代表在工作时间
    public static int checkForWorkOut() {
        Date date = new Date();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String workTimeStr = getParamValueByName("work_time");
        // 缺省代表无限制
        if (workTimeStr.equals("")) {
            return 1;
        }
        else {
            String[] workTimeArray = workTimeStr.split(",");
            for (String workTimeInterval: workTimeArray) {
                String[] startAndEnd = workTimeInterval.split("-");
                // 格式检查 startTime-endTime
                if (startAndEnd.length != 2) {
                    return -1;
                }
                else {
                    String startTime = dateStr + " " + startAndEnd[0] + ":00";
                    String endTime = dateStr + " " + startAndEnd[1] + ":00";
                    SimpleDateFormat checkFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        checkFormat.setLenient(false);
                        Date startDate = checkFormat.parse(startTime);
                        Date endDate = checkFormat.parse(endTime);
                        if (date.after(startDate) && date.before(endDate)) {
                            return 1;
                        }
                    } catch (Exception e) {
                        log.error("ParamUtil.checkForWorkOut:", e);
                        return -1;
                    }
                }
            }
            return 0;
        }

    }
}
