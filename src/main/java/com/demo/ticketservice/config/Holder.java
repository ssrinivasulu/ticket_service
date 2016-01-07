/**
 * 
 */
package com.demo.ticketservice.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * @author ssrinivasulu
 *
 */
@JsonTypeInfo(use = Id.NONE)
public class Holder 
{
	/**
	 * Wrapped value.
	 */
	@JsonProperty("v")
	private Object value;

	public Holder() {

	}

	public Holder(final Object value) {
		this.setValue(value);
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}