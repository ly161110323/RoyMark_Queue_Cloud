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
public class QueueWebaccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表流水号
     */
    @TableId(value = "WebAccount_Ls")
    private String webaccountLs;
    /**
     * 所属人员
     */
    @TableField(value = "UserInfo_Ls")
    private Integer userinfoLs;

    //人员姓名
    @TableField(exist = false, value = "UserInfo_Name")
    private String userInfoName;
    /**
     * 所属项目
     */
    @TableField(value = "Area_Ls")
    private Integer areaLs;
    //所属项目名称
    @TableField(exist = false, value = "Area_Name") //add by wfz 2020-01-17
    private String areaName;
    /**
     * 登录账号
     */
    @TableField(value = "WebAccount_LogIn")
    private String webaccountLogin;
    /**
     * 账号名称
     */
    @TableField(value = "WebAccount_Name")
    private String webaccountName;
    /**
     * 账号编号
     */
    @TableField(value = "WebAccount_Code")
    private String webaccountCode;
    /**
     * 密码
     */
    @TableField(value = "WebAccount_Password")
    private String webaccountPassword;
    /**
     * 备注
     */
    @TableField(value = "WebAccount_Text")
    private String webaccountText;
    /**
     * 头像路径
     */
    @TableField(value = "WebAccount_Image")
    private String webaccountImage;
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

    public String getUserInfoName() {
        return userInfoName;
    }

    public void setUserInfoName(String userInfoName) {
        this.userInfoName = userInfoName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getWebaccountLs() {
        return webaccountLs;
    }

    public void setWebaccountLs(String webaccountLs) {
        this.webaccountLs = webaccountLs;
    }

    public Integer getUserinfoLs() {
        return userinfoLs;
    }

    public void setUserinfoLs(Integer userinfoLs) {
        this.userinfoLs = userinfoLs;
    }

    public Integer getAreaLs() {
        return areaLs;
    }

    public void setAreaLs(Integer areaLs) {
        this.areaLs = areaLs;
    }

    public String getWebaccountLogin() {
        return webaccountLogin;
    }

    public void setWebaccountLogin(String webaccountLogin) {
        this.webaccountLogin = webaccountLogin;
    }

    public String getWebaccountName() {
        return webaccountName;
    }

    public void setWebaccountName(String webaccountName) {
        this.webaccountName = webaccountName;
    }

    public String getWebaccountCode() {
        return webaccountCode;
    }

    public void setWebaccountCode(String webaccountCode) {
        this.webaccountCode = webaccountCode;
    }

    public String getWebaccountPassword() {
        return webaccountPassword;
    }

    public void setWebaccountPassword(String webaccountPassword) {
        this.webaccountPassword = webaccountPassword;
    }

    public String getWebaccountText() {
        return webaccountText;
    }

    public void setWebaccountText(String webaccountText) {
        this.webaccountText = webaccountText;
    }

    public String getWebaccountImage() {
        return webaccountImage;
    }

    public void setWebaccountImage(String webaccountImage) {
        this.webaccountImage = webaccountImage;
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