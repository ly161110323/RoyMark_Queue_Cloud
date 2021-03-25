package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2020-01-13
 */
@Data
public class QueueUserinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表流水号
     */
    @TableId(value = "UserInfo_Ls", type = IdType.AUTO)
    private Integer userinfoLs;
    /**
     * 所属委办局
     */
    @TableField(value = "Dept_Ls")
    private Integer deptLs;
    /**
     * 所属区域
     */
    @TableField(value = "Area_Ls")
    private Integer areaLs;
    /**
     * 人员编号
     */
    @TableField(value = "UserInfo_Id")
    private String userinfoId;
    /**
     * 姓名
     */
    @TableField(value = "UserInfo_Name")
    private String userinfoName;
    /**
     * 工号
     */
    @TableField(value = "UserInfo_Code")
    private String userinfoCode;
    /**
     * 性别1=男 ，2=女 ，0=未知
     */
    @TableField(value = "UserInfo_Sex")
    private String userinfoSex;
    /**
     * 出生年月
     */
    @TableField(value = "UserInfo_Birth")
    private String userinfoBirth;
    /**
     * 年龄
     */
    @TableField(value = "UserInfo_Age")
    private Integer userinfoAge;
    /**
     * 手机号码
     */
    @TableField(value = "UserInfo_Tel")
    private String userinfoTel;
    /**
     * 邮箱
     */
    @TableField(value = "UserInfo_Mail")
    private String userinfoMail;
    /**
     * 头像路径
     */
    @TableField(value = "UserInfo_ImagePath")
    private String userinfoImagepath;
    /**
     * 创建人
     */
    @TableField(value = "Create_user")
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
     * 数据状态0=已删除，1=正常数据
     */
    @TableField(value = "Status")
    private String Status;
    /**
     *
     */
    @TableField(value = "user_state")
    private Integer userState;
    /**
     * 登录密码
     */
    @TableField(value = "User_Password")
    private String userPassword;
    /**
     *
     */
    @TableField(value = "UserInfo_IsTheParty")
    private String userinfoIstheparty;
    /**
     *
     */
    @TableField(value = "UserInfo_Position")
    private String userinfoPosition;

    @TableField(value="UserInfo_Login")
    private String userinfoLogin;
    /**
     * 登录窗口
     */
    @TableField(value="Window_Ls")
    private Integer windowLs;
    /**
     *
     */
    @TableField(value="UserInfo_IsMatter")
    private String userinfoIsmatter;
    /**
     *
     */
    @TableField(value="Login_Time")
    private LocalDateTime loginTime;



    @TableField(exist = false)
    private String  webAccountLs;
    @TableField(exist = false)
    private String areaName;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private String positionName;


    public Integer getUserinfoLs() {
        return userinfoLs;
    }

    public void setUserinfoLs(Integer userinfoLs) {
        this.userinfoLs = userinfoLs;
    }

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

    public String getUserinfoId() {
        return userinfoId;
    }

    public void setUserinfoId(String userinfoId) {
        this.userinfoId = userinfoId;
    }

    public String getUserinfoName() {
        return userinfoName;
    }

    public void setUserinfoName(String userinfoName) {
        this.userinfoName = userinfoName;
    }

    public String getUserinfoCode() {
        return userinfoCode;
    }

    public void setUserinfoCode(String userinfoCode) {
        this.userinfoCode = userinfoCode;
    }

    public String getUserinfoSex() {
        return userinfoSex;
    }

    public void setUserinfoSex(String userinfoSex) {
        this.userinfoSex = userinfoSex;
    }

    public String getUserinfoBirth() {
        return userinfoBirth;
    }

    public void setUserinfoBirth(String userinfoBirth) {
        this.userinfoBirth = userinfoBirth;
    }

    public Integer getUserinfoAge() {
        return userinfoAge;
    }

    public void setUserinfoAge(Integer userinfoAge) {
        this.userinfoAge = userinfoAge;
    }

    public String getUserinfoTel() {
        return userinfoTel;
    }

    public void setUserinfoTel(String userinfoTel) {
        this.userinfoTel = userinfoTel;
    }

    public String getUserinfoMail() {
        return userinfoMail;
    }

    public void setUserinfoMail(String userinfoMail) {
        this.userinfoMail = userinfoMail;
    }

    public String getUserinfoImagepath() {
        return userinfoImagepath;
    }

    public void setUserinfoImagepath(String userinfoImagepath) {
        this.userinfoImagepath = userinfoImagepath;
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

    public Integer getUserState() {
        return userState;
    }

    public void setUserState(Integer userState) {
        this.userState = userState;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserinfoIstheparty() {
        return userinfoIstheparty;
    }

    public void setUserinfoIstheparty(String userinfoIstheparty) {
        this.userinfoIstheparty = userinfoIstheparty;
    }

    public String getUserinfoPosition() {
        return userinfoPosition;
    }

    public void setUserinfoPosition(String userinfoPosition) {
        this.userinfoPosition = userinfoPosition;
    }

    public String getUserinfoLogin() {
        return userinfoLogin;
    }

    public void setUserinfoLogin(String userinfoLogin) {
        this.userinfoLogin = userinfoLogin;
    }

    public Integer getWindowLs() {
        return windowLs;
    }

    public void setWindowLs(Integer windowLs) {
        this.windowLs = windowLs;
    }

    public String getUserinfoIsmatter() {
        return userinfoIsmatter;
    }

    public void setUserinfoIsmatter(String userinfoIsmatter) {
        this.userinfoIsmatter = userinfoIsmatter;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public String getWebAccountLs() {
        return webAccountLs;
    }

    public void setWebAccountLs(String webAccountLs) {
        this.webAccountLs = webAccountLs;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}