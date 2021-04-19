package com.roymark.queue.controller;

import com.alibaba.fastjson.JSONObject;
import com.roymark.queue.util.HCNetSDK;
import com.sun.jna.NativeLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
public class TestController {


    private NativeLong lUserID;


    private HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo;//设备信息

    @RequestMapping(value = "/getAll", produces = "application/json;charset=utf-8")
    public Object test() {
        HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
        boolean initSuc = hCNetSDK.NET_DVR_Init();
        if (!initSuc)
        {
            System.out.println("init fail");;
        }

        JSONObject object = new JSONObject();
        m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        int iPort = 8000;
        lUserID = hCNetSDK.NET_DVR_Login_V30("10.249.41.65",
                (short) iPort, "admin", "1234qwer", m_strDeviceInfo);
        long userID = lUserID.longValue();
        System.out.println(hCNetSDK.NET_DVR_GetLastError());
        if (userID == -1)
        {
            System.out.println("fail");
        }
        else
        {
            System.out.println("success");
        }
        return object;
    }

}
