package com.roymark.queue.entity;

import java.io.Serializable;
import java.util.Date;

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

}
