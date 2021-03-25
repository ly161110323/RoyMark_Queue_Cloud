package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class QueueArea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表流水号
     */
    @TableId(value = "Area_Ls",type = IdType.AUTO)
    private Integer areaLs;
    /**
     * 区域编号
     */
    @TableField(value = "Area_Id")
    private String areaId;
    /**
     * 区域名称
     */
    @TableField(value = "Area_Name")
    private String areaName;
    /**
     * 是否默认区域
     */
    @TableField(value = "Area_IsDefault")
    private String areaIsdefault;
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
     * 最后一次修改用户
     */
    @TableField(value = "Update_User")
    private String updateUser;
    /**
     * 最后一次修改时间
     */
    @TableField(value = "Update_Time")
    private LocalDateTime updateTime;
    /**
     * 数据状态
     */
    @TableField(value = "Status")
    private String Status;
    /**
     * 顺序号
     */
    @TableField(value = "Area_OrderNo")
    private Integer areaOrderno;
    /**
     *
     */
    @TableField(value = "parent_area_ls")
    private Integer parentAreaLs;


    /**
     *
     */
    @TableField(value = "dbconnect")
    private String dbconnect;


    @TableField(exist = false)
    private String parentAreaName;  //bean新增的父节点名字字段，程序载入


    public String getParentAreaName() {
        return parentAreaName;
    }

    public void setParentAreaName(String parentAreaName) {
        this.parentAreaName = parentAreaName;
    }

    public Integer getAreaLs() {
        return areaLs;
    }

    public void setAreaLs(Integer areaLs) {
        this.areaLs = areaLs;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaIsdefault() {
        return areaIsdefault;
    }

    public void setAreaIsdefault(String areaIsdefault) {
        this.areaIsdefault = areaIsdefault;
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

    public Integer getAreaOrderno() {
        return areaOrderno;
    }

    public void setAreaOrderno(Integer areaOrderno) {
        this.areaOrderno = areaOrderno;
    }

    public Integer getParentAreaLs() {
        return parentAreaLs;
    }

    public void setParentAreaLs(Integer parentAreaLs) {
        this.parentAreaLs = parentAreaLs;
    }

    public String getDbconnect() {
        return dbconnect;
    }

    public void setDbconnect(String dbconnect) {
        this.dbconnect = dbconnect;
    }

}