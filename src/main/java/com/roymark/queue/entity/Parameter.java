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

	@TableId(value = "hidden_id")
	private Long hiddenId;

	public Long getHiddenId() {
		return hiddenId;
	}

	public void setHiddenId(Long hiddenId) {
		this.hiddenId = hiddenId;
	}

    @TableField(value = "param_id")
    private String id;

    @TableField(value = "param_name")
    private String name;

    @TableField(value = "param_value")
    private String value;

    @TableField(value = "param_default")
    private String defalutValue;

    @TableField(value = "param_remark")
    private String remark;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDefalutValue() {
		return defalutValue;
	}

	public void setDefalutValue(String defalutValue) {
		this.defalutValue = defalutValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
