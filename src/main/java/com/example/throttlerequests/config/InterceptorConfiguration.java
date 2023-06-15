package com.example.throttlerequests.config;

import com.example.throttlerequests.interceptors.RequestLimiterInterceptor;
import com.example.throttlerequests.ratelimiter.RateLimiter;
import com.example.throttlerequests.ratelimiter.RateLimiterCreator;
import com.example.throttlerequests.utils.EndPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;


@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Autowired
    private RateLimiterCreator rateLimiterCreator;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        RateLimiter historyRateLimiter = createHistoryRateLimiter();
        registry.addInterceptor(new RequestLimiterInterceptor(historyRateLimiter))
                .addPathPatterns(EndPoints.HISTORY_URI);


        RateLimiter profileRateLimiter = createProfileRateLimiter();
        registry.addInterceptor(new RequestLimiterInterceptor(profileRateLimiter))
                .addPathPatterns(EndPoints.PROFILE_URI);
    }

    private RateLimiter createProfileRateLimiter() {
        return rateLimiterCreator.createRateLimiter(RateLimiterConfiguration.builder()
                .setTimeFrame(Duration.ofMinutes(1))
                .setMaxRequestsPerTimeFrame(10)
                .setThrottlingDelay(Duration.ofMillis(3000))
                .build());
    }

    private RateLimiter createHistoryRateLimiter() {
        return rateLimiterCreator.createRateLimiter(RateLimiterConfiguration.builder()
                .setTimeFrame(Duration.ofMinutes(1))
                .setMaxRequestsPerTimeFrame(30)
                .setThrottlingDelay(Duration.ofMillis(3000))
                .setMaxConcurrentThrottledRequests(30)
                .build());
    }
}
