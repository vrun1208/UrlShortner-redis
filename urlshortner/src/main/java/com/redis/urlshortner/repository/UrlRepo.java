package com.redis.urlshortner.repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.redis.urlshortner.redisConfig.ConsistentHashing;

@Repository
public class UrlRepo {

	
	private final ConsistentHashing consistentHashing;
	
	@Autowired
	public UrlRepo(List<LettuceConnectionFactory> redisFactories) {
		this.consistentHashing = new ConsistentHashing(redisFactories);
	}
	
	public void save(String key, String value, long ttl) {
		 // Get the appropriate Redis node based on the consistent hashing algorithm
        LettuceConnectionFactory factory = consistentHashing.getFactory(key);
        StringRedisTemplate template = new StringRedisTemplate(factory);  // Use LettuceConnectionFactory to create Redis template
        
		template.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
	}
	
	public String find(String key) {
		LettuceConnectionFactory factory = consistentHashing.getFactory(key);  // Get Redis node for the key
        StringRedisTemplate template = new StringRedisTemplate(factory);  // Use LettuceConnectionFactory to create Redis template
        
        
		return template.opsForValue().get(key);
	}
}
