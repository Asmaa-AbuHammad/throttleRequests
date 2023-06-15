package com.example.throttlerequests.utils;

public enum CacheKey {

    REQUESTS_PER_FRAME("REQUESTS_PER_FRAME_"),
    CONCURRENT_THROTTLED_REQUESTS("CONCURRENT_THROTTLED_REQUESTS_");
    private final String key;

    CacheKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
