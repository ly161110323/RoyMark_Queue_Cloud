package com.roymark.queue.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnomalyMessage {
    private Long anomalyHiddenId;
    private Date updateTime;
    private String faceId;
    private double faceConf;
}
