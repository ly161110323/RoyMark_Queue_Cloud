package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName(value = "br_abnomaly")
public class Abnomaly  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@TableId(value = "abnomaly_hidden_id")
	private Long abnomalyHiddenId;

	@TableField(value = "abnomaly_id")
    private String abnomalyId;

    @TableField(value = "abnomaly_event")
    private String abnomalyEvent;

    @TableField(value = "abnomaly_start_date")
    private Timestamp abnomalyStartDate;
    
    @TableField(value = "abnomaly_end_date")
    private Timestamp abnomalyEndDate;

    @TableField(value = "abnomaly_confidence")
    private double abnomalyConfidence;

	@TableField(value = "abnomaly_link")
    private String abnomalyLink;

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

	public Long getAbnomalyHiddenId() {
		return abnomalyHiddenId;
	}

	public void setAbnomalyHiddenId(Long abnomalyHiddenId) {
		this.abnomalyHiddenId = abnomalyHiddenId;
	}

	public String getAbnomalyId() {
		return abnomalyId;
	}

	public void setAbnomalyId(String abnomalyId) {
		this.abnomalyId = abnomalyId;
	}

	public String getAbnomalyEvent() {
		return abnomalyEvent;
	}

	public void setAbnomalyEvent(String abnomalyEvent) {
		this.abnomalyEvent = abnomalyEvent;
	}

	public Timestamp getAbnomalyStartDate() {
		return abnomalyStartDate;
	}

	public void setAbnomalyStartDate(Timestamp abnomalyStartDate) {
		this.abnomalyStartDate = abnomalyStartDate;
	}

	public Timestamp getAbnomalyEndDate() {
		return abnomalyEndDate;
	}

	public void setAbnomalyEndDate(Timestamp abnomalyEndDate) {
		this.abnomalyEndDate = abnomalyEndDate;
	}

	public double getAbnomalyConfidence() {
		return abnomalyConfidence;
	}

	public void setAbnomalyConfidence(double abnomalyConfidence) {
		this.abnomalyConfidence = abnomalyConfidence;
	}

	public String getAbnomalyLink() {
		return abnomalyLink;
	}

	public void setAbnomalyLink(String abnomalyLink) {
		this.abnomalyLink = abnomalyLink;
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
}
