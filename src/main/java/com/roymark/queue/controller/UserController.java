package com.roymark.queue.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.ActionUser;
import com.roymark.queue.entity.FaceVector;
import com.roymark.queue.service.AnomalyService;
import com.roymark.queue.service.FaceVectorService;
import com.roymark.queue.service.ParameterService;
import com.roymark.queue.util.HttpUtil;
import com.roymark.queue.util.ParamUtil;
import com.roymark.queue.util.web.HttpUtils;
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
				tempActionUser.setUserHiddenId((long)0);
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

			// 处理发送地址
			String ip_address = ParamUtil.getParamValueByName("face_server_ip");
			String port = ParamUtil.getParamValueByName("face_manager_port");
			if (ip_address.equals("") || port.equals("")) {
				jsonObject.put("msg", "人脸服务器配置错误，请检查参数face_server_ip与face_manager_port！");
				jsonObject.put("result", "no");
				return jsonObject;
			}
			String host = "http://" + ip_address + ":" + port;
			boolean connectResult = HttpUtils.isReachable(ip_address, port, 3000);
			if (!connectResult) {
				jsonObject.put("msg", "人脸服务器连接失败，请检查参数face_server_ip与face_manager_port！");
				jsonObject.put("result", "no");
				return jsonObject;
			}
			String path = "/insertFaceImage";
			requestParams.add("image", uploadinfo.getResource());

			try {
				ResponseEntity<String> response = HttpUtil.sendPost(host+path, requestParams, new HashMap<>());
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
					tempActionUser.setUserHiddenId(0L);
					String filePath = "";
					String uploadPath = "/uploads/user/";
					filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
					tempActionUser.setUserPhoto(filePath);
						// System.out.println(filePath);

					boolean result = userService.save(tempActionUser);
					if (!result) {
						jsonObject.put("result", "no");
						jsonObject.put("msg", "数据库添加失败");
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
					jsonObject.put("msg", "人脸服务器连接失败，请检查参数face_server_ip与face_manager_port！");
					jsonObject.put("result", "no");
					return jsonObject;
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				jsonObject.put("msg", "人脸服务器连接失败，请检查参数face_server_ip与face_manager_port！");
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
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

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
				Long userHiddenId = Long.valueOf(deletes[i]);
				ActionUser queryUser = userService.getById(userHiddenId);
				List<FaceVector> faceVectors = faceVectorService.list(Wrappers.<FaceVector>lambdaQuery().eq(FaceVector::getUserHiddenId, userHiddenId));
				StringBuilder msg = new StringBuilder();

				boolean deleteUserFlag = true;
				for (FaceVector faceVector: faceVectors) {
					String faceId = faceVector.getFaceId();
					MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
					requestParams.add("faceId", faceId);
					// 处理发送地址
					String ip_address = ParamUtil.getParamValueByName("face_server_ip");
					String port = ParamUtil.getParamValueByName("face_manager_port");
					if (ip_address.equals("") || port.equals("")) {
						jsonObject.put("msg", "人脸服务器配置错误，请检查参数face_server_ip与face_manager_port！");
						jsonObject.put("result", "no");
						return jsonObject;
					}
					String host = "http://" + ip_address + ":" + port;
					boolean connectResult = HttpUtils.isReachable(ip_address, port, 3000);
					if (!connectResult) {
						jsonObject.put("msg", "人脸服务器连接失败，请检查参数face_server_ip与face_manager_port！");
						jsonObject.put("result", "no");
						return jsonObject;
					}
					String path = "/deleteFaceImage";
					try {
						ResponseEntity<String> response = HttpUtil.sendPost(host+path, requestParams, new HashMap<>());
						if(response.getStatusCodeValue() == 200) {
							HashMap hashMap = JSON.parseObject(response.getBody(), HashMap.class);
							if (hashMap.get("status").equals("False")) {
								msg.append(hashMap.get("info")).append("\n");
								deleteUserFlag = false;
							}
							// 服务器删除成功才允许删除
							else {
								// 删除人脸向量以及向服务器删除
								faceVectorService.removeById(faceVector.getFaceVectorId());
								String imgPath = queryUser.getUserPhoto();
								if (imgPath != null && !imgPath.equals("")) {
									String[] imgPaths = imgPath.split(",");
									// 删除图片
									for (String curImgPath: imgPaths) {
										UploadUtil.fileDelete(request, curImgPath);
									}
								}
							}
						}
						else {
							msg.append("服务器返回状态异常,").append(queryUser.getUserId()).append("禁止删除");
							deleteUserFlag = false;
						}
					}catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
				if (deleteUserFlag) {
					userService.removeById(userHiddenId);
				}
			}
			jsonObject.put("result", "ok");
			jsonObject.put("msg", "删除成功");
			return jsonObject;

		} catch (Exception e) {
			logger.error("/user/delete 错误:" + e.getMessage(), e);
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
	public Object insertFace(Long userHiddenId,
							 @RequestParam(value = "uploadinfo", required = false) MultipartFile uploadinfo) {
		JSONObject jsonObject = new JSONObject();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		if (uploadinfo == null) {
			jsonObject.put("result", "no");
			jsonObject.put("msg", "未选中图片");
			return jsonObject;
		}
		try {
			ActionUser queryUser = userService.getById(userHiddenId);
			if (queryUser == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "用户不存在");
				return jsonObject;
			}
			// 处理发送地址
			String ip_address = ParamUtil.getParamValueByName("face_server_ip");
			String port = ParamUtil.getParamValueByName("face_manager_port");
			if (ip_address.equals("") || port.equals("")) {
				jsonObject.put("msg", "人脸服务器配置错误，请检查参数face_server_ip与face_manager_port！");
				jsonObject.put("result", "no");
				return jsonObject;
			}
			String host = "http://" + ip_address + ":" + port;
			boolean connectResult = HttpUtils.isReachable(ip_address, port, 3000);
			String path = "/insertFaceImage";
			if (!connectResult) {
				jsonObject.put("msg", "人脸服务器连接失败，请检查参数face_server_ip与face_manager_port！");
				jsonObject.put("result", "no");
				return jsonObject;
			}
			String url = host+path;

			MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
			requestParams.add("image", uploadinfo.getResource());
			String existPath = queryUser.getUserPhoto();
			if (existPath!=null && !existPath.equals("")) {
				String[] existPaths = existPath.split(",");
				if (existPaths.length >= 9) {
					jsonObject.put("msg", "图片数量不能超过10个");
					jsonObject.put("result", "no");
					return jsonObject;
				}
			}

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

					String uploadPath = "/uploads/user/";
					String filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
					if (existPath == null || existPath.equals("")) {
						queryUser.setUserPhoto(filePath);
					}
					else {
						queryUser.setUserPhoto(existPath+","+filePath);
					}

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
					jsonObject.put("msg", "人脸服务器连接失败，请检查参数face_server_ip与face_manager_port！");
					jsonObject.put("result", "no");
					return jsonObject;
				}
			} catch (Exception e) {
				logger.error(e);
				jsonObject.put("msg", "人脸服务器连接失败，请检查参数face_server_ip与face_manager_port！");
				jsonObject.put("result", "no");
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("/user/insertFace 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "添加人脸出现错误");
			return jsonObject;
		}
	}

	@RequestMapping(value = "/deleteFace", produces = "application/json;charset=utf-8")
	public Object deleteFace(Long userHiddenId, String imgPath) {
		JSONObject jsonObject = new JSONObject();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		try {
			if (imgPath == null || imgPath.equals("")) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "图片路径为空");
				return jsonObject;
			}
			ActionUser queryUser = userService.getById(userHiddenId);

			if (queryUser == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "用户不存在");
				return jsonObject;
			}
			// 获得现有的imgPath
			String curImgPath = queryUser.getUserPhoto();

			if (curImgPath==null || curImgPath.equals("")) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "用户没有照片");
				return jsonObject;
			}
			String[] curImgPaths = curImgPath.split(",");

			StringBuilder newImgPath = new StringBuilder();

			// 确定已有的path中是否包含并构造新path
			boolean existFlag = false;
			for (String path: curImgPaths) {
				if (path.equals(imgPath)) {
					existFlag = true;
					continue;
				}
				newImgPath.append(path).append(",");
			}
			if (!existFlag) {
				jsonObject.put("msg", "图片不存在");
				jsonObject.put("result", "no");
				return jsonObject;
			}

			// 对用户进行修改
			if (newImgPath.length() > 0)								// 如果新路径不为空，去掉最后一个,号
				newImgPath.deleteCharAt(newImgPath.length()-1);
			queryUser.setUserPhoto(newImgPath.toString());

			// 对人脸向量表进行处理
			FaceVector queryFaceVector = faceVectorService.getOne(Wrappers.<FaceVector>lambdaQuery()
					.eq(FaceVector::getImgPath, imgPath).eq(FaceVector::getUserHiddenId, userHiddenId));

			if (queryFaceVector != null) {
				String faceId = queryFaceVector.getFaceId();
				faceVectorService.removeById(queryFaceVector.getFaceVectorId());
				MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
				requestParams.add("faceId", faceId);
				// 处理发送地址
				String ip_address = ParamUtil.getParamValueByName("face_server_ip");
				String port = ParamUtil.getParamValueByName("face_manager_port");
				if (ip_address.equals("") || port.equals("")) {
					jsonObject.put("msg", "人脸服务器配置错误，请检查参数face_server_ip与face_manager_port！");
					jsonObject.put("result", "no");
					return jsonObject;
				}
				String host = "http://" + ip_address + ":" + port;
				boolean connectResult = HttpUtils.isReachable(ip_address, port, 3000);
				if (!connectResult) {
					jsonObject.put("msg", "人脸服务器连接失败，请检查参数face_server_ip与face_manager_port！");
					jsonObject.put("result", "no");
					return jsonObject;
				}
				String path = "/deleteFaceImage";
				String url = host+path;
				try {
					ResponseEntity<String> response = HttpUtil.sendPost(url, requestParams, new HashMap<>());
					if(response.getStatusCodeValue() == 200) {
						HashMap hashMap = JSON.parseObject(response.getBody(), HashMap.class);
						if (hashMap.get("status").equals("False")) {
							jsonObject.put("msg", hashMap.get("info"));
							jsonObject.put("result", "no");
							return jsonObject;
						}
					}
					else {
						jsonObject.put("msg", "人脸服务器连接状态异常，请检查参数face_server_ip与face_manager_port！");
						jsonObject.put("result", "no");
						return jsonObject;
					}
				}catch (Exception e) {
					logger.error(e.getMessage());
					jsonObject.put("msg", "人脸服务器连接失败，请检查参数face_server_ip与face_manager_port！");
					jsonObject.put("result", "no");
					return jsonObject;
				}
			}
			// 只有服务器删除成功或人脸向量表中无该项才会到达此处
			if (!UploadUtil.fileDelete(request, imgPath)) {
				jsonObject.put("msg", "删除文件资源失败");
				jsonObject.put("result", "no");
				return jsonObject;
			}
			if (!userService.saveOrUpdate(queryUser)) {
				jsonObject.put("msg", "修改数据失败");
				jsonObject.put("result", "no");
				return jsonObject;
			}
			jsonObject.put("msg", "删除成功");
			jsonObject.put("result", "ok");
			return jsonObject;
		} catch (Exception e) {
			logger.error("/user/deleteFace 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "删除人脸出现错误");
			return jsonObject;
		}
	}
}

