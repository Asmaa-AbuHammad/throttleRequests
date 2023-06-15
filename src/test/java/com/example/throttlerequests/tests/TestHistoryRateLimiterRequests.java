package com.example.throttlerequests.tests;

import com.example.throttlerequests.caches.CacheService;
import com.example.throttlerequests.config.RateLimiterConfiguration;
import com.example.throttlerequests.ratelimiter.RateLimiter;
import com.example.throttlerequests.ratelimiter.RateLimiterCreator;
import com.example.throttlerequests.ratelimiter.TestRateLimiterCreator;
import com.example.throttlerequests.utils.CacheKey;
import com.example.throttlerequests.utils.TestUtils;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

import static com.example.throttlerequests.utils.TestUtils.*;

@SpringBootTest
class TestHistoryRateLimiterRequests {

    @Autowired
    private RateLimiterCreator rateLimiterCreator;

    @Autowired
    private CacheService cacheService;
    private static final RateLimiterConfiguration historyRequestThresholdConf
            = RateLimiterConfiguration.builder()
            .setTimeFrame(Duration.ofMinutes(1))
            .setMaxRequestsPerTimeFrame(30)
            .setThrottlingDelay(Duration.ofMillis(3000))
            .setMaxConcurrentThrottledRequests(30)
            .build();

    @BeforeEach
    @AfterEach
    void afterEach() {
        cacheService.clearAll();
    }

    @Test
    void testApiHistory_ThrottlingWithinLimit() throws Exception {
        // Perform 30 requests within a minute (within the limit)
        performAndTestRequestByMinute(30, createRateLimiter());
        Assertions.assertEquals("30",
                cacheService.get(TestUtils.createMockRequest(),
                        CacheKey.REQUESTS_PER_FRAME));
    }

    private RateLimiter createRateLimiter() {
        return rateLimiterCreator
                .createRateLimiter(historyRequestThresholdConf);
    }

    @Test
    void testApiHistory_SlowUp() throws Exception {
        // Perform 31 requests within a minute (exceeding the limit)
        performAndTestRequestByMinute(31, createRateLimiter());
        MockHttpServletRequest mockRequest = createMockRequest();
        Assertions.assertEquals("30",
                cacheService.get(mockRequest,CacheKey.REQUESTS_PER_FRAME));
        Assertions.assertEquals("0",
                cacheService.get(mockRequest,CacheKey.CONCURRENT_THROTTLED_REQUESTS));
    }

    @Test
    void testApiHistory_ThrottlingExceeded() throws Exception {
        // Perform 31 requests within a minute (exceeding the limit)
        CountDownLatch stopper = new CountDownLatch(1);
        RateLimiter seqRateLimiter = createRateLimiter();
        performAndTestRequestByMinute(30, seqRateLimiter);

        RateLimiter asyncRateLimiter = ((TestRateLimiterCreator) rateLimiterCreator)
                .createRateLimiter(historyRequestThresholdConf,stopper);
        MockHttpServletRequest mockRequest = createMockRequest();
        Assertions.assertEquals("30",
                cacheService.get(mockRequest,CacheKey.REQUESTS_PER_FRAME));
        List<Future<Boolean>> futuresRequest = performRequestAsync(asyncRateLimiter);
        Awaitility.await().until(() -> "30"
                .equals(cacheService.get(mockRequest,CacheKey.CONCURRENT_THROTTLED_REQUESTS)));
        Assertions.assertEquals("30",
                cacheService.get(mockRequest,CacheKey.CONCURRENT_THROTTLED_REQUESTS));

        Assertions.assertFalse(seqRateLimiter.handle(new MockHttpServletRequest(),
                new MockHttpServletResponse()));
        stopper.countDown();
        for (Future<Boolean> future : futuresRequest) {
            Assertions.assertTrue(future.get());
        }

        Assertions.assertEquals("0",
                cacheService.get(mockRequest,CacheKey.CONCURRENT_THROTTLED_REQUESTS));
    }

}
