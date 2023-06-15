package com.example.throttlerequests.tests;

import com.example.throttlerequests.caches.CacheService;
import com.example.throttlerequests.config.RateLimiterConfiguration;
import com.example.throttlerequests.ratelimiter.RateLimiter;
import com.example.throttlerequests.ratelimiter.RateLimiterCreator;
import com.example.throttlerequests.ratelimiter.TestRateLimiterCreator;
import com.example.throttlerequests.utils.CacheKey;
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
class TestProfileRateLimiterRequests {

    @Autowired
    private RateLimiterCreator rateLimiterCreator;

    @Autowired
    private CacheService cacheService;
    private static final RateLimiterConfiguration profileRequestThresholdConf
            = RateLimiterConfiguration.builder()
            .setTimeFrame(Duration.ofMinutes(1))
            .setMaxRequestsPerTimeFrame(10)
            .setThrottlingDelay(Duration.ofMillis(3000))
            .build();

    @BeforeEach
    @AfterEach
    public void afterEach() {
        cacheService.clearAll();
    }

    @Test
    void testApiProfile_ThrottlingWithinLimit() throws Exception {
        // Perform 10 requests within a minute (within the limit)
        performAndTestRequestByMinute(10, createRateLimiter());
        Assertions.assertEquals("10",
                cacheService.get(createMockRequest(),CacheKey.REQUESTS_PER_FRAME));
    }

    private RateLimiter createRateLimiter() {
        return rateLimiterCreator.createRateLimiter(profileRequestThresholdConf);
    }

    @Test
    void testApiProfile_SlowUp() throws Exception {
        // Perform 11 requests within a minute (exceeding the limit)
        performAndTestRequestByMinute(11, createRateLimiter());

        MockHttpServletRequest mockRequest = createMockRequest();
        Assertions.assertEquals("10",
                cacheService.get(mockRequest,CacheKey.REQUESTS_PER_FRAME));
        Assertions.assertNull(cacheService.get(mockRequest,CacheKey.CONCURRENT_THROTTLED_REQUESTS));
    }

    @Test
    void testApiProfile_ThrottlingExceeded() throws Exception {
        // Perform 31 requests within a minute (exceeding the limit)
        CountDownLatch stopper = new CountDownLatch(1);
        RateLimiter seqRateLimiter = createRateLimiter();
        performAndTestRequestByMinute(10, seqRateLimiter);

        MockHttpServletRequest mockRequest = createMockRequest();
        Assertions.assertEquals("10",
                cacheService.get(mockRequest,CacheKey.REQUESTS_PER_FRAME));


        RateLimiter asyncRateLimiter = ((TestRateLimiterCreator) rateLimiterCreator)
                .createRateLimiter(profileRequestThresholdConf,stopper);
        List<Future<Boolean>> futuresRequest = performRequestAsync(asyncRateLimiter);

        Assertions.assertNull(cacheService.get(mockRequest,CacheKey.CONCURRENT_THROTTLED_REQUESTS));


        Assertions.assertTrue(seqRateLimiter.handle(new MockHttpServletRequest(),
                new MockHttpServletResponse()));
        stopper.countDown();
        for (Future<Boolean> future : futuresRequest) {
            Assertions.assertTrue(future.get());
        }

        Assertions.assertNull(cacheService.get(mockRequest,CacheKey.CONCURRENT_THROTTLED_REQUESTS));
    }

}
