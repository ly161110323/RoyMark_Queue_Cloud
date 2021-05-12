package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "br_face_feature")
public class FaceFeature implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "face_feature_id")
    private Long faceFeatureId;

    @TableField(value = "face_id")
    private String faceId;

    @TableField(value = "re_id")
    private String reId;
}
