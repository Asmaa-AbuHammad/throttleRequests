package com.example.throttlerequests.interceptors;


import com.example.throttlerequests.ratelimiter.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/***
 *  Spring interceptor for {@link RateLimiter}
 */
public class RequestLimiterInterceptor implements HandlerInterceptor {

    private final RateLimiter rateLimiter;

    public RequestLimiterInterceptor(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws InterruptedException, IOException {
        boolean result = rateLimiter.handle(request, response);
        if(!result) {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.getWriter().write("Service Unavailable. Please try again later.");
            return false;
        }

        return true;
    }
}



