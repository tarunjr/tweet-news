package org.tarun.learning.tweetnews.trends.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.tarun.learning.tweetnews.trends.model.Article;
import org.tarun.learning.tweetnews.trends.repository.KeyNameSpace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.net.URL;

/**
 * Created by tarunrathor on 01/11/16.
 */
@Repository
public class ArticleRepository {
    private AmazonS3 s3Client = null;

    @Value("${service.article.s3.bucketname}")
    private String bucketName;
    @Value("${service.article.s3.region}")
    private String region;

    public  ArticleRepository() {
        try {
          s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();
        } catch (Exception ex) {
            s3Client = null;
        }
    }

    public String getUrl(String url) {
        if (s3Client == null)
          return null;

        System.out.println("get url from S3");
        String objectKey = getS3ObjectKey("hashtag",url);
        System.out.println(objectKey);
        try {
          URL articleUrl = null;
          articleUrl = s3Client.getUrl(bucketName, objectKey);
          System.out.println(articleUrl.toString());
          return articleUrl.toString();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        return "";
    }
    public Article getExpanded(String url) {
        if (s3Client == null)
          return null;

        Article article=null;
        String objectKey = getS3ObjectKey("hashtag",url);

        try {
          String articleJson =  s3Client.getObjectAsString(bucketName, objectKey);
          ObjectMapper mapper = new ObjectMapper();
          article = mapper.readValue(articleJson, Article.class);
        } catch(Exception ex) {
            article = null;
            ex.printStackTrace();
        }
        return article;
    }
    public String save(String url, Article  article) {
      if (s3Client == null)
        return null;
      System.out.println("saving into S3");
      try {
        ObjectMapper mapper = new ObjectMapper();
        String articleJson = mapper.writeValueAsString(article);
        System.out.println(articleJson);
        String objectKey = getS3ObjectKey("hashtag",url);
        System.out.println(objectKey);
        s3Client.putObject(bucketName, objectKey, articleJson);
        return objectKey;
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      return "";
    }

    private String getS3ObjectKey(String hashtag, String url) {
        StringBuilder sb = new StringBuilder();
        sb.append("article/");
        sb.append(hashtag);
        sb.append('/');
        sb.append(getMD5(url));
        sb.append(".json");
        return sb.toString();
    }
    private String  getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(),0,str.length());
            return new BigInteger(1,md.digest()).toString(16);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
