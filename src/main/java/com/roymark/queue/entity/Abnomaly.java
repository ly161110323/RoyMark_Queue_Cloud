package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName(value = "abnomaly")
public class Abnomaly  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@TableId(value = "hidden_id")
	private Long hiddenId;

	public Long getHiddenId() {
		return hiddenId;
	}

	public void setHiddenId(Long hiddenId) {
		this.hiddenId = hiddenId;
	}

	@TableField(value = "abnomaly_id")
    private String id;

    @TableField(value = "abnomaly_event")
    private String event;

    @TableField(value = "abnomaly_start_date")
    private Timestamp startDate;
    
    @TableField(value = "abnomaly_end_date")
    private Timestamp endDate;

    @TableField(value = "abnomaly_confidence")
    private double confidence;

	@TableField(value = "abnomaly_link")
    private String link;

    @TableField(value = "window_hidden_id")
    private Long windowId;

    @TableField(value = "user_hidden_id")
    private Long userId;

    @TableField(exist = false)
	private String windowName;

    @TableField(exist = false)
	private String userName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}


	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Long getWindowId() {
		return windowId;
	}

	public void setWindowId(Long windowId) {
		this.windowId = windowId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
}
