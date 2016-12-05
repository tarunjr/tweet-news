package org.tarun.learning.tweetnews.trends.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.tarun.learning.tweetnews.trends.model.Article;

import org.springframework.web.client.RestTemplate;
import org.tarun.learning.tweetnews.trends.repository.ArticleRepository;
import org.tarun.learning.tweetnews.trends.service.RedisCachingService;

@Service
public class ArticleService {

    @Value("${service.article.url:127.0.0.1:8000}")
    private String articleServiceEndpoint;

    private final ArticleRepository repository;
    private final RedisCachingService cachingService;
    private final CloudFrontS3Mapper urlMapper;

    @Autowired
    public ArticleService(RedisCachingService cachingService,
                          ArticleRepository repository,
                          CloudFrontS3Mapper urlMapper) {
        this.repository = repository;
        this.cachingService = cachingService;
        this.urlMapper = urlMapper;
    }
    @Async
    public CompletableFuture<String> getCompactAsync(String url){
        String key = "article:url:" + url;
        String articleUrl = cachingService.get(key);
        if (articleUrl != null) {
            System.out.println("found in-mem cache");
            System.out.println(articleUrl);
            return CompletableFuture.completedFuture(articleUrl);
        }
        articleUrl= repository.getUrl(url);
        if (articleUrl != null) {
            System.out.println("found S3");
            System.out.println(articleUrl);
            String cfUrl = urlMapper.mapToCloudFrontUrl(articleUrl);
            // Map to cloudfront url from S3 urls and cache it in-mem
            cachingService.set(key,cfUrl );
            return CompletableFuture.completedFuture(cfUrl);
        }

        CompletableFuture<String> future = new CompletableFuture<String>();
            getArticleAsync(getArticleServiceUri(), url).thenAccept(article -> {
                System.out.println("fetched from extraction");
                repository.save(url, article);

                String s3Url = repository.getUrl(url);
                String cfUrl = urlMapper.mapToCloudFrontUrl(s3Url);
                // Map to cloudfront url from S3 urls and cache it in-mem
                cachingService.set(key,cfUrl );
                /* cache the JSON also
                String keyJson = "article:json:" + url;
                cachingService.set(keyJson,article); */
                future.complete(cfUrl);
            });
        return future;
    }
    @Async
    public CompletableFuture<Article> getExapndedAsync(String url){
        Article article = null;
        String key = "article:json:" + url;
        String articleJson = cachingService.get(key);
        if (articleJson != null) {
          try {
            ObjectMapper mapper = new ObjectMapper();
            article = mapper.readValue(articleJson, Article.class);
            return CompletableFuture.completedFuture(article);
          } catch(Exception ex) {
              ex.printStackTrace();
          }
        }
        article = repository.getExpanded(url);
        if (article != null) {
            return CompletableFuture.completedFuture(article);
        }

        CompletableFuture<Article> future = new CompletableFuture<Article>();
            getArticleAsync(getArticleServiceUri(), url).thenAccept(articleNew -> {
                repository.save(url, articleNew);
                try {
                  ObjectMapper mapper = new ObjectMapper();
                  String js = mapper.writeValueAsString(articleNew);
                  cachingService.set(key, js);
                } catch(Exception ex) {
                }
                future.complete(articleNew);
            });
        return future;
    }
    private String getArticleServiceUri() {
        return String.format("http://%s/article/extract", articleServiceEndpoint);
    }
    private CompletableFuture<Article> getArticleAsync(String uri, String articleUrl) {
        Article article = null;
        RestTemplate restTemplate = new RestTemplate();
        String fullUri = String.format("%s?url=%s", uri, articleUrl);
        String json = restTemplate.getForObject(fullUri, String.class);
        System.out.println(json);
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(json);
            article = mapper.readValue(json, Article.class);
        } catch (IOException ex) {
            article = null;
        }
        return CompletableFuture.completedFuture(article);
    }
}
