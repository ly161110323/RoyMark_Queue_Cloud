package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

@Data
@TableName(value = "br_group")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "group_hidden_id")
    private Long groupHiddenId;

    @TableField(value = "group_id")
    private String groupId;

}
