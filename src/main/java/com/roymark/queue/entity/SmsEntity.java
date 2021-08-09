package com.roymark.queue.entity;

import lombok.Data;

/*
发送短信实体类
 */
@Data
public class SmsEntity {
private String SpCode;
private String LoginName;
private String mobiles;//电话号码，多个号码用逗号隔开
private String content;//短信内容
private String Password;


}
