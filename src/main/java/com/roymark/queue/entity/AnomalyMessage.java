package com.roymark.queue.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnomalyMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long anomalyHiddenId;
    private Date updateTime;
    private String faceId;
    private double faceConf;
}
