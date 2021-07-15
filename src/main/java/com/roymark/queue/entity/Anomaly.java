package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "br_anomaly")
@JsonIgnoreProperties(value = {"handler"})
public class Anomaly implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@TableId(value = "anomaly_hidden_id")
	private Long anomalyHiddenId;

    @TableField(value = "anomaly_event")
    private String anomalyEvent;

    @TableField(value = "anomaly_start_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp anomalyStartDate;
    
    @TableField(value = "anomaly_end_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp anomalyEndDate;


    @TableField(value = "anomaly_confidence")
    private double anomalyConfidence;


	@TableField(value = "anomaly_image_path")
	private String anomalyImagePath;


	@TableField(value = "anomaly_video_path")
    private String anomalyVideoPath;

	@TableField(value = "cam_hidden_id")
	private Long camHiddenId;

    @TableField(value = "window_hidden_id")
    private Long windowHiddenId;

    // 默认的绑定用户
    @TableField(value = "user_hidden_id")
    private Long userHiddenId;

    @TableField(value = "anomaly_status")
	private String anomalyStatus;

    @TableField(value = "anomaly_face_confidence")
    private double anomalyFaceConfidence;

    // 是否是默认用户的标志,默认为真
    @TableField(value = "default_user_flag")
    private Boolean defaultUserFlag;

    @TableField(exist =  false)
	private String windowId;

    @TableField(exist = false)
	private String windowName;

    @TableField(exist = false)
    private List<UserShortInfo> userShortInfos;

}
