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
@TableName(value = "br_user")
public class ActionUser implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@TableId(value = "user_hidden_id")
    private Long userHiddenId;

	@TableField(value = "user_id")
    private String userId;
	
    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "user_sex")
    private String userSex;

    @TableField(value = "user_department")
    private String userDepartment;

    @TableField(value = "user_post")
    private String userPost;

    @TableField(value = "user_photo")
    private String userPhoto;

    @TableField(value = "window_hidden_id")
    private Long windowHiddenId;

    @TableField(exist = false)
    private String windowId;

}
