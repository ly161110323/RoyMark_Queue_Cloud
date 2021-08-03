package com.roymark.queue.util.web;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author liucl
 * @create 2021-01-14 10:41
 */

@WebListener
public class MyServletListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        HttpSession session = request.getSession();
        session.setAttribute("ClientIP", request.getRemoteAddr());//把HttpServletRequest中的IP地址放入HttpSession中，关键字可任取，此处为ClientIP
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
    }

}
