package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wangfz
 * @since 2020-01-13
 */
public class QueueMenuinfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(value = "Menu_Ls")
	private String menuLs;
	/**
	 * 菜单编号
	 */
	@TableField(value = "Menu_Code")
	private String menuCode;
	/**
	 * 菜单名称
	 */
	@TableField(value = "Menu_Name")
	private String menuName;
	/**
	 * 菜单层级
	 */
	@TableField(value = "Menu_Level")
	private String menuLevel;
	/**
	 * 上级菜单
	 */
	@TableField(value = "Menu_Up")
	private String menuUp;
	/**
	 * 是否显示0 =否,1=是
	 */
	@TableField(value = "Menu_IsShow")
	private String menuIsshow;
	/**
	 * 是否新窗口打开
	 */
	@TableField(value = "Menu_IsNewWindow")
	private String menuIsnewwindow;
	/**
	 * 菜单Url
	 */
	@TableField(value = "Menu_Url")
	private String menuUrl;
	/**
	 * 菜单显示顺序
	 */
	@TableField(value = "Menu_No")
	private Integer menuNo;
	/**
	 * 菜单图标
	 */
	@TableField(value = "Menu_ImagePath")
	private String menuImagepath;
	/**
	 * 创建用户
	 */
	@TableField(value = "Create_User")
	private String createUser;
	/**
	 * 创建时间
	 */
	@TableField(value = "Create_Time")
	private LocalDateTime createTime;
	/**
	 * 修改人员
	 */
	@TableField(value = "Update_User")
	private String updateUser;
	/**
	 * 修改时间
	 */
	@TableField(value = "Update_Time")
	private LocalDateTime updateTime;
	/**
	 * 数据状态0=已删除，1=正常数据
	 */
	@TableField(value = "Status")
	private String Status;
	/**
	 * 区域编号
	 */
	@TableField(value = "Area_Ls")
	private Integer areaLs;
	/**
	 * 区域编号
	 */
	@TableField(value = "is_super")
	private String issuper;

	// 新增状态列
	// 是否有子菜单
	@TableField(exist = false)
	private Integer isHaveChildren;

	// 上级菜单名称
	@TableField(exist = false)
	private String upName;

	@TableField(exist = false)
	private String areaName;

	public Integer getIsHaveChildren() {
		return isHaveChildren;
	}

	public void setIsHaveChildren(Integer isHaveChildren) {
		this.isHaveChildren = isHaveChildren;
	}

	public String getUpName() {
		return upName;
	}

	public void setUpName(String upName) {
		this.upName = upName;
	}

	public String getMenuLs() {
		return menuLs;
	}

	public void setMenuLs(String menuLs) {
		this.menuLs = menuLs;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(String menuLevel) {
		this.menuLevel = menuLevel;
	}

	public String getMenuUp() {
		return menuUp;
	}

	public void setMenuUp(String menuUp) {
		this.menuUp = menuUp;
	}

	public String getMenuIsshow() {
		return menuIsshow;
	}

	public void setMenuIsshow(String menuIsshow) {
		this.menuIsshow = menuIsshow;
	}

	public String getMenuIsnewwindow() {
		return menuIsnewwindow;
	}

	public void setMenuIsnewwindow(String menuIsnewwindow) {
		this.menuIsnewwindow = menuIsnewwindow;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public Integer getMenuNo() {
		return menuNo;
	}

	public void setMenuNo(Integer menuNo) {
		this.menuNo = menuNo;
	}

	public String getMenuImagepath() {
		return menuImagepath;
	}

	public void setMenuImagepath(String menuImagepath) {
		this.menuImagepath = menuImagepath;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String Status) {
		this.Status = Status;
	}

	public Integer getAreaLs() {
		return areaLs;
	}

	public void setAreaLs(Integer areaLs) {
		this.areaLs = areaLs;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getIssuper() {
		return issuper;
	}

	public void setIssuper(String issuper) {
		this.issuper = issuper;
	}

}