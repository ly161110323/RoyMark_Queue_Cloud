package com.roymark.queue.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.SmsContact;
import com.roymark.queue.service.SmsContactService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/smsContact")
public class SmsContactController {
    private static final Logger logger = LogManager.getLogger(SmsContactController.class);

    @Autowired
    private SmsContactService smsContactService;

    @RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
    public Object insert(SmsContact smsContact) {
        JSONObject jsonObject = new JSONObject();

        try {
            smsContact.setSmsContactId(0L);

            SmsContact querySmsContact = smsContactService.getOne(Wrappers.<SmsContact>lambdaQuery().eq(SmsContact::getSmsContactPhone, smsContact.getSmsContactPhone()));
            if (querySmsContact != null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "联系人号码已存在");
                return jsonObject;
            }
            boolean result = smsContactService.save(smsContact);

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
            logger.error("/smsContact/insert 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "添加出现错误");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object update(SmsContact smsContact) {
        JSONObject jsonObject = new JSONObject();

        try {
            SmsContact querySmsContact = smsContactService.getById(smsContact.getSmsContactId());

            if (querySmsContact == null) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "联系人不存在");
                return jsonObject;
            }
            if (querySmsContact.getSmsContactName().equals("") || querySmsContact.getSmsContactPhone().equals("")) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "联系人名字和号码不能为空");
                return jsonObject;
            }
            querySmsContact = smsContactService.getOne(Wrappers.<SmsContact>lambdaQuery().eq(SmsContact::getSmsContactPhone, smsContact.getSmsContactPhone()));
            if (querySmsContact != null && !querySmsContact.getSmsContactId().equals(smsContact.getSmsContactId())) {
                jsonObject.put("result", "no");
                jsonObject.put("msg", "联系人号码已存在");
                return jsonObject;
            }

            boolean result = smsContactService.update(smsContact, Wrappers.<SmsContact>lambdaUpdate().eq(SmsContact::getSmsContactId, smsContact.getSmsContactId()));
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
            logger.error("/smsContact/update 错误:" + e.getMessage(), e);
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
            for (String delete : deletes) {
                SmsContact smsContact = smsContactService.getById(Long.valueOf(delete));
                if (smsContact == null) {
                    jsonObject.put("result", "error");
                    jsonObject.put("msg", "数据不存在");
                    return jsonObject;
                }
            }
            for (String delete : deletes) {
                Long deleteGroupHiddenId = Long.valueOf(delete);
                smsContactService.removeById(deleteGroupHiddenId);
            }
            jsonObject.put("result", "ok");
            jsonObject.put("msg", "删除成功");
            return jsonObject;

        } catch (Exception e) {
            logger.error("/smsContact/delete 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "删除出现错误");
            return jsonObject;
        }
    }


    @RequestMapping(value = "/queryData", produces = "application/json;charset=utf-8")
    public Object search(@RequestParam(required = false) String name,
                         @RequestParam(required = false) String phone,
                         int pageNo, int pageSize) {
        JSONObject jsonObject = new JSONObject();

        try {
            // 分页构造器
            Page<SmsContact> page = new Page<>(pageNo, pageSize);
            LambdaQueryWrapper<SmsContact> queryWrapper = new LambdaQueryWrapper<>();
            if (name != null && !name.equals("")) {
                queryWrapper.like(SmsContact::getSmsContactName, name);
            }
            if (phone != null && !phone.equals("")) {
                queryWrapper.like(SmsContact::getSmsContactPhone, phone);
            }
            queryWrapper.orderByAsc(SmsContact::getSmsContactName);

            // 执行分页
            IPage<SmsContact> pageList = smsContactService.page(page, queryWrapper);
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
            logger.error("/smsContact/queryData 错误:" + e.getMessage(), e);
            jsonObject.put("result", "error");
            jsonObject.put("msg", "搜索出现错误");
            return jsonObject;
        }
    }
}
