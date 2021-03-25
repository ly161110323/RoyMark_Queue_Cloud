package com.roymark.queue.entity;

import java.io.Serializable;
import java.util.Date;


public class User implements Serializable {
    private long freeSpace;
    private long usableSpace;
    private long totalSpace;


    /** 管理员ID  */
    private String id;
    /** 员工的ID  */
    private String empID;
    /** 管理员姓名 */
    private String username;
    /** 管理员姓名 */
    private String name;
    /** 登录者姓名 */
    private String employee_name;
    /** 登录者所在部门 */
    private String department_id;
    /** 登录者员工ID */
    private String employee_id;
    /**登陆记录id*/
    private String sm_loginuser_history_id;
    /**登陆IP*/
    private String loginUser_ip;
    /**删除时间(选择时间之前的记录)*/
    private Date delDate;
    /**登陆ID*/
    private String userID;
    /**登陆时间 */
    private Date loginTime;
    /** 登录者所在部门 */
    private String department_name;
    /** 登录者所属职位 */
    private String jobpost_name;
    /** 登录者所属职位id */
    private String jobpost_id;
    /** 密码  */
    private String userpassword;
    /** 密码  */
    private String password;
    /** 原始密码  */
    private String oldPassword;
    /** 权限  */
    private int role;
    /**登陆出错标识*/
    private String msg;
    private String type;
    private String type_name;
    private String usergroup;
    private String groupName;
    /**审查权限*/
    private String checkauthor;
    /** 批准权限*/
    private String approvalauthor;
    private String email;
    /**日志管理权限*/
    private String logpermission;

    /**地图管理权限*/
    private String mappermission;
    //20161117
    private String roleid;
    private String rolename;
    /**优先级*/
    private String priority;
    /**指定用户组*/
    private String df_usergroup;
    /**指定用户组名*/
    private String df_groupname;

    private Integer topAreaLs;//add by wfz 20190117 当前用户有权限的顶层areadLs,超级管理员为0(拥有0节点下递归的所有areaLs的权限)

    public Integer getTopAreaLs() {
        return topAreaLs;
    }

    public void setTopAreaLs(Integer topAreaLs) {
        this.topAreaLs = topAreaLs;
    }

    public long getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(long freeSpace) {
        this.freeSpace = freeSpace;
    }

    public long getUsableSpace() {
        return usableSpace;
    }

    public void setUsableSpace(long usableSpace) {
        this.usableSpace = usableSpace;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getSm_loginuser_history_id() {
        return sm_loginuser_history_id;
    }

    public void setSm_loginuser_history_id(String sm_loginuser_history_id) {
        this.sm_loginuser_history_id = sm_loginuser_history_id;
    }

    public String getLoginUser_ip() {
        return loginUser_ip;
    }

    public void setLoginUser_ip(String loginUser_ip) {
        this.loginUser_ip = loginUser_ip;
    }

    public Date getDelDate() {
        return delDate;
    }

    public void setDelDate(Date delDate) {
        this.delDate = delDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getJobpost_name() {
        return jobpost_name;
    }

    public void setJobpost_name(String jobpost_name) {
        this.jobpost_name = jobpost_name;
    }

    public String getJobpost_id() {
        return jobpost_id;
    }

    public void setJobpost_id(String jobpost_id) {
        this.jobpost_id = jobpost_id;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(String usergroup) {
        this.usergroup = usergroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCheckauthor() {
        return checkauthor;
    }

    public void setCheckauthor(String checkauthor) {
        this.checkauthor = checkauthor;
    }

    public String getApprovalauthor() {
        return approvalauthor;
    }

    public void setApprovalauthor(String approvalauthor) {
        this.approvalauthor = approvalauthor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogpermission() {
        return logpermission;
    }

    public void setLogpermission(String logpermission) {
        this.logpermission = logpermission;
    }

    public String getMappermission() {
        return mappermission;
    }

    public void setMappermission(String mappermission) {
        this.mappermission = mappermission;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDf_usergroup() {
        return df_usergroup;
    }

    public void setDf_usergroup(String df_usergroup) {
        this.df_usergroup = df_usergroup;
    }

    public String getDf_groupname() {
        return df_groupname;
    }

    public void setDf_groupname(String df_groupname) {
        this.df_groupname = df_groupname;
    }
}
