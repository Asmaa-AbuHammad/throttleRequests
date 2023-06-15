package com.example.throttlerequests.ratelimiter;

import com.example.throttlerequests.caches.CacheService;
import com.example.throttlerequests.config.RateLimiterConfiguration;
import com.example.throttlerequests.ratelimiter.*;
import com.example.throttlerequests.ratelimiter.RateLimiter;
import com.example.throttlerequests.ratelimiter.impl.RateLimiterCreatorImpl;
import com.example.throttlerequests.ratelimiter.impl.RequestsPerTimeFrameRateLimiter;
import com.example.throttlerequests.ratelimiter.impl.ThrottledRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


@Component
public class TestRateLimiterCreator extends RateLimiterCreatorImpl {
    @Autowired
    private CacheService cacheService;


    public RateLimiter createRateLimiter(RateLimiterConfiguration configuration, CountDownLatch stopper) {
        RequestsPerTimeFrameRateLimiter tokenBucketThreshold = new RequestsPerTimeFrameRateLimiter(configuration,
                cacheService);
        List<RateLimiter> otherRateLimiter = new ArrayList<>();
        if(configuration.getMaxConcurrentThrottledRequests() > 0) {
            otherRateLimiter.add(new TestConcurrentThrottledRequestsRateLimiter(configuration,
                    cacheService,
                    stopper));
        } else {
            otherRateLimiter.add(new ThrottledRateLimiter(configuration));
        }

        return RateLimiter.link(tokenBucketThreshold, otherRateLimiter
                .toArray(new RateLimiter[0]));
    }
}
