package com.roymark.queue.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "server")
public class Server implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "hidden_id")
	private Long hiddenId;

	public Long getHiddenId() {
		return hiddenId;
	}

	public void setHiddenId(Long hiddenId) {
		this.hiddenId = hiddenId;
	}

    @TableField(value = "server_id")
    private String id;

    @TableField(value = "server_name")
    private String name;

    @TableField(value = "server_ip")
    private String ip;

    @TableField(value = "server_port")
    private Long port;

    @TableField(exist = false)
	private String serverStatus;

	@TableField(exist = false)
	private String programStatus;


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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
		this.port = port;
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
