package com.roymark.queue.entity;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserShortInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String userName;
    private double faceConf;
}
