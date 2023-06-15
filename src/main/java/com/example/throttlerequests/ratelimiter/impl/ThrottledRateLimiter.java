package com.example.throttlerequests.ratelimiter.impl;

import com.example.throttlerequests.config.RateLimiterConfiguration;
import com.example.throttlerequests.ratelimiter.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 *  Throttle requests that exceed the threshold
 */
public class ThrottledRateLimiter extends RateLimiter {
    public ThrottledRateLimiter(RateLimiterConfiguration configuration) {
        super(configuration);
    }

    @Override
    public boolean handle(HttpServletRequest request,
                          HttpServletResponse response) throws InterruptedException {
        Thread.sleep(configuration.getThrottlingDelay().toMillis());
        return true;
    }
}
