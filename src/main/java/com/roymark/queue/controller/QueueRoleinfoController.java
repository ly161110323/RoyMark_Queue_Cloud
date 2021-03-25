package com.roymark.queue.controller;

import cn.hutool.core.util.StrUtil;

import com.roymark.queue.dao.QueueAreaMapper;
import com.roymark.queue.entity.QueueArea;
import com.roymark.queue.entity.QueueMenuinfo;
import com.roymark.queue.entity.QueueRoleinfo;
import com.roymark.queue.dao.QueueRoleinfoMapper;

import com.roymark.queue.entity.User;
import com.roymark.queue.util.GetDataLs;
import com.roymark.queue.util.Pinyin;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author wangfz
 * @since 2020-01-13
 */
@RestController
@RequestMapping("/QueueRoleinfo")
public class QueueRoleinfoController {

	private static final Logger logger = LogManager.getLogger(QueueRoleinfoController.class);
	@Autowired
	private QueueRoleinfoMapper queueRoleinfoMapper;
	@Autowired
	private QueueAreaMapper queueAreaMapper;

	/**
	 * 新增
	 */
	@RequestMapping(value = "/insert", produces = "application/json;charset=utf-8")
	public Object insert(QueueRoleinfo tempQueueRoleinfo) {
		JSONObject jsonObject = new JSONObject();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("LOGIN_USER");
		try {
			List<QueueRoleinfo> roleinfos = queueRoleinfoMapper.selectList(new QueryWrapper<QueueRoleinfo>().lambda()
					.eq(QueueRoleinfo::getStatus, "1").eq(QueueRoleinfo::getRoleName, tempQueueRoleinfo.getRoleName()).or()
					.eq(QueueRoleinfo::getRoleCode, tempQueueRoleinfo.getRoleCode()));
			if (roleinfos != null && roleinfos.size() > 0) {
				int code = 0;
				for (QueueRoleinfo role : roleinfos) {
					if (role.getAreaLs() == tempQueueRoleinfo.getAreaLs()) {
						code++;
					}
				}
				if (code > 0) {
					jsonObject.put("result", "repeat");
					return jsonObject;
				} else {
					String createUser = user.getUserID();
					// 定义Role_Ls
					String Role_Ls = GetDataLs.getLs();
					// 设置新增数据
					tempQueueRoleinfo.setRoleLs(Role_Ls);
					tempQueueRoleinfo.setCreateUser(createUser);
					LocalDateTime createTime = LocalDateTime.now();
					tempQueueRoleinfo.setCreateTime(createTime);
					tempQueueRoleinfo.setStatus("1");
					int result = queueRoleinfoMapper.insert(tempQueueRoleinfo);
					if (result > 0) {
						jsonObject.put("result", "ok");
						return jsonObject;
					} else {
						jsonObject.put("result", "no");
						return jsonObject;
					}
				}
			}else {
				String createUser = user.getUserID();
				// 定义Role_Ls
				String Role_Ls = GetDataLs.getLs();
				// 设置新增数据
				tempQueueRoleinfo.setRoleLs(Role_Ls);
				tempQueueRoleinfo.setCreateUser(createUser);
				LocalDateTime createTime = LocalDateTime.now();
				tempQueueRoleinfo.setCreateTime(createTime);
				tempQueueRoleinfo.setStatus("1");
				int result = queueRoleinfoMapper.insert(tempQueueRoleinfo);
				if (result > 0) {
					jsonObject.put("result", "ok");
					return jsonObject;
				} else {
					jsonObject.put("result", "no");
					return jsonObject;
				}
			}
		} catch (Exception e) {
			logger.error("/QueueRoleinfo/insert错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
	public Object update(QueueRoleinfo roleinfo, String updateName, String updateNo) {
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
			QueueRoleinfo hos = queueRoleinfoMapper.selectOne(
					new QueryWrapper<QueueRoleinfo>().eq("Role_Name", roleinfo.getRoleName()).eq("Status", "1")
					.eq("Area_Ls", roleinfo.getAreaLs()));
			QueueRoleinfo hos2 = queueRoleinfoMapper.selectOne(
					new QueryWrapper<QueueRoleinfo>().eq("Role_Code", roleinfo.getRoleCode()).eq("Status", "1")
					.eq("Area_Ls", roleinfo.getAreaLs()));
			if (hos == null || hos2 == null) {
				roleinfo.setUpdateUser(updateUser);
				LocalDateTime updateTime = LocalDateTime.now();
				roleinfo.setUpdateTime(updateTime);
				roleinfo.setStatus("1");
				int result = queueRoleinfoMapper.update(roleinfo,
						new QueryWrapper<QueueRoleinfo>().lambda().eq(QueueRoleinfo::getRoleLs, roleinfo.getRoleLs()));
				if (result > 0) {
					jsonObject.put("result", "ok");
				} else {
					jsonObject.put("result", "no");
				}
			} else {
				if (hos.getRoleName().equals(updateName) && hos2.getRoleCode().equals(updateNo)) {
					// 定义Role_Name_PY
					roleinfo.setUpdateUser(updateUser);
					LocalDateTime updateTime = LocalDateTime.now();
					roleinfo.setUpdateTime(updateTime);
					roleinfo.setStatus("1");
					int result = queueRoleinfoMapper.update(roleinfo, new QueryWrapper<QueueRoleinfo>().lambda()
							.eq(QueueRoleinfo::getRoleLs, roleinfo.getRoleLs()));
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
			logger.error("/QueueRoleinfo/update错误:" + e.getMessage(), e);
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
				QueueRoleinfo entity = new QueueRoleinfo();
				entity.setStatus("0");
				queueRoleinfoMapper.update(entity, new QueryWrapper<QueueRoleinfo>().eq("Role_Ls", deletes[i]));
			}
			jsonObject.put("result", "ok");
			return jsonObject;
		} catch (Exception e) {
			logger.error("/QueueRoleinfo/delete错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
			return jsonObject;
		}
	}

	/**
	 * 分页查询
	 */
	@PostMapping("/list")
	public Object list(QueueRoleinfo entity, @RequestParam(required = false, defaultValue = "0") int pageNo,
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
			Page<QueueRoleinfo> page = new Page<QueueRoleinfo>(pageNo, pageSize);
			// 条件构造器
			String primaryName = entity.getRoleName();
			QueryWrapper<QueueRoleinfo> queryWrapper = new QueryWrapper<QueueRoleinfo>();
			queryWrapper.eq("Status", "1");
			if (!StrUtil.isBlank(primaryName)) // 根据诊室名称的查询条件不为空
			{
				queryWrapper.like("Role_Name", primaryName);
			}
			queryWrapper.eq("Area_Ls", areaLs);
			// 执行分页
			IPage<QueueRoleinfo> pageList = queueRoleinfoMapper.selectPage(page, queryWrapper);
			List<QueueRoleinfo> roleList = pageList.getRecords();
			for (QueueRoleinfo tempRole : roleList) {
				Integer tempRoleAreaLs = tempRole.getAreaLs();
				QueueArea tempArea = queueAreaMapper.selectById(tempRoleAreaLs);
				tempRole.setAreaName(tempArea.getAreaName());
			}
			// 返回结果
			return ApiReturnUtil.success(pageList);
		} catch (Exception e) {
			logger.error("/QueueRoleinfo/list错误:" + e.getMessage(), e);
			return "error";
		}
	}

	/**
	 * 分页查询
	 */
	@RequestMapping(value = "/singletablelist", produces = "application/json;charset=utf-8")
	public Object singletablelist(QueueRoleinfo entity, int pageNo, int pageSize) {
		try {
			// 分页构造器
			Page<QueueRoleinfo> page = new Page<QueueRoleinfo>(pageNo, pageSize);
			String primaryName = entity.getRoleName();
			QueryWrapper<QueueRoleinfo> queryWrapper = new QueryWrapper<QueueRoleinfo>();
			queryWrapper.eq("Status", "1");
			if (!StrUtil.isBlank(primaryName)) // 根据诊室名称的查询条件不为空
			{
				queryWrapper.like("Role_Name", primaryName);
			}
			// 执行分页
			IPage<QueueRoleinfo> pageList = queueRoleinfoMapper.selectPage(page, queryWrapper);
			// 返回结果
			return ApiReturnUtil.success(pageList);
		} catch (Exception e) {
			logger.error("/QueueRoleinfo/singletablelist错误:" + e.getMessage(), e);
			return "error";
		}
	}

	/**
	 *
	 */
	@RequestMapping(value = "/bindexeprogram", produces = "application/json;charset=utf-8")
	public Object bindexeprogram(QueueRoleinfo entity) {
		JSONObject jsonObject = new JSONObject();
		// 获取操作用户的用户名
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("LOGIN_USER");
//        String updateUser = user.getUserID();
		// 进行修改操作
		try {

			String primaryKey = entity.getRoleLs();
			QueryWrapper<QueueRoleinfo> wrapper = new QueryWrapper<QueueRoleinfo>();
			wrapper.eq("Role_Ls", primaryKey);
			wrapper.eq("Status", "1");

			List<QueueRoleinfo> searchList = queueRoleinfoMapper.selectList(wrapper);// 同名的大于1个则说明有重名
			if (searchList == null || searchList.size() == 0) // 没有这条分诊台数据
			{
				jsonObject.put("result", "none");
				return jsonObject;
			} else {

//                entity.setUpdateUser(updateUser);
				LocalDateTime updateTime = LocalDateTime.now();
				entity.setUpdateTime(updateTime);
				entity.setStatus("1");
				int result = queueRoleinfoMapper.updateById(entity);
				if (result > 0) {
					jsonObject.put("result", "ok");
				} else {
					jsonObject.put("result", "no");
				}
			}
		} catch (Exception e) {
			logger.error("/QueueRoleinfo/bindexeprogram错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
		}
		return jsonObject;

	}

	/**
	 * 删除（解绑）
	 */
	@RequestMapping(value = "/unbindexeprogram", produces = "application/json;charset=utf-8")
	public Object unbindexeprogram(String deleteLss) {
		JSONObject jsonObject = new JSONObject();

		// 获取操作用户的用户名
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("LOGIN_USER");
//        String updateUser = user.getUserID();
		// 进行修改操作
		try {
			String[] deletes = deleteLss.split(",");
			for (int i = 0; i < deletes.length; i++) {
				{
					String primaryKeyLs = deletes[i];
					QueryWrapper<QueueRoleinfo> wrapper = new QueryWrapper<QueueRoleinfo>();
					wrapper.eq("Role_Ls", primaryKeyLs);
					wrapper.eq("Status", "1");

					List<QueueRoleinfo> searchList = queueRoleinfoMapper.selectList(wrapper);// 同名的大于1个则说明有重名
					if (searchList == null || searchList.size() == 0) {
						jsonObject.put("result", "none");
						return jsonObject;
					} else {
						QueueRoleinfo tempEntity = queueRoleinfoMapper.selectById(primaryKeyLs);
//                        tempEntity.setUpdateUser(updateUser);

						LocalDateTime updateTime = LocalDateTime.now();
						tempEntity.setUpdateTime(updateTime);
						tempEntity.setStatus("1");
						int result = queueRoleinfoMapper.updateById(tempEntity);
						if (result > 0) {
							jsonObject.put("result", "ok");
						} else {
							jsonObject.put("result", "no");
						}
					}
				}

			}
		} catch (Exception e) {
			logger.error("/QueueRoleinfo/bindexeprogram错误:" + e.getMessage(), e);
			jsonObject.put("result", "error");
		}
		return jsonObject;
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
			logger.error("/QueueRoleinfo/getUserTree错误:" + e.getMessage(), e);
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
			logger.error("/QueueRoleinfo/generateTree错误:" + ex.getMessage(), ex);
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
			logger.error("/QueueRoleinfo/generateTreeById错误:" + ex.getMessage(), ex);
		}
		return resultList;
	}

}
