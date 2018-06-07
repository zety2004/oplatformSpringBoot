package com.hklk.oplatform.comm.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Set;

@Configuration
@EnableCaching
public class RedisCacheConfiguration extends CachingConfigurerSupport {

    @Value("${spring.redis.sentinel.nodes}")
    private Set<String> nodes;

    @Value("${spring.redis.sentinel.master}")
    private String master;

    @Value("${spring.redis.pool.max-wait}")
    private int wait;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private int database;


    @Bean
    public JedisSentinelPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(master,nodes,jedisPoolConfig,wait,password,database);
        return jedisSentinelPool;
    }

}