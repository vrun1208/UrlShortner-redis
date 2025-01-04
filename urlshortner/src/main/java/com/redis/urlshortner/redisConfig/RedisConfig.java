package com.redis.urlshortner.redisConfig;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

	@Value("${redis.instances}")
	private String redisInstance;
	
	@Bean
	public List<LettuceConnectionFactory> redisConnectionFactories() {
		String[] instances = redisInstance.split(",");
		List<LettuceConnectionFactory> factories = new ArrayList<>();
		
		for (String instance : instances) {
            String[] hostPort = instance.split(":");
            factories.add(new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(hostPort[0], Integer.parseInt(hostPort[1]))
            ));
        }
        return factories;
	}
}
