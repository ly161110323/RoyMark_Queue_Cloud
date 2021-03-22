package com.roymark.queue.controller;

import cn.hutool.core.util.StrUtil;

import com.roymark.queue.entity.QueueArea;
import com.roymark.queue.entity.QueueMenuinfo;
import com.roymark.queue.entity.User;
import com.roymark.queue.dao.QueueAreaMapper;
import com.roymark.queue.dao.QueueMenuinfoMapper;
import com.roymark.queue.util.GetDataLs;
import com.roymark.queue.util.Pinyin;
import com.roymark.queue.util.UploadUtil;

import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.roymark.queue.util.ApiReturnUtil;
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
@RequestMapping("/QueueMenuinfo")
public class QueueMenuinfoController {

	private static final Logger logger = LogManager.getLogger(QueueMenuinfoController.class);
	@Autowired
	private QueueMenuinfoMapper queueMenuinfoMapper;
	@Autowired
	private QueueAreaMapper queueAreaMapper;

	/**
	 * 新增
	 */
	@RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
	public Object insert(QueueMenuinfo queueMenuinfo,
			@RequestParam(value = "uploadinfo", required = false) MultipartFile uploadinfo) {
		JSONObject jsonObject = new JSONObject();
		try {
			List<QueueMenuinfo> menuinfos = queueMenuinfoMapper.selectList(new QueryWrapper<QueueMenuinfo>().lambda()
					.eq(QueueMenuinfo::getStatus, "1").eq(QueueMenuinfo::getMenuName, queueMenuinfo.getMenuName()).or()
					.eq(QueueMenuinfo::getMenuCode, queueMenuinfo.getMenuCode()));
			if (menuinfos != null && menuinfos.size() > 0) {
				int code = 0;
				for (QueueMenuinfo menu : menuinfos) {
					if (menu.getAreaLs().equals(queueMenuinfo.getAreaLs())) {
						code++;
					}
				}
				if (code > 0) {
					jsonObject.put("result", "repeat");
				} else {
					// 定义Menu_Ls
					String menuLs = GetDataLs.getLs();
					// 设置新增数据
					queueMenuinfo.setMenuLs(menuLs);
					ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					HttpServletRequest request = attributes.getRequest();
					HttpSession session = request.getSession();
					User user = (User) session.getAttribute("LOGIN_USER");
					String createUser = user.getUserID();
					queueMenuinfo.setCreateUser(createUser);
					LocalDateTime createTime = LocalDateTime.now();
					queueMenuinfo.setCreateTime(createTime);
					queueMenuinfo.setStatus("1");
					String filePath = "";// 头像的路径
					if (uploadinfo != null) {
						// 账号的附件路径参考医院和政务的后台代码
						String areaLs = String.valueOf(queueMenuinfo.getAreaLs());
						String uploadPath = "/RemoteQueue/"+areaLs+"/upload/menuInfo/";
						filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
					}

					queueMenuinfo.setMenuImagepath(filePath);
					int result = queueMenuinfoMapper.insert(queueMenuinfo);
					if (result > 0) {
						jsonObject.put("result", "ok");
						return jsonObject;
					} else {
						jsonObject.put("result", "no");
						return jsonObject;
					}
				}
				return jsonObject;
			} else {
				// 定义Menu_Ls
				String menuLs = GetDataLs.getLs();
				// 设置新增数据
				queueMenuinfo.setMenuLs(menuLs);
				ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();
				HttpServletRequest request = attributes.getRequest();
				HttpSession session = request.getSession();
				User user = (User) session.getAttribute("LOGIN_USER");
				String createUser = user.getUserID();
				queueMenuinfo.setCreateUser(createUser);
				LocalDateTime createTime = LocalDateTime.now();
				queueMenuinfo.setCreateTime(createTime);
				queueMenuinfo.setStatus("1");
				String filePath = "";// 头像的路径
				if (uploadinfo != null) {
					// 账号的附件路径参考医院和政务的后台代码
					String areaLs = String.valueOf(queueMenuinfo.getAreaLs());
					String uploadPath = "/RemoteQueue/"+areaLs+"/upload/menuInfo/";
					filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
				}

				queueMenuinfo.setMenuImagepath(filePath);
				int result = queueMenuinfoMapper.insert(queueMenuinfo);
				if (result > 0) {
					jsonObject.put("result", "ok");
					return jsonObject;
				} else {
					jsonObject.put("result", "no");
					return jsonObject;
				}
			}
		} catch (Exception e) {
			logger.error("/QueueMenuinfo/insert错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
	public Object update(QueueMenuinfo queueMenuinfo, @RequestParam("updateName") String updateName,
			@RequestParam("updateCode") String updateCode,
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
			QueueMenuinfo menuinfo1 = queueMenuinfoMapper.selectOne(new QueryWrapper<QueueMenuinfo>().lambda()
					.eq(QueueMenuinfo::getStatus, "1").eq(QueueMenuinfo::getAreaLs, queueMenuinfo.getAreaLs())
					.eq(QueueMenuinfo::getMenuName, queueMenuinfo.getMenuName()));
			QueueMenuinfo menuinfo2 = queueMenuinfoMapper.selectOne(new QueryWrapper<QueueMenuinfo>().lambda()
					.eq(QueueMenuinfo::getStatus, "1").eq(QueueMenuinfo::getAreaLs, queueMenuinfo.getAreaLs())
					.eq(QueueMenuinfo::getMenuCode, queueMenuinfo.getMenuCode()));
			if (menuinfo1 == null || menuinfo2 == null) {
				queueMenuinfo.setUpdateUser(updateUser);
				LocalDateTime updateTime = LocalDateTime.now();
				queueMenuinfo.setUpdateTime(updateTime);
				queueMenuinfo.setStatus("1");
				String filePath = "";// 头像的路径
				if (uploadinfo != null) {
					// 账号的附件路径参考医院和政务的后台代码
					String areaLs = String.valueOf(queueMenuinfo.getAreaLs());
					String uploadPath = "/RemoteQueue/"+areaLs+"/upload/menuInfo/";
					filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
					queueMenuinfo.setMenuImagepath(filePath);
				}

				int result = queueMenuinfoMapper.updateById(queueMenuinfo);
				if (result > 0) {
					jsonObject.put("result", "ok");
				} else {
					jsonObject.put("result", "no");
				}
			} else {
				if (queueMenuinfo.getMenuName().equals(updateName) && queueMenuinfo.getMenuCode().equals(updateCode)) {
					queueMenuinfo.setUpdateUser(updateUser);
					LocalDateTime updateTime = LocalDateTime.now();
					queueMenuinfo.setUpdateTime(updateTime);
					queueMenuinfo.setStatus("1");
					String filePath = "";// 头像的路径
					if (uploadinfo != null) {
						// 账号的附件路径参考医院和政务的后台代码
						String areaLs = String.valueOf(queueMenuinfo.getAreaLs());
						String uploadPath = "/RemoteQueue/"+areaLs+"/upload/menuInfo/";
						filePath = UploadUtil.fileupload(request, uploadinfo, uploadPath);
						queueMenuinfo.setMenuImagepath(filePath);
					}

					int result = queueMenuinfoMapper.updateById(queueMenuinfo);
					if (result > 0) {
						jsonObject.put("result", "ok");
					} else {
						jsonObject.put("result", "no");
					}
				} else {
					jsonObject.put("result", "repeat");
				}
			}
		} catch (Exception e) {
			logger.error("/QueueMenuinfo/update错误:" + e.getMessage(), e);
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
				QueueMenuinfo menu = new QueueMenuinfo();
				menu.setStatus("0");
				queueMenuinfoMapper.update(menu, new QueryWrapper<QueueMenuinfo>().eq("Menu_Ls", deletes[i]));
			}
			jsonObject.put("result", "ok");
			return jsonObject;
		} catch (Exception e) {
			logger.error("/QueueMenuinfo/delete错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

	/**
	 * 分页查询
	 */
	@PostMapping("/list")
	public Object list(QueueMenuinfo menu, @RequestParam(required = false, defaultValue = "0") int pageNo,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		// 获取用户session信息
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession();
		User curretUser=(User)session.getAttribute("LOGIN_USER");//判断是否超管
		boolean isSupser=false;
		if(curretUser.getId().equals("0"))
		{
			isSupser=true;
		}
		QueueArea defaultProject = (QueueArea) session.getAttribute("DEFAULT_PROJECT");
		int areaLs = defaultProject.getAreaLs();
		if (isSupser) {
			// 分页构造器
			Page<QueueMenuinfo> page = new Page<QueueMenuinfo>(pageNo, pageSize);
			menu.setStatus("1");
			menu.setAreaLs(areaLs);
			IPage<QueueMenuinfo> pageList = queueMenuinfoMapper.searchPageMenuInfoList(page, menu);
			// 返回结果
			return ApiReturnUtil.success(pageList);
		} else {
			// 分页构造器
			Page<QueueMenuinfo> page = new Page<QueueMenuinfo>(pageNo, pageSize);
			menu.setStatus("1");
			menu.setAreaLs(areaLs);
			menu.setIssuper("0");
			IPage<QueueMenuinfo> pageList = queueMenuinfoMapper.searchPageMenuInfoList(page, menu);
			// 返回结果
			return ApiReturnUtil.success(pageList);
		}
	}

	/**
	 * 分页查询
	 */
	@PostMapping("/superlist")
	public Object superlist(QueueMenuinfo menu, @RequestParam(required = false, defaultValue = "0") int pageNo,
					   @RequestParam(required = false, defaultValue = "10") int pageSize) {
		// 获取用户session信息
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession();
		User curretUser=(User)session.getAttribute("LOGIN_USER");//判断是否超管
		boolean isSupser=false;
		if(curretUser.getId().equals("0"))
		{
			isSupser=true;
		}
		QueueArea defaultProject = (QueueArea) session.getAttribute("DEFAULT_PROJECT");
		int areaLs = defaultProject.getAreaLs();
		if (isSupser) {
			// 分页构造器
			Page<QueueMenuinfo> page = new Page<QueueMenuinfo>(pageNo, pageSize);
			menu.setStatus("1");
			menu.setAreaLs(0);//超管的菜单areaLs为0;
			menu.setIssuper("1");
			IPage<QueueMenuinfo> pageList = queueMenuinfoMapper.searchPageMenuInfoList(page, menu);
			// 返回结果
			return ApiReturnUtil.success(pageList);
		} else {
			return ApiReturnUtil.error("error");
		}
	}


	@RequestMapping("/searchupmenulist")
	public Object searchupmenulist(@RequestParam(required = true) String menuLevel) {
		QueryWrapper<QueueMenuinfo> menuinfoQueryWrapper = new QueryWrapper<QueueMenuinfo>();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession();
		QueueArea defaultProject = (QueueArea) session.getAttribute("DEFAULT_PROJECT");
		int areaLs = defaultProject.getAreaLs();
		menuinfoQueryWrapper.eq("Area_Ls", areaLs);
		menuinfoQueryWrapper.eq("Menu_Level", menuLevel);
		menuinfoQueryWrapper.eq("status", "1");
		List<QueueMenuinfo> menuList = queueMenuinfoMapper.selectList(menuinfoQueryWrapper);
		return ApiReturnUtil.success(menuList);
	}
	@RequestMapping("/searchsuperupmenulist")
	public Object searchsuperupmenulist(@RequestParam(required = true) String menuLevel) {
		QueryWrapper<QueueMenuinfo> menuinfoQueryWrapper = new QueryWrapper<QueueMenuinfo>();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession();
		QueueArea defaultProject = (QueueArea) session.getAttribute("DEFAULT_PROJECT");
		int areaLs = defaultProject.getAreaLs();
		menuinfoQueryWrapper.eq("Area_Ls", 0);
		menuinfoQueryWrapper.eq("Menu_Level", menuLevel);
		menuinfoQueryWrapper.eq("status", "1");
		menuinfoQueryWrapper.eq("is_super", "1");
		List<QueueMenuinfo> menuList = queueMenuinfoMapper.selectList(menuinfoQueryWrapper);
		return ApiReturnUtil.success(menuList);
	}
	/**
	 * 获取当前账号的区域权限
	 */
	@RequestMapping(value = "getUserTree", produces = "application/json;charset=utf-8")
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
			logger.error("/QueueMenuinfo/getUserTree错误:" + e.getMessage(), e);
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
			logger.error("/QueueMenuinfo/generateTree错误:" + ex.getMessage(), ex);
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
			logger.error("/QueueMenuinfo/generateTreeById错误:" + ex.getMessage(), ex);
		}
		return resultList;
	}

}
