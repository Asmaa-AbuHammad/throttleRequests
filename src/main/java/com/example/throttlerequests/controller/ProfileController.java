package com.example.throttlerequests.controller;

import com.example.throttlerequests.utils.EndPoints;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.throttlerequests.utils.UrlUtils.buildRequestUrl;

/**
 * Displays user profile
 */
@RestController
@RequestMapping(EndPoints.PROFILE_URI)
public class ProfileController {

    @GetMapping
    public String getProfileUrl(HttpServletRequest request)  {
        return buildRequestUrl(request);
    }
}