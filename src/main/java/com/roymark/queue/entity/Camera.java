package com.roymark.queue.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "cam")
public class Camera implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @TableId(value = "cam_id")
    private String id;

    @TableField(value = "cam_video_addr")
    private String videoAddr;

    @TableField(value = "cam_mac_addr")
    private String macAddr;

    @TableField(value = "cam_brand")
    private String brand;

    @TableField(value = "cam_type")
    private String type;

    @TableField(value = "cam_birth")
    private Date birth;

    @TableField(value = "window_id")
    private String windowId;

    @TableField(value = "server_id")
	private String serverId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVideoAddr() {
		return videoAddr;
	}

	public void setVideoAddr(String videoAddr) {
		this.videoAddr = videoAddr;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getWindowId() {
		return windowId;
	}

	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
}
