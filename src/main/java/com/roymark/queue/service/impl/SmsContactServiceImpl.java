package com.roymark.queue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roymark.queue.dao.SmsContactMapper;
import com.roymark.queue.entity.SmsContact;
import com.roymark.queue.service.SmsContactService;
import org.springframework.stereotype.Service;

@Service
public class SmsContactServiceImpl extends ServiceImpl<SmsContactMapper, SmsContact> implements SmsContactService {
}
