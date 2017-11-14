package com.example.demo.listener;


import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

//@Order(Ordered.HIGHEST_PRECEDENCE)
public class PropertiesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        System.out.println("PropertiesListener start");
        try {
            // such as getting properties from url
            Thread.sleep(2* 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sleep end");
        ConfigurableEnvironment environment = event.getEnvironment();
        Properties props = new Properties();
        props.put("test.property.name", "writer");
        environment.getPropertySources().addFirst(new PropertiesPropertySource("myProps", props));
        System.out.println("PropertiesListener end");
    }
}
