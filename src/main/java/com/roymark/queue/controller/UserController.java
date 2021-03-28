package com.roymark.queue.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.roymark.queue.entity.ActionUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.roymark.queue.service.UserService;
import com.roymark.queue.util.Md5Util;
import com.roymark.queue.util.UploadUtil;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LogManager.getLogger(UserController.class);
    
	@Autowired
    private UserService userSerivce;

	
	@RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
	public Object update(ActionUser tempActionUser,
						 @RequestParam(value = "uploadinfo", required = false) MultipartFile uploadinfo) {
		JSONObject jsonObject = new JSONObject();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		// 进行修改操作
		try {
			
			String filePath = "";
			if (uploadinfo != null) {
				// 上传文件
				String uploadPath = "/RemoteQueue/upload/user/";
				filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
			}
			tempActionUser.setUserPhoto(filePath);
			
			// 对密码进行MD5加密
			if (tempActionUser.getUserPwd() != null) {
				tempActionUser.setUserPwd(Md5Util.EncoderByMd5(tempActionUser.getUserPwd()));
			}
			boolean result = userSerivce.update(tempActionUser, Wrappers.<ActionUser>lambdaUpdate().eq(ActionUser::getUserHiddenId, tempActionUser.getUserHiddenId()));
			if (result) {
				jsonObject.put("result", "ok");
			} else {
				jsonObject.put("result", "no");
			}
			
		} catch (Exception e) {
			logger.error("/user/update错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
		}
		return jsonObject;

	}
	
	@RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
	public Object insert(ActionUser tempActionUser,
			@RequestParam(value = "uploadinfo", required = false) MultipartFile uploadinfo) {
		JSONObject jsonObject = new JSONObject();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		
		try {
			List<ActionUser> existActionUsers = userSerivce.list();
					
			int repeatId = 0;
				for (ActionUser actionUser : existActionUsers) {
					if (actionUser.getUserId().equals(tempActionUser.getUserId())) {
						repeatId++;
					}
				}
				if (repeatId > 0) {
					jsonObject.put("result", "repeat");
					return jsonObject;
				} else {
					String filePath = "";
					if (uploadinfo != null) {
						// 上传文件
						String uploadPath = "/RemoteQueue/upload/user/";
						filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
					}
					tempActionUser.setUserPhoto(filePath);
					
					// 对密码进行MD5加密
					if (tempActionUser.getUserPwd() != null) {
						tempActionUser.setUserPwd(Md5Util.EncoderByMd5(tempActionUser.getUserPwd()));
					}

					boolean result = userSerivce.save(tempActionUser);
					if (result) {
						jsonObject.put("result", "ok");
						return jsonObject;
					} else {
						jsonObject.put("result", "no");
						return jsonObject;
					}
				}
			
		} catch (Exception e) {
			logger.error("/user/insert 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/getUsers", produces = "application/json;charset=utf-8")
	public Object getAllUsers() {
		JSONObject jsonObject = new JSONObject();
		try {
			List<ActionUser> actionUsers = userSerivce.list();
			jsonObject.put("actionUsers", actionUsers);
			jsonObject.put("result", "ok");
			return jsonObject;
		} catch (Exception e) {
			logger.error("/user/getUsers 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
	public Object delete(ActionUser tempActionUser) {
		JSONObject jsonObject = new JSONObject();
		try {
			boolean result = userSerivce.removeById(tempActionUser.getUserHiddenId());
			if (result) {
				jsonObject.put("result", "ok");
				return jsonObject;
			} else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/user/getUsers 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
	public Object getOne(Long userId) {
		JSONObject jsonObject = new JSONObject();

		try {
			ActionUser user = userSerivce.getById(userId);
			if (user != null) {
				jsonObject.put("result", "ok");
				jsonObject.put("user", user);
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/user/getOne 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}
}

