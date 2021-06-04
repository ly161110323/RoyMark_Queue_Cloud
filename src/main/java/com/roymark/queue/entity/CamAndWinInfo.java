package com.roymark.queue.entity;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CamAndWinInfo {

    private Camera camera;
    private List<Window> windows;
}
