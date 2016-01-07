package com.demo.ticketservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.demo.ticketservice.config.TicketServiceRedisKeyExpiredListener;
import com.demo.ticketservice.config.TicketServiceRedisConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SpringBootApplication
@Configuration
@Import({ TicketServiceRedisConfig.class})
public class TicketServiceApplication {
	
	private static Logger logger = LoggerFactory.getLogger(TicketServiceRedisConfig.class);
    public static void main(String[] args) {
    	SpringApplication application = new SpringApplication(
    			TicketServiceApplication.class);
		application.setApplicationContextClass(AnnotationConfigApplicationContext.class);
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
        //final JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379, 0);
		ApplicationContext ctx = SpringApplication.run(TicketServiceApplication.class, args);
		
		final Jedis subscriberJedis = ((JedisPool)ctx.getBean("jedisPool")).getResource();
        final TicketServiceRedisKeyExpiredListener subscriber = ((TicketServiceRedisKeyExpiredListener)ctx.getBean("ticketServiceRedisKeyExpiredListener"));
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            try {
	                logger.info("Subscribing to \"expired event messages\". This thread will be blocked.");
	                subscriberJedis.subscribe(subscriber, "__keyevent@0__:expired");
	                subscriberJedis.psubscribe(subscriber, "__keyevent@0__:expired");
	                logger.info("Subscription ended.");
	            } catch (Exception e) {
	                logger.error("Subscribing failed.", e);
	            }
	        }
	    }).start();
    }
}
