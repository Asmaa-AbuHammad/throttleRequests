package com.example.throttlerequests.controller;

import com.example.throttlerequests.utils.EndPoints;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.throttlerequests.utils.UrlUtils.buildRequestUrl;


/***
 * Displays user posts
 */
@RestController
@RequestMapping(EndPoints.POST_URI)
public class PostsController {
    @GetMapping
    public String getPostsUrl(HttpServletRequest request) {
        return buildRequestUrl(request);
    }
}
