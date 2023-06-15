package com.example.throttlerequests.tests;

import com.example.throttlerequests.controller.HistoryController;
import com.example.throttlerequests.controller.PostsController;
import com.example.throttlerequests.controller.ProfileController;
import com.example.throttlerequests.utils.EndPoints;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

@SpringBootTest
class TestApiEndPoints {


    @Test
    void testApiPosts() {
        PostsController postsController = new PostsController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(EndPoints.POST_URI);
        request.addHeader("host", "localhost:8080");
        String postsUrl = postsController.getPostsUrl(request);

        Assertions.assertEquals("http://localhost:8080" + EndPoints.POST_URI, postsUrl);
    }


    @Test
    void testApiHistory() {
        HistoryController historyController = new HistoryController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(EndPoints.HISTORY_URI);
        request.addHeader("host", "localhost:8080");
        String historyUrl = historyController.getHistoryUrl(request);

        Assertions.assertEquals("http://localhost:8080" + EndPoints.HISTORY_URI, historyUrl);
    }


    @Test
    void testApiProfile() throws InterruptedException {
        ProfileController profileController = new ProfileController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(EndPoints.PROFILE_URI);
        request.addHeader("host", "localhost:8080");
        String profileUri = profileController.getProfileUrl(request);

        Assertions.assertEquals("http://localhost:8080" + EndPoints.PROFILE_URI, profileUri);
    }
}