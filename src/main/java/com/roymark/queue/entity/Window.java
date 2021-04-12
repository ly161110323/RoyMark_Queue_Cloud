package com.roymark.queue.entity;

import java.io.Serializable;

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

    @TableField(value = "floor_Hidden_id")
    private Long floorHiddenId;

	@TableField(exist = false)
	private String floorId;

	@TableField(value = "window_action_analysis")
	private Boolean windowActionAnalysis;

    @TableField(value = "window_nine_palaces")
	private Boolean windowNinePalaces;

    @TableField(exist = false)
	private String floorName;

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
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

	public String getFloorId() {
		return floorId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
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

	public Long getFloorHiddenId() {
		return floorHiddenId;
	}

	public void setFloorHiddenId(Long floorHiddenId) {
		this.floorHiddenId = floorHiddenId;
	}
}
