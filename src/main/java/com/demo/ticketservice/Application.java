package com.demo.ticketservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.demo.ticketservice.service.EventVenueMgmtService;

@SpringBootApplication
/*@Configuration
@Import({ TicketServiceConfig.class})
@ComponentScan(
		useDefaultFilters = false,
	    basePackages = { "com.demo.ticketservice", "com.demo.ticketservice.services"})*/
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
		
    	/*ApplicationContext ctx = SpringApplication.run(Application.class, args);
        System.out.println("Let's inspect the beans provided by Spring Boot:");
        
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }*/
    }
    
    
}
