package org.tarun.learning.tweetnews.trends.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.tarun.learning.tweetnews.trends.service.CloudFrontS3Mapper;


@RestController
@CrossOrigin
public class HealthCheckController {
    @Autowired
    private CloudFrontS3Mapper mapper;

    @RequestMapping("/trends/api/v1/healthcheck")
    public String health() {
        System.out.println(mapper.mapToCloudFrontUrl("https://s3.amazonaws.com/articles-cache/article/hashtag/2358671059a576a047317f5a762bee17.json"));
        return "OK";
    }
}
