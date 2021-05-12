package com.roymark.queue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.FaceVectorMapper;
import com.roymark.queue.entity.FaceVector;
import com.roymark.queue.service.FaceVectorService;
import org.springframework.stereotype.Service;

@Service
public class FaceVectorServiceImpl extends ServiceImpl<FaceVectorMapper, FaceVector> implements FaceVectorService {
}
