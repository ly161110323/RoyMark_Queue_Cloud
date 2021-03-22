package com.roymark.queue.util;

import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.UUID;

public class GetDataLs {
	public static String getLs() {
//		Date dt=new Date();
//		String DataLs = DateUtil.format(dt, "yyyyMMddHHmmssSSS");
//		Long ls = Long.valueOf(DataLs);
//		int num = (int) ((Math.random() * 9 + 1) * 100);
//		Long newDataLs = ls+num;
//		return String.valueOf(newDataLs);
		return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}
}
