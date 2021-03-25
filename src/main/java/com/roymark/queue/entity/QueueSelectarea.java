package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

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
 * @since 2020-03-23
 */
@Data
public class QueueSelectarea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "SelectArea_Ls", type = IdType.AUTO)
    private Integer selectareaLs;
    /**
     *
     */
    @TableField(value = "Area_Ls")
    private Integer areaLs;
    /**
     * 区域编号
     */
    @TableField(value = "SelectArea_Id")
    private String selectareaId;
    /**
     * 区域名称
     */
    @TableField(value = "SelectArea_Name")
    private String selectareaName;
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
     *
     */
    @TableField(value = "SelectArea_Print")
    private String selectareaPrint;
    /**
     * 排序号
     */
    @TableField(value = "SelectArea_OrderNo")
    private Integer selectareaOrderno;

    @TableField(exist =false)
   private String areaName;

    public Integer getSelectareaLs() {
        return selectareaLs;
    }

    public void setSelectareaLs(Integer selectareaLs) {
        this.selectareaLs = selectareaLs;
    }

    public Integer getAreaLs() {
        return areaLs;
    }

    public void setAreaLs(Integer areaLs) {
        this.areaLs = areaLs;
    }

    public String getSelectareaId() {
        return selectareaId;
    }

    public void setSelectareaId(String selectareaId) {
        this.selectareaId = selectareaId;
    }

    public String getSelectareaName() {
        return selectareaName;
    }

    public void setSelectareaName(String selectareaName) {
        this.selectareaName = selectareaName;
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

    public void setStatus(String status) {
        Status = status;
    }

    public String getSelectareaPrint() {
        return selectareaPrint;
    }

    public void setSelectareaPrint(String selectareaPrint) {
        this.selectareaPrint = selectareaPrint;
    }

    public Integer getSelectareaOrderno() {
        return selectareaOrderno;
    }

    public void setSelectareaOrderno(Integer selectareaOrderno) {
        this.selectareaOrderno = selectareaOrderno;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}