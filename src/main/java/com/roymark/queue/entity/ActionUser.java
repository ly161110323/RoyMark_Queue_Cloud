package com.roymark.queue.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class ActionUser implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@TableId(value = "user_hidden_id")
    private Long userHiddenId;

	@TableField(value = "user_id")
    private String userId;

	@TableField(value = "user_pwd")
	private String userPwd;
	
    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "user_sex")
    private String userSex;

    @TableField(value = "user_department")
    private String userDepartment;

    @TableField(value = "user_post")
    private String userPost;

    @TableField(value = "user_photo")
    private String userPhoto;

    @TableField(value = "window_hidden_id")
    private Long windowHiddenId;

    @TableField(exist = false)
    private String windowId;

    public String getWindowId() {
        return windowId;
    }

    public void setWindowId(String windowId) {
        this.windowId = windowId;
    }

    public Long getUserHiddenId() {
        return userHiddenId;
    }

    public void setUserHiddenId(Long userHiddenId) {
        this.userHiddenId = userHiddenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(String userDepartment) {
        this.userDepartment = userDepartment;
    }

    public String getUserPost() {
        return userPost;
    }

    public void setUserPost(String userPost) {
        this.userPost = userPost;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Long getWindowHiddenId() {
        return windowHiddenId;
    }

    public void setWindowHiddenId(Long windowHiddenId) {
        this.windowHiddenId = windowHiddenId;
    }
}
