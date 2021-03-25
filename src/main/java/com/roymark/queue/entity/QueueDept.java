package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wangfz
 * @since 2020-02-06
 */
@Data
public class QueueDept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表流水号
     */
    @TableId(value = "Dept_Ls",type = IdType.AUTO)
    private Integer deptLs;
    /**
     * 所属区域
     */
    @TableField(value = "Area_Ls")
    private Integer areaLs;
    /**
     * 委办局编号
     */
    @TableField(value = "Dept_Id")
    private String deptId;
    /**
     * 委办局名称
     */
    @TableField(value = "Dept_Name")
    private String deptName;
    /**
     * 打印前缀
     */
    @TableField(value = "Dept_Print")
    private String deptPrint;
    /**
     * 显示顺序
     */
    @TableField(value = "Dept_OrderNo")
    private Integer deptOrderno;
    /**
     * 现场取号上限
     */
    @TableField(value = "Dept_MaxTakeNo")
    private Integer deptMaxtakeno;
    /**
     * 现场预约上限
     */
    @TableField(value = "Dept_MaxAppointment")
    private Integer deptMaxappointment;
    /**
     * 部门图标路径
     */
    @TableField(value = "Dept_ImagePath")
    private String deptImagepath;
    /**
     *
     */
    @TableField(value = "Py_Code")
    private String pyCode;
    /**
     *
     */
    @TableField(value = "Wb_Code")
    private String wbCode;
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
     * 区域流水号
     */
    @TableField(value = "CaseArea_Ls")
    private Integer caseareaLs;
    /**
     *
     */
    @TableField(value = "DEPT_STATE")
    private String deptState;
    /**
     * 行政区域流水号
     */
    @TableField(value = "SelectArea_Ls")
    private Integer selectareaLs;
    /**
     * 第三方系统唯一编号
     */
    @TableField(value = "Other_Id")
    private String otherId;

    @TableField(exist = false)
    private String isHaveUser;

    @TableField(exist = false)
    private String isHaveMatter;

    @TableField(exist = false)
    private List<Integer> caseareaLsList; //用来做in 判断过滤

    public Integer getDeptLs() {
        return deptLs;
    }

    public void setDeptLs(Integer deptLs) {
        this.deptLs = deptLs;
    }

    public Integer getAreaLs() {
        return areaLs;
    }

    public void setAreaLs(Integer areaLs) {
        this.areaLs = areaLs;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptPrint() {
        return deptPrint;
    }

    public void setDeptPrint(String deptPrint) {
        this.deptPrint = deptPrint;
    }

    public Integer getDeptOrderno() {
        return deptOrderno;
    }

    public void setDeptOrderno(Integer deptOrderno) {
        this.deptOrderno = deptOrderno;
    }

    public Integer getDeptMaxtakeno() {
        return deptMaxtakeno;
    }

    public void setDeptMaxtakeno(Integer deptMaxtakeno) {
        this.deptMaxtakeno = deptMaxtakeno;
    }

    public Integer getDeptMaxappointment() {
        return deptMaxappointment;
    }

    public void setDeptMaxappointment(Integer deptMaxappointment) {
        this.deptMaxappointment = deptMaxappointment;
    }

    public String getDeptImagepath() {
        return deptImagepath;
    }

    public void setDeptImagepath(String deptImagepath) {
        this.deptImagepath = deptImagepath;
    }

    public String getPyCode() {
        return pyCode;
    }

    public void setPyCode(String pyCode) {
        this.pyCode = pyCode;
    }

    public String getWbCode() {
        return wbCode;
    }

    public void setWbCode(String wbCode) {
        this.wbCode = wbCode;
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

    public Integer getCaseareaLs() {
        return caseareaLs;
    }

    public void setCaseareaLs(Integer caseareaLs) {
        this.caseareaLs = caseareaLs;
    }

    public String getDeptState() {
        return deptState;
    }

    public void setDeptState(String deptState) {
        this.deptState = deptState;
    }

    public Integer getSelectareaLs() {
        return selectareaLs;
    }

    public void setSelectareaLs(Integer selectareaLs) {
        this.selectareaLs = selectareaLs;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getIsHaveUser() {
        return isHaveUser;
    }

    public void setIsHaveUser(String isHaveUser) {
        this.isHaveUser = isHaveUser;
    }

    public String getIsHaveMatter() {
        return isHaveMatter;
    }

    public void setIsHaveMatter(String isHaveMatter) {
        this.isHaveMatter = isHaveMatter;
    }

    public List<Integer> getCaseareaLsList() {
        return caseareaLsList;
    }

    public void setCaseareaLsList(List<Integer> caseareaLsList) {
        this.caseareaLsList = caseareaLsList;
    }
}