package com.roymark.queue.controller;

import SuperDog.DogStatus;
import cn.hutool.core.util.StrUtil;

import com.roymark.dog.DogBean;
import com.roymark.dog.DogUtil;
import com.roymark.queue.dao.QueueAreaMapper;
import com.roymark.queue.entity.QueueArea;
import com.roymark.queue.entity.QueueMenuinfo;
import com.roymark.queue.entity.QueueRoleinfo;
import com.roymark.queue.entity.QueueWebaccount;
import com.roymark.queue.dao.QueueWebaccountMapper;

import com.roymark.queue.entity.User;
import com.roymark.queue.service.QueueMenuinfoService;
import com.roymark.queue.util.*;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author wangfz
 * @since 2020-01-13
 */
@RestController
@RequestMapping("/webaccount")
public class QueueWebaccountController {

	private static final Logger logger = LogManager.getLogger(QueueWebaccountController.class);
	@Autowired
	private QueueWebaccountMapper queueWebaccountMapper;
	@Autowired
	private QueueAreaMapper queueAreaMapper;

	private static final boolean isDog = false;

	@Autowired
	private QueueMenuinfoService queueMenuinfoService;

	/**
	 * 新增
	 */
	@RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
	public Object insert(QueueWebaccount tempQueueWebaccount,
			@RequestParam(value = "uploadinfo", required = false) MultipartFile uploadinfo) {
		JSONObject jsonObject = new JSONObject();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("LOGIN_USER");
		try {
			List<QueueWebaccount> webaccounts = queueWebaccountMapper
					.selectList(new QueryWrapper<QueueWebaccount>().lambda().eq(QueueWebaccount::getStatus, "1")
							.eq(QueueWebaccount::getWebaccountLogin, tempQueueWebaccount.getWebaccountLogin()).or()
							.eq(QueueWebaccount::getWebaccountCode, tempQueueWebaccount.getWebaccountCode()));
			if (webaccounts != null && webaccounts.size() > 0) {
				int code = 0;
				for (QueueWebaccount webaccount : webaccounts) {
					if (webaccount.getAreaLs().equals(tempQueueWebaccount.getAreaLs())) {
						code++;
					}
				}
				if (code > 0) {
					jsonObject.put("result", "repeat");
					return jsonObject;
				} else {
					// 定义主键_Ls
					String primaryKeyLs = GetDataLs.getLs();
					// 设置新增数据
					tempQueueWebaccount.setWebaccountLs(primaryKeyLs);
					String createUser = user.getUserID();
					tempQueueWebaccount.setCreateUser(createUser);

					LocalDateTime createTime = LocalDateTime.now();
					tempQueueWebaccount.setCreateTime(createTime);
					tempQueueWebaccount.setStatus("1");
					if (tempQueueWebaccount.getWebaccountPassword() != null
							|| "".equals(tempQueueWebaccount.getWebaccountPassword())) {
						tempQueueWebaccount.setWebaccountPassword(
								Md5Util.EncoderByMd5(tempQueueWebaccount.getWebaccountPassword()));
					}
					String filePath = "";// 头像的路径
					if (uploadinfo != null) {
						// 账号的附件路径
						String areaLs = String.valueOf(tempQueueWebaccount.getAreaLs());
						String uploadPath = "/RemoteQueue/"+areaLs+"/upload/webAccount/";
						filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
					}
					tempQueueWebaccount.setWebaccountImage(filePath);

					int result = queueWebaccountMapper.insert(tempQueueWebaccount);
					if (result > 0) {
						jsonObject.put("result", "ok");
						return jsonObject;
					} else {
						jsonObject.put("result", "no");
						return jsonObject;
					}
				}
			} else {
				// 定义主键_Ls
				String primaryKeyLs = GetDataLs.getLs();
				// 设置新增数据
				tempQueueWebaccount.setWebaccountLs(primaryKeyLs);
				String createUser = user.getUserID();
				tempQueueWebaccount.setCreateUser(createUser);

				LocalDateTime createTime = LocalDateTime.now();
				tempQueueWebaccount.setCreateTime(createTime);
				tempQueueWebaccount.setStatus("1");
				if (tempQueueWebaccount.getWebaccountPassword() != null
						|| "".equals(tempQueueWebaccount.getWebaccountPassword())) {
					tempQueueWebaccount
							.setWebaccountPassword(Md5Util.EncoderByMd5(tempQueueWebaccount.getWebaccountPassword()));
				}
				String filePath = "";// 头像的路径
				if (uploadinfo != null) {
					String areaLs = String.valueOf(tempQueueWebaccount.getAreaLs());
					String uploadPath = "/RemoteQueue/"+areaLs+"/upload/webAccount/";
					filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
				}
				tempQueueWebaccount.setWebaccountImage(filePath);

				int result = queueWebaccountMapper.insert(tempQueueWebaccount);
				if (result > 0) {
					jsonObject.put("result", "ok");
					return jsonObject;
				} else {
					jsonObject.put("result", "no");
					return jsonObject;
				}
			}
		} catch (Exception e) {
			logger.error("/webaccount/insert错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
	public Object update(QueueWebaccount tempQueueWebaccount, @RequestParam("updateName") String updateName,
			@RequestParam("updateNo") String updateNo,
			@RequestParam(value = "uploadinfo", required = false) MultipartFile uploadinfo) {
		JSONObject jsonObject = new JSONObject();
		// 获取操作用户的用户名
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("LOGIN_USER");
		String updateUser = user.getUserID();
		// 进行修改操作
		try {
			// 重复值修改判断
			QueueWebaccount webaccount1 = queueWebaccountMapper.selectOne(
					new QueryWrapper<QueueWebaccount>().eq("WebAccount_LogIn", tempQueueWebaccount.getWebaccountLogin())
							.eq("Status", "1").eq("Area_Ls", tempQueueWebaccount.getAreaLs()));
			QueueWebaccount webaccount2 = queueWebaccountMapper.selectOne(
					new QueryWrapper<QueueWebaccount>().eq("WebAccount_Code", tempQueueWebaccount.getWebaccountCode())
							.eq("Status", "1").eq("Area_Ls", tempQueueWebaccount.getAreaLs()));
			String filePath = "";
			if (uploadinfo != null) {
				// 上传文件
				String areaLs = String.valueOf(tempQueueWebaccount.getAreaLs());
				String uploadPath = "/RemoteQueue/"+areaLs+"/upload/webAccount/";
				filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
			}
			tempQueueWebaccount.setWebaccountImage(filePath);
			if (webaccount1 == null || webaccount2 == null) {

				tempQueueWebaccount.setUpdateUser(updateUser);
				LocalDateTime updateTime = LocalDateTime.now();
				tempQueueWebaccount.setUpdateTime(updateTime);
				tempQueueWebaccount.setStatus("1");
				// 对密码进行MD5加密
				if (tempQueueWebaccount.getWebaccountPassword() != null
						|| "".equals(tempQueueWebaccount.getWebaccountPassword())) {
					tempQueueWebaccount
							.setWebaccountPassword(Md5Util.EncoderByMd5(tempQueueWebaccount.getWebaccountPassword()));
				}
				int result = queueWebaccountMapper.update(tempQueueWebaccount, new QueryWrapper<QueueWebaccount>()
						.lambda().eq(QueueWebaccount::getWebaccountLs, tempQueueWebaccount.getWebaccountLs()));
				if (result > 0) {
					jsonObject.put("result", "ok");
				} else {
					jsonObject.put("result", "no");
				}

			} else {
				if (webaccount1.getWebaccountLogin().equals(updateName)
						&& webaccount2.getWebaccountCode().equals(updateNo)) {

					tempQueueWebaccount.setUpdateUser(updateUser);
					LocalDateTime updateTime = LocalDateTime.now();
					tempQueueWebaccount.setUpdateTime(updateTime);
					tempQueueWebaccount.setStatus("1");
					// 对密码进行MD5加密
					if (tempQueueWebaccount.getWebaccountPassword() != null
							|| "".equals(tempQueueWebaccount.getWebaccountPassword())) {
						tempQueueWebaccount.setWebaccountPassword(
								Md5Util.EncoderByMd5(tempQueueWebaccount.getWebaccountPassword()));
					}
					int result = queueWebaccountMapper.update(tempQueueWebaccount, new QueryWrapper<QueueWebaccount>()
							.lambda().eq(QueueWebaccount::getWebaccountLs, tempQueueWebaccount.getWebaccountLs()));
					if (result > 0) {
						jsonObject.put("result", "ok");
					} else {
						jsonObject.put("result", "no");
					}
				} else if (webaccount1.getWebaccountLogin().equals(updateName)
						&& !webaccount2.getWebaccountCode().equals(updateNo)) {
					jsonObject.put("result", "Coderepeat");
				} else if (!webaccount1.getWebaccountLogin().equals(updateName)
						&& webaccount2.getWebaccountCode().equals(updateNo)) {
					jsonObject.put("result", "Loginrepeat");
				}
			}
		} catch (Exception e) {
			logger.error("/QueueWebaccount/update错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
		}
		return jsonObject;

	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
	public Object delete(String deleteLss) {
		JSONObject jsonObject = new JSONObject();
		try {
			String[] deletes = deleteLss.split(",");
			for (int i = 0; i < deletes.length; i++) {
				QueueWebaccount entity = new QueueWebaccount();
				entity.setStatus("0");
				queueWebaccountMapper.update(entity,
						new QueryWrapper<QueueWebaccount>().eq("WebAccount_Ls", deletes[i]));
			}
			jsonObject.put("result", "ok");
			return jsonObject;
		} catch (Exception e) {
			logger.error("/QueueWebaccount/delete错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

	/**
	 * 分页查询
	 */
	@RequestMapping("/list")
	public Object list(QueueWebaccount entity, @RequestParam(required = false, defaultValue = "0") int pageNo,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		try {
			// 获取用户session信息
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			HttpSession session = request.getSession();
			QueueArea defaultProject = (QueueArea) session.getAttribute("DEFAULT_PROJECT");
			int areaLs = defaultProject.getAreaLs();
			// 分页构造器
			Page<QueueWebaccount> page = new Page<QueueWebaccount>(pageNo, pageSize);
			entity.setStatus("1");
			entity.setAreaLs(areaLs);
			// 执行分页
			IPage<QueueWebaccount> pageList = queueWebaccountMapper.searchPageWebaccountInfoList(page, entity); // 返回结果
			return ApiReturnUtil.success(pageList);
		} catch (Exception e) {
			logger.error("/QueueWebaccount/list错误:" + e.getMessage(), e);
			return "error";
		}

	}

	/**
	 * 分页查询
	 */
	@RequestMapping(value = "/singletablelist", produces = "application/json;charset=utf-8")
	public Object singletablelist(QueueWebaccount entity, int pageNo, int pageSize) {
		try {
			// 分页构造器
			Page<QueueWebaccount> page = new Page<QueueWebaccount>(pageNo, pageSize);
			String primaryName = entity.getWebaccountName();
			QueryWrapper<QueueWebaccount> queryWrapper = new QueryWrapper<QueueWebaccount>();
			queryWrapper.eq("Status", "1");
			if (!StrUtil.isBlank(primaryName)) // 根据诊室名称的查询条件不为空
			{
				queryWrapper.like("WebAccount_Name", primaryName);
			}
			// 执行分页
			IPage<QueueWebaccount> pageList = queueWebaccountMapper.selectPage(page, queryWrapper);
			// 返回结果
			return ApiReturnUtil.success(pageList);
		} catch (Exception e) {
			logger.error("/QueueWebaccount/singletablelist错误:" + e.getMessage(), e);
			return "error";
		}
	}

	@RequestMapping("/login")
	public Object login(String loginId, String passWord, String areaLs) {
		JSONObject jsonObject = new JSONObject();
		DogUtil dogUtil = new DogUtil();

		try {

			DogBean dog = new DogBean();

			if (isDog) {
				int status = dog.dog_login();
				if (status != DogStatus.DOG_STATUS_OK) {
					if (status == DogStatus.DOG_NOT_FOUND) { // 没有找到可用的狗
						jsonObject.put("result", "error");
						jsonObject.put("msg", "没有找到可用的狗");
						return jsonObject;
					} else if (status == DogStatus.DOG_FEATURE_EXPIRED) { // 已经过期
						jsonObject.put("result", "error");
						jsonObject.put("msg", "加密狗已经过期");
						return jsonObject;
					} else { //
						jsonObject.put("result", "error");
						jsonObject.put("msg", "加密狗发生了未可预期的错误!");
						return jsonObject;
					}

				} else {
					// 客户端的加密狗，提示特征码为空
					String sessionXml = dog.dog_feature_id();
					String featureid = dogUtil.readStringXml2(sessionXml);
					if (featureid == null || "".equals(featureid)) {
						jsonObject.put("result", "error");
						jsonObject.put("msg", "提示特征码为空");
						return jsonObject;
					}
					// 判断有效期
					String end_day = dogUtil.getDogTime();
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String start_day = sf.format(new Date());
					int countDay = dogUtil.daysBetween(start_day, end_day);
					if (countDay < 0) {
						jsonObject.put("result", "error");
						jsonObject.put("msg", "加密狗已经超过有效期!");
						return jsonObject;
					}
				}
			}

			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			String webAccountLs = "";
			HttpSession session = request.getSession();

			String superUser = PropertiesUtils.readData("otherinfo.properties", "superUser");
			String superPwd = PropertiesUtils.readData("otherinfo.properties", "superPwd");
			boolean isSuper = false;
			if (loginId.equals(superUser) && passWord.equals(superPwd)) {
				isSuper = true;
			}
			if (isSuper) {
				User user = new User();
				user.setUserID(loginId);
				user.setId("0");
				user.setUsername("超级管理员 ");
				session.setAttribute("LOGIN_USER", user);
				user.setTopAreaLs(0);// 超管的顶层AreaLs为0;
				String loginIp = GetLoginIp.getIpAddr(request); // 取得登陆客户端IP
				session.setAttribute("LOGIN_CLIENT", loginIp);

				QueryWrapper<QueueArea> searchAreaWrapper = new QueryWrapper<QueueArea>();
				searchAreaWrapper.eq("parent_area_ls", 0); // 超管取父id为0的第一个做为默认大厅。树展现时还是展现为全部；
				searchAreaWrapper.eq("status", "1");
				List<QueueArea> areaList = queueAreaMapper.selectList(searchAreaWrapper);
				QueueArea defaultArea = new QueueArea();
				if (areaList != null && areaList.size() > 0) // 如果没有默认项目,超管还是可以登录。
				{
					defaultArea = areaList.get(0);
					session.setAttribute("DEFAULT_PROJECT", defaultArea);
				} else {
					session.setAttribute("DEFAULT_PROJECT", null);
				}

				QueryWrapper<QueueMenuinfo> menuinfoQueryWrapper = new QueryWrapper<QueueMenuinfo>();
				menuinfoQueryWrapper.eq("Menu_IsShow", "1");
				menuinfoQueryWrapper.eq("status", "1");
//                改菜单的界面布局,临时放开显示所有菜单
				if (defaultArea != null) {
					menuinfoQueryWrapper.eq("Area_Ls", defaultArea.getAreaLs());
					menuinfoQueryWrapper.or().eq("is_super", "1");// is super为1的菜单跟大厅无关，可以直接引入
				}
				menuinfoQueryWrapper.orderByAsc("Menu_No");
				List<QueueMenuinfo> menuList = queueMenuinfoService.searchMenuInfoList(menuinfoQueryWrapper, true);
				session.setAttribute("menuList", menuList);
				jsonObject.put("result", "success");
				jsonObject.put("msg", "");
				return jsonObject;
			} else {
				QueueWebaccount webAccount = new QueueWebaccount();
				webAccount.setWebaccountLogin(loginId);
				QueryWrapper<QueueWebaccount> queryWrapperw = new QueryWrapper<QueueWebaccount>(webAccount);
				queryWrapperw.eq("Area_Ls", areaLs);
				webAccount = queueWebaccountMapper.selectOne(queryWrapperw);
				// //账号判断
				if (webAccount != null) {
					User tempUser = new User();
					tempUser.setUsername(webAccount.getWebaccountName());
					tempUser.setPassword(webAccount.getWebaccountPassword());
					tempUser.setId(webAccount.getWebaccountLs() + "");
					webAccountLs = webAccount.getWebaccountLs() + "";
					tempUser.setTopAreaLs(webAccount.getAreaLs());// 设置账号的顶层AreaId;

					QueryWrapper<QueueArea> searchAreaWrapper = new QueryWrapper<QueueArea>();
					searchAreaWrapper.eq("Area_Ls", webAccount.getAreaLs());
					searchAreaWrapper.eq("status", "1");
					QueueArea defaultArea = queueAreaMapper.selectOne(searchAreaWrapper);
					if (defaultArea == null) // 如果没有默认项目,提示出错
					{
						jsonObject.put("result", "error");
						jsonObject.put("msg", "该账号未配置所属大厅,请联系系统管理员!");
						return jsonObject;
					} else {
						session.setAttribute("DEFAULT_PROJECT", defaultArea);
					}

					if (Md5Util.checkpassword(passWord, tempUser.getPassword())) {

						tempUser.setUserID(loginId);
						session.setAttribute("LOGIN_USER", tempUser);
						// 取得登陆客户端IP
						String loginIp = GetLoginIp.getIpAddr(request);
						session.setAttribute("LOGIN_CLIENT", loginIp);

						QueryWrapper<QueueMenuinfo> menuinfoQueryWrapper = new QueryWrapper<QueueMenuinfo>();
						menuinfoQueryWrapper.eq("Menu_IsShow", "1");
						menuinfoQueryWrapper.eq("status", "1");
						List<QueueMenuinfo> menuList = queueMenuinfoService
								.searchMenuInfoListByAreaLs(webAccount.getAreaLs(), true);
						session.setAttribute("menuList", menuList);
						jsonObject.put("result", "success");
						jsonObject.put("msg", "");
						return jsonObject;
					} else {
						jsonObject.put("result", "error");
						jsonObject.put("msg", "密码错误!");
						return jsonObject;
					}
				} else {// 用户不存在
					jsonObject.put("result", "error");
					jsonObject.put("msg", "该用户不存在!");
					return jsonObject;

				}

			}

			// 保存客户端登陆IP纪录
//            int count = loginServies.loginUserHistory(loginId);
//            if (count == 0) {
//                loginServies.addLoginUserHistory(loginId,
//                        (String) session.getAttribute("LOGIN_CLIENT"));
//            } else {
//                loginServies.updLoginUserHistory(loginId,
//                        (String) session.getAttribute("LOGIN_CLIENT"));
//            }
//            logger.error("/webaccount/login错误:" + ex.getMessage(), ex);

		} catch (Exception ex) {
			logger.error("/webaccount/login错误:" + ex.getMessage(), ex);
			jsonObject.put("result", "error");
			jsonObject.put("msg", ex.getMessage());
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
		} catch (Exception ex) {
			logger.error("/webaccount/logout错误:" + ex.getMessage(), ex);
			return "error";
		}

	}
	
	/**
	 * 登录时获取所有大厅数据
	 */
	@RequestMapping(value = "/getAllTree", produces = "application/json;charset=utf-8")
	public Object getAllTree() {
		try {
			List<QueueArea> TreeList = new ArrayList<QueueArea>();
			List<QueueArea> tempList = generateTree(0);
			TreeList.addAll(tempList);
			return ApiReturnUtil.success(TreeList);
		} catch (Exception e) {
			logger.error("/webaccount/getAllTree错误:" + e.getMessage(), e);
			return "error";
		}
	}

	/**
	 * 获取当前账号的区域权限
	 */
	@RequestMapping(value = "/getUserTree", produces = "application/json;charset=utf-8")
	public Object getUserTree() {
		try {
			// 获取用户session信息
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			HttpSession session = request.getSession();
			QueueArea area = (QueueArea) session.getAttribute("DEFAULT_PROJECT");
			List<QueueArea> TreeList = new ArrayList<QueueArea>();
			if (area.getParentAreaLs() == 0) {
				QueueArea rootArea = new QueueArea();
				rootArea.setAreaLs(0);
				rootArea.setAreaName("总厅(无上级办事大厅)");
				rootArea.setParentAreaLs(-1); // 构造根节点
				TreeList.add(rootArea);
				List<QueueArea> tempList = generateTree(0);
				TreeList.addAll(tempList);
			} else {
				TreeList = generateTreeById(area.getAreaLs());
			}
			return ApiReturnUtil.success(TreeList);
		} catch (Exception e) {
			logger.error("/webaccount/getUserTree错误:" + e.getMessage(), e);
			return "error";
		}
	}

	/* 根据父节点areaLs生成树 */
	public List<QueueArea> generateTree(Integer parentId) {
		List<QueueArea> resultList = new ArrayList<QueueArea>();

		try {
			QueryWrapper<QueueArea> queryWrapper = new QueryWrapper<QueueArea>();
			queryWrapper.eq("Status", "1");
			queryWrapper.eq("parent_area_ls", parentId);
			queryWrapper.orderByAsc("Area_OrderNo"); // 增加排序字段
			List<QueueArea> list = queueAreaMapper.selectList(queryWrapper);
			for (QueueArea tempArea : list) {
				Integer tempParentId = tempArea.getAreaLs();
				resultList.add(tempArea);
				List<QueueArea> childList = generateTree(tempParentId);
				resultList.addAll(childList);// 求两个数组的并集

			}
		} catch (Exception ex) {
			logger.error("/webaccount/generateTree错误:" + ex.getMessage(), ex);
		}

		return resultList;
	}

	/* 根据当前节点areaLs生成tree */
	public List<QueueArea> generateTreeById(Integer areaLs) {
		List<QueueArea> resultList = new ArrayList<QueueArea>();
		try {
			QueryWrapper<QueueArea> queryWrapper = new QueryWrapper<QueueArea>();
			queryWrapper.eq("Status", "1");
			queryWrapper.eq("Area_Ls", areaLs);
			queryWrapper.orderByAsc("Area_OrderNo"); // 增加排序字段
			List<QueueArea> list = queueAreaMapper.selectList(queryWrapper);
			for (QueueArea tempArea : list) {
				Integer tempParentId = tempArea.getAreaLs();
				resultList.add(tempArea);
				List<QueueArea> childList = generateTree(tempParentId);// 这里要调用根据父节点添加子节点的方法，不然会死循环
				resultList.addAll(childList);// 求两个数组的并集
			}
		} catch (Exception ex) {
			logger.error("/webaccount/generateTreeById错误:" + ex.getMessage(), ex);
		}
		return resultList;
	}

}
