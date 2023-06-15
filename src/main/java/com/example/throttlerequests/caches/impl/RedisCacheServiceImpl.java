package com.example.throttlerequests.caches.impl;

import com.example.throttlerequests.caches.CacheService;
import com.example.throttlerequests.utils.CacheKey;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheServiceImpl implements CacheService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    
    
    @Override
    public void setIfAbsent(HttpServletRequest request,CacheKey cacheKey, String value) {
        String internalKey = generateInternalKey(cacheKey,request);
        redisTemplate.opsForValue().setIfAbsent(internalKey, value);
    }


    private String generateInternalKey(CacheKey cacheKey, HttpServletRequest request) {
        return cacheKey.getKey() +  request.getRequestURI() +":"+ request.getRemoteAddr();
    }


    @Override
    public void set(HttpServletRequest request, CacheKey cacheKey, String value) {
        redisTemplate.opsForValue().set(generateInternalKey(cacheKey,request),value);
    }

    @Override
    public String get(HttpServletRequest request, CacheKey cacheKey) {
        return redisTemplate.opsForValue().get(generateInternalKey(cacheKey,request));
    }

    @Override
    public String incrementAndGet(HttpServletRequest request, CacheKey cacheKey) {
        return String.valueOf(redisTemplate.opsForValue()
                .increment(generateInternalKey(cacheKey,request),1));
    }

    @Override
    public void expire(HttpServletRequest request, CacheKey cacheKey, long amount, TimeUnit timeUnit) {
        redisTemplate.expire(generateInternalKey(cacheKey,request),amount,timeUnit);
    }


    @Override
    public String decrementAndGet(HttpServletRequest request, CacheKey cacheKey) {
        return String.valueOf(redisTemplate.opsForValue()
                .decrement(generateInternalKey(cacheKey,request),1));
    }

    @Override
    public void clearAll() {
        redisTemplate.execute(connection -> {
            connection.serverCommands().flushAll();
            return null;
        }, true);
    }

}
