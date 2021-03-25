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
    public class QueueRolepermissions implements Serializable {

private static final long serialVersionUID = 1L;

/**
 * 
 */
        @TableId(value="RolePermissions_Ls")
    private String rolepermissionsLs;
/**
 * 角色LS
 */
        @TableField(value="Role_Ls")
private String roleLs;
/**
 * 权限类型menu=菜单权限 case=项目权限
 */
        @TableField(value="Permissions_Type")
private String permissionsType;
/**
 * 菜单Ls
 */
        @TableField(value="Menu_Ls")
private String menuLs;
/**
 * 所属项目
 */
        @TableField(value="Area_Ls")
private Integer areaLs;
/**
 * 创建用户
 */
        @TableField(value="Create_User")
private String createUser;
/**
 * 创建时间
 */
        @TableField(value="Create_Time")
private LocalDateTime createTime;
/**
 * 修改人员
 */
        @TableField(value="Update_User")
private String updateUser;
/**
 * 修改时间
 */
        @TableField(value="Update_Time")
private LocalDateTime updateTime;
/**
 * 数据状态0=已删除，1=正常数据
 */
        @TableField(value="Status")
private String Status;


public String getRolepermissionsLs() {
        return rolepermissionsLs;
        }

    public void setRolepermissionsLs(String rolepermissionsLs) {
        this.rolepermissionsLs = rolepermissionsLs;
        }

public String getRoleLs() {
        return roleLs;
        }

    public void setRoleLs(String roleLs) {
        this.roleLs = roleLs;
        }

public String getPermissionsType() {
        return permissionsType;
        }

    public void setPermissionsType(String permissionsType) {
        this.permissionsType = permissionsType;
        }

public String getMenuLs() {
        return menuLs;
        }

    public void setMenuLs(String menuLs) {
        this.menuLs = menuLs;
        }

public Integer getAreaLs() {
        return areaLs;
        }

    public void setAreaLs(Integer areaLs) {
        this.areaLs = areaLs;
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

        }