/**
 * 
 */
package com.demo.ticketservice.services;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.demo.ticketservice.config.TicketServiceRedisKeyExpiredListener;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;

/**
 * @author ssrinivasulu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractTicketServiceTest {

	private static Logger logger = LoggerFactory.getLogger(AbstractTicketServiceTest.class);
	public RedisServer redisServer;
	@Autowired
    private ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		redisServer = new RedisServer(6378);
		redisServer.start();
	}

	@After
	public void tearDown() throws Exception {
		redisServer.stop();
	}
	
	public void redisListenerStart() {
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
