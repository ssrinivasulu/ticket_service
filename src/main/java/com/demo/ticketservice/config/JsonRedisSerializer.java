package com.demo.ticketservice.config;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ssrinivasulu
 *
 */
@Component
public class JsonRedisSerializer implements RedisSerializer<Object> 
{

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	static {
		OBJECT_MAPPER.enableDefaultTyping();
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		if (ArrayUtils.isEmpty(bytes)) {
			return null;
		}	
		try {
			return OBJECT_MAPPER.readValue(bytes, Holder.class).getValue();
		} catch (Exception e) {
			throw new SerializationException("Error on converting bytearray to json object", e);
		}
	}
	
	@Override
	public byte[] serialize(Object t) throws SerializationException {
		if (t == null) {
			return new byte[0];
		}
		try {
			return OBJECT_MAPPER.writeValueAsBytes(new Holder(t));
		} catch (Exception e) {
			throw new SerializationException("Error on writing json object to bytearray", e);
		}
	}
	
}