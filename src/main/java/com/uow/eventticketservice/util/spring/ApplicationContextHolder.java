package com.uow.eventticketservice.util.spring;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder {

    private static ApplicationContext context;

    private final ApplicationContext applicationContext;

    @Autowired
    public ApplicationContextHolder(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        if (context == null) {
            throw new IllegalStateException("ApplicationContext is not initialized yet.");
        }
        return context;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return getApplicationContext().getBean(beanClass);
    }

    @PostConstruct
    public void init() {
        ApplicationContextHolder.context = this.applicationContext;
    }
}
