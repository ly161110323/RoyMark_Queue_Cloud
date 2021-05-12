package com.roymark.queue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.FaceMapper;
import com.roymark.queue.entity.Face;
import com.roymark.queue.service.FaceService;
import org.springframework.stereotype.Service;

@Service
public class FaceServiceImpl extends ServiceImpl<FaceMapper, Face> implements FaceService {
}
