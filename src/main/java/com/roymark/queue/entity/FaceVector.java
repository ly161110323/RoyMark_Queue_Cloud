package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "br_face_vector")
public class FaceVector implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "face_vector_id")
    private Long faceVectorId;

    @TableField(value = "face_id")
    private String faceId;

    @TableField(value = "user_hidden_id")
    private Long userHiddenId;
}
