package com.roymark.queue.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.roymark.queue.entity.ActionUser;

public interface UserService  extends IService<ActionUser> {
    List<ActionUser> getAllUser();
    ActionUser getUserByHiddenId(Long hiddenId);
}
