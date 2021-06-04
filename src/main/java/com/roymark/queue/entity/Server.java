package com.roymark.queue.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

}
