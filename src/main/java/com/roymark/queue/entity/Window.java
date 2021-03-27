package com.roymark.queue.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "window")
public class Window implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "hidden_id")
	private Long hiddenId;

	public Long getHiddenId() {
		return hiddenId;
	}

	public void setHiddenId(Long hiddenId) {
		this.hiddenId = hiddenId;
	}

    @TableField(value = "window_id")
    private String id;

    @TableField(value = "window_name")
    private String name;

    @TableField(value = "window_department")
    private String department;

    @TableField(value = "window_event")
    private String event;

    @TableField(value = "floor_hidden_id")
    private Long floorId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Long getFloorId() {
		return floorId;
	}

	public void setFloorId(Long floorId) {
		this.floorId = floorId;
	}
    
}
