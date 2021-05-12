package com.roymark.queue.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CamAndWinInfo {

    private Camera camera;
    private List<Window> windows;
}
