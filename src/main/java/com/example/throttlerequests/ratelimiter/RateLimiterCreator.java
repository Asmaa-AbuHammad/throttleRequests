package com.example.throttlerequests.ratelimiter;

import com.example.throttlerequests.config.RateLimiterConfiguration;

/***
 * Responsible for creating {@link RateLimiter}
 */
public interface RateLimiterCreator {
    RateLimiter createRateLimiter(RateLimiterConfiguration configuration);
}
