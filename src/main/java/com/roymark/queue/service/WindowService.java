package com.roymark.queue.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.roymark.queue.entity.Window;

public interface WindowService  extends IService<Window> {

    List<Window> getAllWindow();
    boolean deleteByWindowHiddenId(Long windowHiddenId);
    Window getWindowByHiddenId(Long windowHiddenId);
}
