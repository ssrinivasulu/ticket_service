/**
 * 
 */
package com.demo.ticketservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author SSrinivasulu
 *
 */

@Configuration
public class TicketServiceRedisConfig 
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TicketServiceRedisConfig.class);

    @Bean
	JedisPool jedisPool() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestWhileIdle(true);
		return new JedisPool(poolConfig, "localhost", 6379, 30);
	}
    
    @Bean
    StringRedisSerializer  stringRedisSerializer(){
    	StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    	return stringRedisSerializer;
    }
}
