package com.example.throttlerequests.utils;


import jakarta.servlet.http.HttpServletRequest;

public class UrlUtils {

    private UrlUtils() {
    }

    /**
     * @return request URL based on {@link HttpServletRequest}
     */
    public static String buildRequestUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String host = request.getHeader("Host");
        String requestUri = request.getRequestURI();
        return scheme + "://" + host + requestUri;
    }
}
