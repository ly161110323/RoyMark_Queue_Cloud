package com.roymark.queue.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.Anomaly;
import com.roymark.queue.entity.ActionUser;
import com.roymark.queue.service.AnomalyService;
import com.roymark.queue.service.WindowService;
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
import com.roymark.queue.util.UploadUtil;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LogManager.getLogger(UserController.class);
    
	@Autowired
    private UserService userService;

	@Autowired
	private WindowService windowService;

	@Autowired
	private AnomalyService anomalyService;
	
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
			else if (tempActionUser.getWindowHiddenId()==null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "未设置窗口");
				return jsonObject;
			}
			else if (windowService.getById(tempActionUser.getWindowHiddenId()) == null) {
				jsonObject.put("result", "no");
				jsonObject.put("msg", "设置的窗口不存在");
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
				String uploadPath = "/RemoteQueue/upload/user/";
				filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
				tempActionUser.setUserPhoto(filePath);
			}

			boolean result = userService.update(tempActionUser, Wrappers.<ActionUser>lambdaUpdate().eq(ActionUser::getUserHiddenId, tempActionUser.getUserHiddenId()));
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
				} else {
					if (tempActionUser.getWindowHiddenId() == null) {
						jsonObject.put("result", "no");
						jsonObject.put("msg", "未设置窗口");
						return jsonObject;
					}
					else if (windowService.getById(tempActionUser.getWindowHiddenId()) == null) {
						jsonObject.put("result", "no");
						jsonObject.put("msg", "设置的窗口不存在");
						return jsonObject;
					}
					tempActionUser.setUserHiddenId(Long.valueOf(0));
					String filePath = "";
					if (uploadinfo != null) {
						// 上传文件
						String uploadPath = "/RemoteQueue/upload/user/";
						filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
						tempActionUser.setUserPhoto(filePath);
						// System.out.println(filePath);
					}

					boolean result = userService.save(tempActionUser);
					if (result) {
						jsonObject.put("result", "ok");
						jsonObject.put("msg", "添加成功");
						return jsonObject;
					} else {
						jsonObject.put("result", "no");
						jsonObject.put("msg", "添加失败");
						return jsonObject;
					}
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
			// 执行分页
			IPage<ActionUser> pageList = userService.page(page, queryWrapper);
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
			logger.error("/user/queryData 错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			jsonObject.put("msg", "搜索出现错误");
			return jsonObject;
		}
	}

}

