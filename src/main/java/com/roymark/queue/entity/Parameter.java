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
@TableName(value = "br_param")
public class Parameter implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "param_hidden_id")
	private Long paramHiddenId;

    @TableField(value = "param_name")
    private String paramName;

    @TableField(value = "param_value")
    private String paramValue;

    @TableField(value = "param_default")
    private String paramDefault;

    @TableField(value = "param_remark")
    private String paramRemark;

}
