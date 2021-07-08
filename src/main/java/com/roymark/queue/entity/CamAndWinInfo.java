package com.roymark.queue.entity;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CamAndWinInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Camera camera;
    private List<Window> windows;
}
