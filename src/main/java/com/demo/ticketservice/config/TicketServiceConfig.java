package com.demo.ticketservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ssrinivasulu
 *
 */
@Configuration
@ComponentScan(
	    basePackages = { "com.demo.ticketservice", "com.demo.ticketservice.services"})
public class TicketServiceConfig {
}
