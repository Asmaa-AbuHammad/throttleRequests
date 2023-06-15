package com.example.throttlerequests.ratelimiter;

import com.example.throttlerequests.config.RateLimiterConfiguration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/***
 * Manage the number of requests that can be made within a specific time frame.
 */
public abstract class RateLimiter {
    protected final RateLimiterConfiguration configuration;
    private RateLimiter next;

    protected RateLimiter(RateLimiterConfiguration configuration) {
        this.configuration= configuration;
    }

    public static RateLimiter link(RateLimiter first,
                                   RateLimiter... chain) {
        RateLimiter head = first;
        for (RateLimiter nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    protected boolean handleNext(HttpServletRequest request,
                                 HttpServletResponse response) throws InterruptedException {
        if (next == null) {
            return false;
        }

        return next.handle(request,response);
    }

    public abstract boolean handle(HttpServletRequest request,
                                   HttpServletResponse response) throws InterruptedException;
}
