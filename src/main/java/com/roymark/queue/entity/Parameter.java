package com.roymark.queue.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "param")
public class Parameter implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "param_hidden_id")
	private Long paramHiddenId;

    @TableField(value = "param_id")
    private String paramId;

    @TableField(value = "param_name")
    private String paramName;

    @TableField(value = "param_value")
    private String paramValue;

    @TableField(value = "param_default")
    private String paramDefault;

    @TableField(value = "param_remark")
    private String paramRemark;

	public Long getParamHiddenId() {
		return paramHiddenId;
	}

	public void setParamHiddenId(Long paramHiddenId) {
		this.paramHiddenId = paramHiddenId;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamDefault() {
		return paramDefault;
	}

	public void setParamDefault(String paramDefault) {
		this.paramDefault = paramDefault;
	}

	public String getParamRemark() {
		return paramRemark;
	}

	public void setParamRemark(String paramRemark) {
		this.paramRemark = paramRemark;
	}
}
