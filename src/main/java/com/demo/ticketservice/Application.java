package com.demo.ticketservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.demo.ticketservice.service.EventVenueMgmtService;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private EventVenueMgmtService eventVenueMgmtService;
	
	@Override
	public void run(String... args) {
		System.out.println(this.eventVenueMgmtService.getHelloMessage());
	}
    public static void main(String[] args) {
    	SpringApplication application = new SpringApplication(
    			Application.class);
		application.setApplicationContextClass(AnnotationConfigApplicationContext.class);
		SpringApplication.run(Application.class, args);
    }
    
    
}
