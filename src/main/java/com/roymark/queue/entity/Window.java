package com.roymark.queue.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "br_window")
public class Window implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "window_hidden_id")
	private Long windowHiddenId;

    @TableField(value = "window_id")
    private String windowId;

    @TableField(value = "window_name")
    private String windowName;

    @TableField(value = "window_department")
    private String windowDepartment;

    @TableField(value = "window_event")
    private String windowEvent;

	@TableField(value = "window_action_analysis")
	private Boolean windowActionAnalysis;

    @TableField(value = "window_nine_palaces")
	private Boolean windowNinePalaces;

    @TableField(value = "window_coordinates")
	private String windowCoordinates;

    @TableField(value = "cam_hidden_id")
	private Long camHiddenId;

    @TableField(value = "user_hidden_id")
	private Long userHiddenId;

    @TableField(value = "user_update_time")
	private Date userUpdateTime;

    @TableField(exist = false)
	private String camId;

	public Date getUserUpdateTime() {
		return userUpdateTime;
	}

	public void setUserUpdateTime(Date userUpdateTime) {
		this.userUpdateTime = userUpdateTime;
	}

	public Long getUserHiddenId() {
		return userHiddenId;
	}

	public void setUserHiddenId(Long userHiddenId) {
		this.userHiddenId = userHiddenId;
	}

	public Long getCamHiddenId() {
		return camHiddenId;
	}

	public void setCamHiddenId(Long camHiddenId) {
		this.camHiddenId = camHiddenId;
	}

	public String getCamId() {
		return camId;
	}

	public void setCamId(String camId) {
		this.camId = camId;
	}

	public Long getWindowHiddenId() {
		return windowHiddenId;
	}

	public void setWindowHiddenId(Long windowHiddenId) {
		this.windowHiddenId = windowHiddenId;
	}

	public String getWindowId() {
		return windowId;
	}

	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}

	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	public String getWindowDepartment() {
		return windowDepartment;
	}

	public void setWindowDepartment(String windowDepartment) {
		this.windowDepartment = windowDepartment;
	}

	public String getWindowEvent() {
		return windowEvent;
	}

	public void setWindowEvent(String windowEvent) {
		this.windowEvent = windowEvent;
	}

	public Boolean getWindowActionAnalysis() {
		return windowActionAnalysis;
	}

	public void setWindowActionAnalysis(Boolean windowActionAnalysis) {
		this.windowActionAnalysis = windowActionAnalysis;
	}

	public Boolean getWindowNinePalaces() {
		return windowNinePalaces;
	}

	public void setWindowNinePalaces(Boolean windowNinePalaces) {
		this.windowNinePalaces = windowNinePalaces;
	}

	public String getWindowCoordinates() {
		return windowCoordinates;
	}

	public void setWindowCoordinates(String windowCoordinates) {
		this.windowCoordinates = windowCoordinates;
	}
}
