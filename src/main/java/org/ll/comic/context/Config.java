package org.ll.comic.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import redis.clients.jedis.Jedis;

@Configuration
public class Config {

	private static final Logger log = LoggerFactory.getLogger(Config.class);
	@Value("${redis.host}")
	private String host;

	@Value("${redis.password}")
	private String password;

	@Value("${redis.port}")
	private Integer port;

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	Jedis jedis() {
		log.info("redis server [host:" + host + ",port:" + port + "]");
		Jedis jedis = new Jedis(host, port);
		jedis.connect();
		jedis.auth("bedbd870d45c65f2db8017d3d7f4334d");
		return jedis;
	}

}
