package com.example.throttlerequests.ratelimiter.impl;

import com.example.throttlerequests.caches.CacheService;
import com.example.throttlerequests.config.RateLimiterConfiguration;
import com.example.throttlerequests.ratelimiter.RateLimiter;
import com.example.throttlerequests.utils.CacheKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Rate limiter that restricts the number of requests within a time frame.
 */
public class RequestsPerTimeFrameRateLimiter extends RateLimiter {

    private final CacheService cacheService;

    public RequestsPerTimeFrameRateLimiter(RateLimiterConfiguration configuration, CacheService cacheService) {
        super(configuration);
        this.cacheService = cacheService;
    }

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
        CacheKey cacheKey = CacheKey.REQUESTS_PER_FRAME;
        cacheService.setIfAbsent(request, cacheKey,"0");
        Long allowedRequestPerTimeFrame =
                Long.valueOf(Objects.requireNonNull(cacheService.get(request, cacheKey)));
        if (isZero(allowedRequestPerTimeFrame)) {
            cacheService.expire(request,
                    cacheKey,
                    configuration.getTimeFrame().getSeconds(),
                    TimeUnit.SECONDS);
        }
        if (isAllowedRequestPerTimeFrameExceeds(allowedRequestPerTimeFrame)) {
            // Request limit exceeded, slow down the request
            return handleNext(request, response);
        }
        cacheService.incrementAndGet(request,cacheKey);
        return true;
    }

    private boolean isAllowedRequestPerTimeFrameExceeds(Long allowedRequestPerTimeFrame) {
        return allowedRequestPerTimeFrame >= configuration.getMaxRequestsPerTimeFrame();
    }

    private static boolean isZero(Long allowedRequestPerTimeFrame) {
        return allowedRequestPerTimeFrame == 0;
    }
}
