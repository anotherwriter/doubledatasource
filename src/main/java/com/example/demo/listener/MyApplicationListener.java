package com.example.demo.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;

public class MyApplicationListener implements ApplicationListener {

    public static boolean isClosed = true;
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ApplicationReadyEvent){
            System.out.println("spring boot ready");
        } else if(event instanceof ContextStoppedEvent){
            System.out.println("spring boot stop");
        } else if(event instanceof ContextClosedEvent) {
            System.out.println("spring boot close");
        } else {
            System.out.println("spring boot what have you done! className:" + event.getClass());
        }
    }
}
