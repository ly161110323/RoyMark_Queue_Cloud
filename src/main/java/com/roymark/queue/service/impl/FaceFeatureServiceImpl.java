package com.roymark.queue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.FaceFeatureMapper;
import com.roymark.queue.entity.FaceFeature;
import com.roymark.queue.service.FaceFeatureService;
import org.springframework.stereotype.Service;

@Service
public class FaceFeatureServiceImpl extends ServiceImpl<FaceFeatureMapper, FaceFeature> implements FaceFeatureService {
}
