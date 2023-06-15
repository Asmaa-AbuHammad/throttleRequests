package com.example.throttlerequests.ratelimiter;

import com.example.throttlerequests.caches.CacheService;
import com.example.throttlerequests.config.RateLimiterConfiguration;
import com.example.throttlerequests.ratelimiter.impl.ConcurrentThrottledRequestsRateLimiter;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.CountDownLatch;

public class TestConcurrentThrottledRequestsRateLimiter extends ConcurrentThrottledRequestsRateLimiter {
    private final CountDownLatch stopper;

    public TestConcurrentThrottledRequestsRateLimiter(RateLimiterConfiguration configuration,
                                                      CacheService cacheService,
                                                      CountDownLatch stopper) {
        super(configuration, cacheService);
        this.stopper = stopper;
    }

    @Override
    protected void slowDown() throws InterruptedException {
        stopper.await();
//        while (lock.get() == 0) {
//            Thread.sleep(configuration.getThrottlingDelay().toMillis());
//        }
    }
}
