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

    @Override
    public String get(String key) {
        ValueOperations<String,String> opsForValue = redisTemplate.opsForValue();
        return opsForValue.get(key);
    }
    @Override
    public void set(String key, String value) {
        ValueOperations<String,String> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key, value);
    }
}
