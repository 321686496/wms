package com.hongan.template.base.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PLApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    public ApplicationContext getContext() {
        return applicationContext;
    }

    /***
     * 当继承了ApplicationContextAware类之后，那么程序在调用 getBean(String)的时候会自动调用该方法，不用自己操作
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        log.info("setApplicationContext :::: " + applicationContext);
        PLApplicationContext.applicationContext = applicationContext;
    }
     // 获取 bean
    public Object getBean(String beanName) {
        try {
            if(applicationContext == null){
                log.error("applicationContext is null");
            }
            return applicationContext.getBean(beanName);
        } catch (Exception e) {
            log.warn("not fund bean [" + beanName + "]", e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(String beanName, Class<T> clazz) {
        return (T) getBean(beanName);
    }

    public Object getBeanThrowException(String beanID) {
        return getBeanThrowException(beanID, Object.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBeanThrowException(String beanID, Class<T> clazz) {

        if (beanID == null || "".equals(beanID)) {
            throw new IllegalArgumentException("beanID is empty [" + beanID + "]");
        }

        try {
            return (T) applicationContext.getBean(beanID);
        } catch (Exception e) {
            log.error("not fund bean [" + beanID + "]", e);
            throw new NullPointerException("not fund bean [" + beanID + "] !!!!!!!");
        }
    }

}