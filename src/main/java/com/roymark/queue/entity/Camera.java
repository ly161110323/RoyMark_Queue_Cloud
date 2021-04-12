package com.roymark.queue.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
@TableName(value = "br_cam")
public class Camera implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "cam_hidden_id")
	private Long camHiddenId;

    @TableField(value = "cam_id")
    private String camId;

    @TableField(value = "cam_ip")
	private String camIp;

    @TableField(value = "cam_video_addr")
    private String camVideoAddr;

    @TableField(value = "cam_mac_addr")
    private String camMacAddr;

    @TableField(value = "cam_brand")
    private String camBrand;

    @TableField(value = "cam_type")
    private String camType;

    @TableField(value = "cam_birth")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date camBirth;

    @TableField(value = "window_hidden_id")
    private Long windowHiddenId;

    @TableField(value = "server_hidden_id")
	private Long serverHiddenId;

    @TableField(exist = false)
	private String windowId;

	@TableField(exist = false)
	private String serverId;


	@TableField(exist = false)
	private String camStatus;

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

	public String getCamVideoAddr() {
		return camVideoAddr;
	}

	public void setCamVideoAddr(String camVideoAddr) {
		this.camVideoAddr = camVideoAddr;
	}

	public String getCamMacAddr() {
		return camMacAddr;
	}

	public void setCamMacAddr(String camMacAddr) {
		this.camMacAddr = camMacAddr;
	}

	public String getCamBrand() {
		return camBrand;
	}

	public void setCamBrand(String camBrand) {
		this.camBrand = camBrand;
	}

	public String getCamType() {
		return camType;
	}

	public void setCamType(String camType) {
		this.camType = camType;
	}

	public Date getCamBirth() {
		return camBirth;
	}

	public void setCamBirth(Date camBirth) {
		this.camBirth = camBirth;
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

	public String getCamStatus() {
		return camStatus;
	}

	public void setCamStatus(String camStatus) {
		this.camStatus = camStatus;
	}

	public String getCamIp() {
		return camIp;
	}

	public void setCamIp(String camIp) {
		this.camIp = camIp;
	}

	public Long getWindowHiddenId() {
		return windowHiddenId;
	}

	public void setWindowHiddenId(Long windowHiddenId) {
		this.windowHiddenId = windowHiddenId;
	}

	public Long getServerHiddenId() {
		return serverHiddenId;
	}

	public void setServerHiddenId(Long serverHiddenId) {
		this.serverHiddenId = serverHiddenId;
	}
}
