package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "br_anomaly_user")
public class AnomalyUser {
    @TableId(value = "id")
    private Long id;

    @TableField(value = "anomaly_hidden_id")
    private Long anomalyHiddenId;

    @TableField(value = "user_hidden_id")
    private Long userHiddenId;

    @TableField(value = "face_conf")
    private double faceConf;
}
