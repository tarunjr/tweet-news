package org.tarun.learning.tweetnews.trends.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Service
public class RedisCachingService implements CachingService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private ValueOperations<String,String> opsForValue;

    public RedisCachingService() {
        opsForValue = redisTemplate.opsForValue();
    }
    @Override
    public String get(String key) {
        return opsForValue.get(key);
    }
    @Override
    public void set(String key, String value) {
        opsForValue.set(key, value);
    }
}
