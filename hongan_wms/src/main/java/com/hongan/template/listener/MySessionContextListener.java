package com.hongan.template.listener;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class MySessionContextListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // TODO Auto-generated method stub
//        System.out.println("有session诞生了，ID是：" + se.getSession().getId() + "---" + se.getSession().getMaxInactiveInterval());

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // TODO Auto-generated method stub
//        System.out.println("有session失效了，ID是：" + se.getSession().getId() + "--" + se.getSession().getMaxInactiveInterval());
    }
}
