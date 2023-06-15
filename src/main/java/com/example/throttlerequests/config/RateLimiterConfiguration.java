package com.example.throttlerequests.config;

import com.example.throttlerequests.ratelimiter.RateLimiter;

import java.time.Duration;

/***
 * Contains information required to create {@link RateLimiter}
 */
public class RateLimiterConfiguration {

    private final Duration timeFrame;
    private final long maxRequestsPerTimeFrame;
    private final Duration throttlingDelay;
    private final int maxConcurrentThrottledRequests;

    private RateLimiterConfiguration(RateLimiterConfigurationBuilder builder) {
        this.timeFrame = builder.timeFrame;
        this.maxRequestsPerTimeFrame = builder.maxRequestsPerTimeFrame;
        this.throttlingDelay = builder.throttlingDelay;
        this.maxConcurrentThrottledRequests = builder.maxConcurrentThrottledRequests;

    }

    public Duration getTimeFrame() {
        return timeFrame;
    }

    public long getMaxRequestsPerTimeFrame() {
        return maxRequestsPerTimeFrame;
    }

    public Duration getThrottlingDelay() {
        return throttlingDelay;
    }

    public int getMaxConcurrentThrottledRequests() {
        return maxConcurrentThrottledRequests;
    }

    public static RateLimiterConfigurationBuilder builder() {
        return new RateLimiterConfigurationBuilder();
    }

    public static class RateLimiterConfigurationBuilder {
        private Duration timeFrame;
        private long maxRequestsPerTimeFrame;
        private Duration throttlingDelay;
        private int maxConcurrentThrottledRequests;


        public RateLimiterConfigurationBuilder setTimeFrame(Duration timeFrame) {
            this.timeFrame = timeFrame;
            return this;
        }

        public RateLimiterConfigurationBuilder setMaxRequestsPerTimeFrame(long maxRequestsPerTimeFrame) {
            this.maxRequestsPerTimeFrame = maxRequestsPerTimeFrame;
            return this;
        }

        public RateLimiterConfigurationBuilder setThrottlingDelay(Duration throttlingDelay) {
            this.throttlingDelay = throttlingDelay;
            return this;
        }

        public RateLimiterConfigurationBuilder setMaxConcurrentThrottledRequests(int maxConcurrentThrottledRequests) {
            this.maxConcurrentThrottledRequests = maxConcurrentThrottledRequests;
            return this;
        }

        public RateLimiterConfiguration build() {
            return new RateLimiterConfiguration(this);
        }
    }
}