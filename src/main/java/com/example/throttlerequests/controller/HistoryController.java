package com.example.throttlerequests.controller;

import com.example.throttlerequests.utils.EndPoints;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.throttlerequests.utils.UrlUtils.buildRequestUrl;

/***
 * Displays user history
 */
@RestController
@RequestMapping(EndPoints.HISTORY_URI)
public class HistoryController {
    @GetMapping
    public String getHistoryUrl(HttpServletRequest request)  {
        return buildRequestUrl(request);
    }
}
