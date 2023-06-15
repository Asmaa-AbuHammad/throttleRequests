package com.example.throttlerequests.ratelimiter.impl;

import com.example.throttlerequests.caches.CacheService;
import com.example.throttlerequests.config.RateLimiterConfiguration;
import com.example.throttlerequests.ratelimiter.RateLimiter;
import com.example.throttlerequests.ratelimiter.RateLimiterCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RateLimiterCreatorImpl implements RateLimiterCreator {
    @Autowired
    private CacheService cacheService;


    @Override
    public RateLimiter createRateLimiter(RateLimiterConfiguration configuration) {
        RequestsPerTimeFrameRateLimiter requestsPerTimeFrameRateLimiter =
                new RequestsPerTimeFrameRateLimiter(configuration,cacheService);
        List<RateLimiter> otherRateLimiter = new ArrayList<>();
        if(configuration.getMaxConcurrentThrottledRequests() > 0) {
            otherRateLimiter.add(new ConcurrentThrottledRequestsRateLimiter(configuration,cacheService));
        } else {
            otherRateLimiter.add(new ThrottledRateLimiter(configuration));
        }

        return RateLimiter.link(requestsPerTimeFrameRateLimiter, otherRateLimiter
                .toArray(new RateLimiter[0]));
    }
}
