package com.roymark.queue.controller;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.roymark.queue.entity.User;
import com.roymark.queue.service.UserService;
import com.roymark.queue.util.GetDataLs;
import com.roymark.queue.util.GetLoginIp;
import com.roymark.queue.util.Md5Util;
import com.roymark.queue.util.UploadUtil;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LogManager.getLogger(QueueAreaController.class);
    
	@Autowired
    private UserService userSerivce;
	
	@RequestMapping("/login")
	public Object login(String loginId, String pwd) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			HttpSession session = request.getSession();
			
			User user = new User();
			user.setId(loginId);
			QueryWrapper<User> queryWrapper =  new QueryWrapper<User>(user);
			user = userSerivce.getOne(queryWrapper);
			
			if (user != null) {
				if (Md5Util.checkpassword(pwd, user.getPwd())) {
					session.setAttribute("LOGIN_USER", user);
					// 取得登陆客户端IP
					String loginIp = GetLoginIp.getIpAddr(request);
					session.setAttribute("LOGIN_CLIENT", loginIp);
					jsonObject.put("result", "success");
					jsonObject.put("msg", "");
					return jsonObject;
				}
				else {
					jsonObject.put("result", "error");
					jsonObject.put("msg", "密码错误!");
					return jsonObject;
				}
			}
			else {
				jsonObject.put("result", "error");
				jsonObject.put("msg", "该用户不存在!");
				return jsonObject;
			}
			
			// 保存客户端登陆IP纪录
//          int count = loginServies.loginUserHistory(loginId);
//          if (count == 0) {
//              loginServies.addLoginUserHistory(loginId,
//                      (String) session.getAttribute("LOGIN_CLIENT"));
//          } else {
//              loginServies.updLoginUserHistory(loginId,
//                      (String) session.getAttribute("LOGIN_CLIENT"));
//          }
			
		} catch (Exception e) {
			logger.error("/user/login 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", e.getMessage());
			return jsonObject;
		}
	}
	
	@RequestMapping("/logout")
	public String logout() {
		try {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			HttpSession session = request.getSession();
			Enumeration em = request.getSession().getAttributeNames(); // 得到session中所有的属性名
			while (em.hasMoreElements()) {
				session.removeAttribute(em.nextElement().toString()); // 遍历删除session中的值
			}
			session.invalidate();
			return "success";
		} catch (Exception e) {
			logger.error("/user/logout 错误:" + e.getMessage(), e);
			return "error";
		}
	}
	
	@RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
	public Object update(User tempUser, @RequestParam("updateName") String updateName,
			@RequestParam("updateNo") String updateNo,
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
			tempUser.setPhoto(filePath);
			
			// 对密码进行MD5加密
			if (tempUser.getPwd() != null) {
				tempUser.setPwd(Md5Util.EncoderByMd5(tempUser.getPwd()));
			}
			boolean result = userSerivce.update(tempUser, new QueryWrapper<User>(tempUser));
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
	public Object insert(User tempUser,
			@RequestParam(value = "uploadinfo", required = false) MultipartFile uploadinfo) {
		JSONObject jsonObject = new JSONObject();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		
		try {
			List<User> existUsers = userSerivce.list();
					
			int repeatId = 0;
				for (User user : existUsers) {
					if (user.getId().equals(tempUser.getId())) {
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
					tempUser.setPhoto(filePath);
					
					// 对密码进行MD5加密
					if (tempUser.getPwd() != null) {
						tempUser.setPwd(Md5Util.EncoderByMd5(tempUser.getPwd()));
					}

					boolean result = userSerivce.save(tempUser);
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
			List<User> users = userSerivce.list();
			jsonObject.put("users", users);
			jsonObject.put("result", "ok");
			return jsonObject;
		} catch (Exception e) {
			logger.error("/user/getUsers 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
	public Object delete(User tempUser) {
		JSONObject jsonObject = new JSONObject();
		try {
			boolean result = userSerivce.removeById(tempUser.getId());
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
	
}

