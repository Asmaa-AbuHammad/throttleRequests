package com.example.throttlerequests.caches;


import com.example.throttlerequests.utils.CacheKey;
import jakarta.servlet.http.HttpServletRequest;

import java.util.concurrent.TimeUnit;

public interface CacheService {
    void setIfAbsent(HttpServletRequest request, CacheKey cacheKey, String value);

    void set(HttpServletRequest request, CacheKey cacheKey, String value);

    String get(HttpServletRequest request, CacheKey cacheKey);

    String incrementAndGet(HttpServletRequest request, CacheKey cacheKey);

    void expire(HttpServletRequest request, CacheKey cacheKey, long amount, TimeUnit timeUnit);

    String decrementAndGet(HttpServletRequest request, CacheKey cacheKey);

    void clearAll();
}
