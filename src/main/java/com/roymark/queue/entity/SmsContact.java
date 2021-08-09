package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "br_sms_contact")
public class SmsContact {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sms_contact_id")
    private Long smsContactId;

    @TableField(value = "sms_contact_name")
    private String smsContactName;

    @TableField(value = "sms_contact_phone")
    private String smsContactPhone;
}
