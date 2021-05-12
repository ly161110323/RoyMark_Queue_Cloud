package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "br_face_")
public class Face implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField(value = "face_id")
    private Long faceId;

    @TableField(value = "user_id")
    private Long userId;
}
