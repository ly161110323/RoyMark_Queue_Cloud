package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName(value = "br_anomaly")
public class Anomaly implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@TableId(value = "anomaly_hidden_id")
	private Long anomalyHiddenId;

    @TableField(value = "anomaly_event")
    private String anomalyEvent;

    @TableField(value = "anomaly_start_date")
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Timestamp anomalyStartDate;
    
    @TableField(value = "anomaly_end_date")
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Timestamp anomalyEndDate;

    @TableField(value = "anomaly_confidence")
    private double anomalyConfidence;

	@TableField(value = "anomaly_link")
    private String anomalyLink;

	@TableField(value = "cam_hidden_id")
	private Long camHiddenId;

    @TableField(value = "window_hidden_id")
    private Long windowHiddenId;

    @TableField(value = "user_hidden_id")
    private Long userHiddenId;

    @TableField(exist =  false)
	private String windowId;

    @TableField(exist = false)
	private String windowName;

    @TableField(exist = false)
	private String userName;

	public Long getAnomalyHiddenId() {
		return anomalyHiddenId;
	}

	public void setAnomalyHiddenId(Long anomalyHiddenId) {
		this.anomalyHiddenId = anomalyHiddenId;
	}

	public String getAnomalyEvent() {
		return anomalyEvent;
	}

	public void setAnomalyEvent(String anomalyEvent) {
		this.anomalyEvent = anomalyEvent;
	}

	public Timestamp getAnomalyStartDate() {
		return anomalyStartDate;
	}

	public void setAnomalyStartDate(Timestamp anomalyStartDate) {
		this.anomalyStartDate = anomalyStartDate;
	}

	public Timestamp getAnomalyEndDate() {
		return anomalyEndDate;
	}

	public void setAnomalyEndDate(Timestamp anomalyEndDate) {
		this.anomalyEndDate = anomalyEndDate;
	}

	public double getAnomalyConfidence() {
		return anomalyConfidence;
	}

	public void setAnomalyConfidence(double anomalyConfidence) {
		this.anomalyConfidence = anomalyConfidence;
	}

	public String getAnomalyLink() {
		return anomalyLink;
	}

	public void setAnomalyLink(String anomalyLink) {
		this.anomalyLink = anomalyLink;
	}

	public Long getWindowHiddenId() {
		return windowHiddenId;
	}

	public void setWindowHiddenId(Long windowHiddenId) {
		this.windowHiddenId = windowHiddenId;
	}

	public Long getUserHiddenId() {
		return userHiddenId;
	}

	public void setUserHiddenId(Long userHiddenId) {
		this.userHiddenId = userHiddenId;
	}

	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWindowId() {
		return windowId;
	}

	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}

	public Long getCamHiddenId() {
		return camHiddenId;
	}

	public void setCamHiddenId(Long camHiddenId) {
		this.camHiddenId = camHiddenId;
	}
}
