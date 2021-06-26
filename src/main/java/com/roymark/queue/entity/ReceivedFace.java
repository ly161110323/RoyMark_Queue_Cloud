package com.roymark.queue.entity;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReceivedFace implements Serializable {
    private String faceId;
    private Long windowHiddenId;
    private String reId;
    private double faceConf;
}
