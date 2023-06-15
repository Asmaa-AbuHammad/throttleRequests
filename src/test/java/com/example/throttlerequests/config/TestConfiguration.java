package com.example.throttlerequests.config;

import com.example.throttlerequests.ratelimiter.TestRateLimiterCreator;
import com.example.throttlerequests.ratelimiter.RateLimiterCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestConfiguration {

    @Primary
    @Bean
    public RateLimiterCreator thresholdChainCreator() {
        return new TestRateLimiterCreator();
    }
}
