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
public class QueueCasearea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "CaseArea_Ls", type = IdType.AUTO)
    private Integer caseareaLs;
    /**
     *
     */
    @TableField(value = "Area_Ls")
    private Integer areaLs;
    /**
     *
     */
    @TableField(value = "CaseArea_Id")
    private String caseareaId;
    /**
     *
     */
    @TableField(value = "CaseArea_Name")
    private String caseareaName;
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
    @TableField(value = "CaseArea_Print")
    private String caseareaPrint;
    /**
     * 排序号
     */
    @TableField(value = "CaseArea_OrderNo")
    private Integer caseareaOrderno;
    /**
     *
     */
    @TableField(value = "HallArea_Ls")
    private String hallareaLs;
    /**
     *
     */
    @TableField(value = "CaseArea_IsUrl")
    private String caseareaIsurl;
    /**
     *
     */
    @TableField(value = "CaseArea_AppUrlStr")
    private String caseareaAppurlstr;
    /**
     *
     */
    @TableField(value = "CaseArea_UrlStr")
    private String caseareaUrlstr;

    @TableField(exist=false)
    private String areaName;

    @TableField(exist=false)
    private String hallareaName;

    public Integer getCaseareaLs() {
        return caseareaLs;
    }

    public void setCaseareaLs(Integer caseareaLs) {
        this.caseareaLs = caseareaLs;
    }

    public Integer getAreaLs() {
        return areaLs;
    }

    public void setAreaLs(Integer areaLs) {
        this.areaLs = areaLs;
    }

    public String getCaseareaId() {
        return caseareaId;
    }

    public void setCaseareaId(String caseareaId) {
        this.caseareaId = caseareaId;
    }

    public String getCaseareaName() {
        return caseareaName;
    }

    public void setCaseareaName(String caseareaName) {
        this.caseareaName = caseareaName;
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

    public String getCaseareaPrint() {
        return caseareaPrint;
    }

    public void setCaseareaPrint(String caseareaPrint) {
        this.caseareaPrint = caseareaPrint;
    }

    public Integer getCaseareaOrderno() {
        return caseareaOrderno;
    }

    public void setCaseareaOrderno(Integer caseareaOrderno) {
        this.caseareaOrderno = caseareaOrderno;
    }

    public String getHallareaLs() {
        return hallareaLs;
    }

    public void setHallareaLs(String hallareaLs) {
        this.hallareaLs = hallareaLs;
    }

    public String getCaseareaIsurl() {
        return caseareaIsurl;
    }

    public void setCaseareaIsurl(String caseareaIsurl) {
        this.caseareaIsurl = caseareaIsurl;
    }

    public String getCaseareaAppurlstr() {
        return caseareaAppurlstr;
    }

    public void setCaseareaAppurlstr(String caseareaAppurlstr) {
        this.caseareaAppurlstr = caseareaAppurlstr;
    }

    public String getCaseareaUrlstr() {
        return caseareaUrlstr;
    }

    public void setCaseareaUrlstr(String caseareaUrlstr) {
        this.caseareaUrlstr = caseareaUrlstr;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getHallareaName() {
        return hallareaName;
    }

    public void setHallareaName(String hallareaName) {
        this.hallareaName = hallareaName;
    }
}