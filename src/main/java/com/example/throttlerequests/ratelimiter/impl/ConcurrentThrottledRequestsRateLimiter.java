package com.example.throttlerequests.ratelimiter.impl;

import com.example.throttlerequests.caches.CacheService;
import com.example.throttlerequests.config.RateLimiterConfiguration;
import com.example.throttlerequests.ratelimiter.RateLimiter;
import com.example.throttlerequests.utils.CacheKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;

/**
 * Slow down requests that exceed the threshold should take into consideration
 * that the number of concurrent throttled requests doesn't exceed the limit.
 */
public class ConcurrentThrottledRequestsRateLimiter extends RateLimiter {

    private final CacheService cacheService;

    public ConcurrentThrottledRequestsRateLimiter(RateLimiterConfiguration configuration,
                                                  CacheService cacheService) {
        super(configuration);
        this.cacheService = cacheService;
    }

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
        CacheKey cacheKey = CacheKey.CONCURRENT_THROTTLED_REQUESTS;
        cacheService.setIfAbsent(request,cacheKey,"0");
        Long count = Long.valueOf(Objects.requireNonNull(cacheService.get(request,cacheKey)));
        if (isConcurrentRequestsNotExceeds(count)) {
            // Request limit exceeded, slow down the request
            return slowDownRequestWithLimitation(request,cacheKey);
        }
        return false;
    }

    private boolean isConcurrentRequestsNotExceeds(Long numberOfConcurrentRequests) {
        return numberOfConcurrentRequests < configuration.getMaxConcurrentThrottledRequests();
    }

    private boolean slowDownRequestWithLimitation(HttpServletRequest request,CacheKey key) throws InterruptedException {
        try {
            incrementByKey(request,key);
            slowDown();
            return true;
        } finally {
            decrementByKey(request,key);
        }
    }

    protected void decrementByKey(HttpServletRequest request,CacheKey key) {
        cacheService.decrementAndGet(request,key);
    }

    protected void incrementByKey(HttpServletRequest request,CacheKey key)  {
        cacheService.incrementAndGet(request,key);
    }

    protected void slowDown() throws InterruptedException {
        Thread.sleep(configuration
                .getThrottlingDelay().toMillis());
    }
}
