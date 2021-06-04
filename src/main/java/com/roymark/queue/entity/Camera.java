package com.roymark.queue.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    @TableField(value = "cam_coordinates")
	private String camCoordinates;

    @TableField(value = "server_hidden_id")
	private Long serverHiddenId;

	@TableField(exist = false)
	private String serverId;

	@TableField(value = "map_hidden_id")
	private Long mapHiddenId;

	@TableField(value = "group_hidden_id")
	private Long groupHiddenId;

	@TableField(exist = false)
	private String mapId;

	@TableField(exist = false)
	private String mapName;

	@TableField(exist = false)
	private String camStatus;

	@TableField(exist = false)
	private String groupId;

}
