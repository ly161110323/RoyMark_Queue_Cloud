package com.roymark.queue.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "floor")
public class Floor implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "floor_hidden_id")
	private Long floorHiddenId;

    @TableField(value = "floor_id")
    private String floorId;

    @TableField(value = "floor_name")
    private String floorName;

	public Long getFloorHiddenId() {
		return floorHiddenId;
	}

	public void setFloorHiddenId(Long floorHiddenId) {
		this.floorHiddenId = floorHiddenId;
	}

	public String getFloorId() {
		return floorId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
}
