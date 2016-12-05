package org.tarun.learning.tweetnews.trends.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class CloudFrontS3Mapper{
    @Value("${service.article.cloudfront.url}")
    private final String cloudFrontDistributionDNS;

    @Value("${service.article.s3.url}")
    private final String s3BucketUrlPath;

    public String mapToCloudFrontUrl(String s3ObjectUrl) {
        String[] tokens = s3ObjectUrl.split("/");
        System.out.println("mapToCloudFrontUrl:" + cloudFrontDistributionDNS);
        StringBuilder sb = new StringBuilder();
        sb.append(cloudFrontDistributionDNS);
        sb.append('/');
        sb.append(tokens[tokens.length-1]);
        return sb.toString();
    }
    public String mapToS3ObjectUrl(String cloudFrontUrl) {
      String[] tokens = cloudFrontUrl.split("/");
      StringBuilder sb = new StringBuilder();
      sb.append(s3BucketUrlPath);
      sb.append('/');
      sb.append(tokens[tokens.length-1]);
      return sb.toString();
    }
}
