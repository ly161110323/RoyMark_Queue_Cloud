package com.roymark.queue.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "br_server")
public class Server implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "server_hidden_id")
	private Long serverHiddenId;


    @TableField(value = "server_id")
    private String serverId;

    @TableField(value = "server_name")
    private String serverName;

    @TableField(value = "server_ip")
    private String serverIp;

    @TableField(value = "server_port")
    private Long serverPort;

    @TableField(exist = false)
	private String serverStatus;

	@TableField(exist = false)
	private String programStatus;

	public Long getServerHiddenId() {
		return serverHiddenId;
	}

	public void setServerHiddenId(Long serverHiddenId) {
		this.serverHiddenId = serverHiddenId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public Long getServerPort() {
		return serverPort;
	}

	public void setServerPort(Long serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(String serverStatus) {
		this.serverStatus = serverStatus;
	}

	public String getProgramStatus() {
		return programStatus;
	}

	public void setProgramStatus(String programStatus) {
		this.programStatus = programStatus;
	}
}
