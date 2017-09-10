package com.example.demo;

import com.example.demo.listener.MyApplicationListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class
})
@EnableAspectJAutoProxy
@EnableScheduling
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(DemoApplication.class);
		application.addListeners(new MyApplicationListener());
		application.run(args);
//		SpringApplication.run(DemoApplication.class, args);
	}
}

