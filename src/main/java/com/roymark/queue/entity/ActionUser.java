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

	@TableId(value = "hidden_id")
    private Long hiddenId;

    public Long getHiddenId() {
        return hiddenId;
    }

    public void setHiddenId(Long hiddenId) {
        this.hiddenId = hiddenId;
    }
	@TableField(value = "user_id")
    private String id;

	@TableField(value = "user_pwd")
	private String pwd;
	
    @TableField(value = "user_name")
    private String name;

    @TableField(value = "user_sex")
    private String sex;

    @TableField(value = "user_department")
    private String department;

    @TableField(value = "user_post")
    private String post;

    @TableField(value = "user_photo")
    private String photo;

    @TableField(value = "window_hidden_id")
    private Long windowId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getWindowId() {
        return windowId;
    }

    public void setWindowId(Long windowId) {
        this.windowId = windowId;
    }
}
