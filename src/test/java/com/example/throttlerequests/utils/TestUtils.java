package com.example.throttlerequests.utils;

import com.example.throttlerequests.ratelimiter.RateLimiter;
import org.junit.jupiter.api.Assertions;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestUtils {

    private TestUtils() {
    }

    public static List<Future<Boolean>> performRequestAsync(RateLimiter rateLimiter) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(50);

        List<Future<Boolean>> futureList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Future<Boolean> future = executorService.submit(() -> rateLimiter.handle(new MockHttpServletRequest(),
                    new MockHttpServletResponse()));
            futureList.add(future);
        }
        return futureList;
    }

    public static void performAndTestRequestByMinute(int numberOfRequest, RateLimiter rateLimiter) throws InterruptedException {
        for (int i = 1; i <= numberOfRequest; i++) {
            Assertions.assertTrue(rateLimiter.handle(new MockHttpServletRequest(),
                    new MockHttpServletResponse()));
        }
    }

    public static MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("");
        request.setRemoteHost("127.0.0.1");
        return request;
    }
}
