package com.roymark.queue.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.Anomaly;
import com.roymark.queue.entity.ActionUser;
import com.roymark.queue.entity.FaceVector;
import com.roymark.queue.entity.Parameter;
import com.roymark.queue.service.AnomalyService;
import com.roymark.queue.service.FaceVectorService;
import com.roymark.queue.service.ParameterService;
import com.roymark.queue.util.HttpUtil;
import com.roymark.queue.util.web.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.roymark.queue.service.UserService;
import com.roymark.queue.util.UploadUtil;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LogManager.getLogger(UserController.class);
    
	@Autowired
    private UserService userService;

	@Autowired
	private AnomalyService anomalyService;

	@Autowired
	private ParameterService parameterService;

	@Autowired
	private FaceVectorService faceVectorService;
	
	@RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
	public Object update(ActionUser tempActionUser,
						 @RequestParam(value = "uploadinfo", required = false) MultipartFile uploadinfo) {
		JSONObject jsonObject = new JSONObject();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		// 进行修改操作
		try {
			if (tempActionUser.getUserHiddenId() == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "用户不存在");
				return jsonObject;
			}

			ActionUser queryUser = userService.getOne(Wrappers.<ActionUser>lambdaQuery().eq(ActionUser::getUserId, tempActionUser.getUserId()));
			if (queryUser != null && !queryUser.getUserHiddenId().equals(tempActionUser.getUserHiddenId())) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "修改的用户ID已存在");
				return jsonObject;
			}

			String filePath = "";
			if (uploadinfo != null) {
				// 上传文件
				String uploadPath = "/uploads/user/";
				filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
				tempActionUser.setUserPhoto(filePath);
			}
			boolean result;
			if (tempActionUser.getWindowHiddenId() != null) {
				result = userService.update(tempActionUser, Wrappers.<ActionUser>lambdaUpdate().eq(ActionUser::getUserHiddenId, tempActionUser.getUserHiddenId()));
			}
			else {
				result = userService.update(tempActionUser, Wrappers.<ActionUser>lambdaUpdate().set(ActionUser::getWindowHiddenId, null).eq(ActionUser::getUserHiddenId, tempActionUser.getUserHiddenId()));
			}
			if (result) {
				jsonObject.put("result", "ok");
				jsonObject.put("msg", "修改成功");
			} else {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "修改失败");
			}
			
		} catch (Exception e) {
			logger.error("/user/update错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "修改出现错误");
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
			MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();

			List<ActionUser> existActionUsers = userService.getAllUser();
					
			int repeatId = 0;
			for (ActionUser actionUser : existActionUsers) {
				if (actionUser.getUserId().equals(tempActionUser.getUserId())) {
					repeatId++;
				}
			}
			if (repeatId > 0) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "用户ID已存在");
				return jsonObject;
			}

			if (uploadinfo == null) {
				boolean result = userService.save(tempActionUser);
				if (!result) {
					jsonObject.put("result", "no");
					jsonObject.put("msg", "添加失败");
					return jsonObject;
				}
				jsonObject.put("result", "ok");
				jsonObject.put("msg", "添加成功");
				return jsonObject;
			}

			String url = getURLFromDB("/insertFaceImage");
			requestParams.add("image", uploadinfo.getResource());

			try {
				ResponseEntity<String> response = HttpUtil.sendPost(url, requestParams, new HashMap<>());
				// System.out.println(response);
				if(response.getStatusCodeValue() == 200){
					logger.info("向服务器发送图片;");
					HashMap hashMap = JSON.parseObject(response.getBody(), HashMap.class);
					// System.out.println(String.valueOf(hashMap.get("info")));
					// 可能
					if (hashMap.get("status").equals("False")) {
						jsonObject.put("msg", hashMap.get("info"));
						jsonObject.put("result", "no");
						return jsonObject;
					}
					// 添加用户
					tempActionUser.setUserHiddenId(Long.valueOf(0));
					String filePath = "";
					String uploadPath = "/uploads/user/";
					filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
					tempActionUser.setUserPhoto(filePath);
						// System.out.println(filePath);

					boolean result = userService.save(tempActionUser);
					if (!result) {
						jsonObject.put("result", "no");
						jsonObject.put("msg", "添加失败");
						return jsonObject;
					}
					ActionUser queryUser = userService.getOne(Wrappers.<ActionUser>lambdaQuery().eq(ActionUser::getUserId, tempActionUser.getUserId()));

					FaceVector faceVector = new FaceVector();
					faceVector.setFaceVectorId((long)0);
					faceVector.setFaceId(String.valueOf(hashMap.get("info")));
					faceVector.setImgPath(filePath);
					faceVector.setUserHiddenId(queryUser.getUserHiddenId());

					faceVectorService.save(faceVector);
					jsonObject.put("result", "ok");
					jsonObject.put("msg", "添加成功");
					return jsonObject;
				}
				else {
					logger.info("服务器拒绝;");
					jsonObject.put("msg", "向人脸服务器添加失败！请检查人脸服务器配置");
					jsonObject.put("result", "no");
					return jsonObject;
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				jsonObject.put("msg", "向人脸服务器添加失败！请检查人脸服务器配置");
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/user/insert 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "添加出现错误");
			return jsonObject;
		}
	}
	
	@RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
	public Object getAllUsers() {
		JSONObject jsonObject = new JSONObject();
		try {
			List<ActionUser> actionUsers = userService.getAllUser();
			if (actionUsers.size() <= 0) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "获取结果为空");
				jsonObject.put("users", actionUsers);
				return jsonObject;
			}
			jsonObject.put("users", actionUsers);
			jsonObject.put("result", "ok");
			jsonObject.put("msg", "获取成功");
			return jsonObject;
		} catch (Exception e) {
			logger.error("/user/getUsers 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "获取出现错误");
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
				ActionUser actionUser = userService.getById(Long.valueOf(deletes[i]));
				if (actionUser == null) {
					jsonObject.put("result", "error");
					jsonObject.put("msg", "数据不存在");
					return jsonObject;
				}
			}
			for (int i = 0; i < deletes.length; i++) {
				List<Anomaly> anomalyList = anomalyService.list(Wrappers.<Anomaly>lambdaQuery().eq(Anomaly::getUserHiddenId, Long.valueOf(deletes[i])));
				for (Anomaly anomaly : anomalyList) {
					anomalyService.update(null, Wrappers.<Anomaly>lambdaUpdate().set(Anomaly::getUserHiddenId, null)
							.eq(Anomaly::getAnomalyHiddenId, anomaly.getAnomalyHiddenId()));
				}
				userService.removeById(Long.valueOf(deletes[i]));
			}
			jsonObject.put("result", "ok");
			jsonObject.put("msg", "删除成功");
			return jsonObject;

		} catch (Exception e) {
			logger.error("/camera/delete 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "删除出现错误");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/getOne", produces = "application/json;charset=utf-8")
	public Object getOne(Long userHiddenId) {
		JSONObject jsonObject = new JSONObject();

		try {
			ActionUser user = userService.getUserByHiddenId(userHiddenId);
			if (user != null) {
				jsonObject.put("result", "ok");
				jsonObject.put("user", user);
				jsonObject.put("msg", "获取成功");
				return jsonObject;
			}
			else {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "获取失败");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/user/getOne 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "获取出现错误");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/queryData", produces = "application/json;charset=utf-8")
	public Object search(@RequestParam(required = false) String userName, @RequestParam(required = false) String userId,
			@RequestParam(required = false) String userDepartment, int pageNo, int pageSize) {
		JSONObject jsonObject = new JSONObject();

		try {
			// 分页构造器
			Page<ActionUser> page = new Page<ActionUser>(pageNo, pageSize);
			QueryWrapper<ActionUser> queryWrapper = new QueryWrapper<ActionUser>();
			if (userName != null) {
				queryWrapper.like ("user_name",userName);
			}
			if (userId != null) {
				queryWrapper.like("user_id", userId);
			}
			if (userDepartment != null) {
				queryWrapper.like("user_department", userDepartment);
			}

			queryWrapper.orderByAsc("user_id");
			// 执行分页
			IPage<ActionUser> pageList = userService.page(page, queryWrapper);
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
			logger.error("/user/queryData 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "搜索出现错误");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/insertFace", produces = "application/json;charset=utf-8")
	public Object insertFace(Long userHiddenId, @RequestParam(value = "uploadinfo") MultipartFile uploadinfo) {
		JSONObject jsonObject = new JSONObject();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		try {
			if (uploadinfo == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "上传图片有误，请重新上传");
				return jsonObject;
			}
			ActionUser queryUser = userService.getById(userHiddenId);
			if (queryUser == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "用户不存在");
				return jsonObject;
			}
			// 处理发送地址
			String url = getURLFromDB("/insertFaceImage");
			if (url.equals("")) {
				jsonObject.put("msg", "人脸服务器参数有误！请检查人脸服务器配置");
				jsonObject.put("result", "no");
				return jsonObject;
			}

			MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();

			requestParams.add("image", uploadinfo.getResource());

			try {
				ResponseEntity<String> response = HttpUtil.sendPost(url, requestParams, new HashMap<>());
				// System.out.println(response);
				if(response.getStatusCodeValue() == 200){
					logger.info("向服务器发送图片;");
					HashMap hashMap = JSON.parseObject(response.getBody(), HashMap.class);
					if (hashMap.get("status").equals("False")) {
						jsonObject.put("msg", hashMap.get("info"));
						jsonObject.put("result", "no");
						return jsonObject;
					}
					String filePath = "";
					String uploadPath = "/uploads/user/";
					filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
					String existPath = queryUser.getUserPhoto();
					if (existPath==null || existPath.equals(""))
						queryUser.setUserPhoto(filePath);
					else
						queryUser.setUserPhoto(existPath+","+filePath);

					userService.saveOrUpdate(queryUser);
					FaceVector faceVector = new FaceVector();
					faceVector.setFaceVectorId((long)0);
					faceVector.setFaceId(String.valueOf(hashMap.get("info")));
					faceVector.setImgPath(filePath);
					faceVector.setUserHiddenId(userHiddenId);

					faceVectorService.save(faceVector);
					jsonObject.put("result", "ok");
					jsonObject.put("msg", "添加成功");
					return jsonObject;
				}
				else {
					logger.info("服务器拒绝;");
					jsonObject.put("msg", "向人脸服务器添加失败！请检查人脸服务器配置");
					jsonObject.put("result", "no");
					return jsonObject;
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				jsonObject.put("msg", "向人脸服务器添加失败！请检查人脸服务器配置");
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/user/insertFace 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "添加出现错误");
			return jsonObject;
		}
	}

	public String getURLFromDB(String path) {
		// 处理发送地址
		Parameter faceServerIp = parameterService.getOne(Wrappers.<Parameter>lambdaQuery().eq(Parameter::getParamName, "face_server_ip"));
		Parameter faceServerPort = parameterService.getOne(Wrappers.<Parameter>lambdaQuery().eq(Parameter::getParamName, "face_server_port"));
		if (faceServerIp == null || faceServerPort == null) {
			return "";
		}
		String host = "http://";
		if (!faceServerIp.getParamValue().equals("")) {
			host += faceServerIp.getParamValue();
		}
		else if (!faceServerIp.getParamDefault().equals("")) {
			host += faceServerIp.getParamDefault();
		}
		else {
			return "";
		}
		host += ":";
		if (!faceServerPort.getParamValue().equals("")) {
			host += faceServerPort.getParamValue();
		}
		else if (!faceServerPort.getParamDefault().equals("")) {
			host += faceServerPort.getParamDefault();
		}
		else {
			return "";
		}
		return host + path;
	}
}

