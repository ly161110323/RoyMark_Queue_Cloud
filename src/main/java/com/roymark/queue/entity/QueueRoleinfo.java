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
public class QueueRoleinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "Role_Ls")
    private String roleLs;
    /**
     * 角色编号
     */
    @TableField(value = "Role_Code")
    private String roleCode;
    /**
     * 角色名称
     */
    @TableField(value = "Role_Name")
    private String roleName;
    /**
     * 菜单层级
     */
    @TableField(value = "Role_IsShow")
    private String roleIsshow;
    /**
     * 备注
     */
    @TableField(value = "Role_Text")
    private String roleText;
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
     *
     */
    @TableField(value = "area_Ls")
    private Integer areaLs;

    @TableField(exist = false)
    private String  areaName;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getRoleLs() {
        return roleLs;
    }

    public void setRoleLs(String roleLs) {
        this.roleLs = roleLs;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleIsshow() {
        return roleIsshow;
    }

    public void setRoleIsshow(String roleIsshow) {
        this.roleIsshow = roleIsshow;
    }

    public String getRoleText() {
        return roleText;
    }

    public void setRoleText(String roleText) {
        this.roleText = roleText;
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

}