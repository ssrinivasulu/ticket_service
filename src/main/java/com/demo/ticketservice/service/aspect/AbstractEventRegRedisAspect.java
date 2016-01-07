package com.demo.ticketservice.service.aspect;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.demo.ticketservice.config.JsonRedisSerializer;

import redis.clients.jedis.JedisPool;

/**
 * @author ssrinivasulu
 *
 */
public class AbstractEventRegRedisAspect {
	 private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEventRegRedisAspect.class);

	@Inject
	protected JedisPool jedisPool;
	
	@Inject
	protected JsonRedisSerializer jsonRedisSerializer;
	
	@Inject
	protected StringRedisSerializer stringRedisSerializer;
}
