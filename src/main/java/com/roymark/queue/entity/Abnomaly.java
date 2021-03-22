package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "abnomaly")
public class Abnomaly  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @TableId(value = "abnomaly_id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "abnomaly_event")
    private String event;

    @TableField(value = "abnomaly_date")
    private Date date;

    @TableField(value = "abnomaly_link")
    private String Link;

    @TableField(value = "window_id")
    private Long windowId;

    @TableField(value = "user_id")
    private String userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLink() {
		return Link;
	}

	public void setLink(String link) {
		Link = link;
	}

	public Long getWindowId() {
		return windowId;
	}

	public void setWindowId(Long windowId) {
		this.windowId = windowId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

    
}
