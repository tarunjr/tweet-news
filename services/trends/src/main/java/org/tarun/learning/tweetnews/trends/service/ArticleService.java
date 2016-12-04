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

    @Autowired
    public ArticleService(RedisCachingService cachingService,
                          ArticleRepository repository) {
        this.repository = repository;
        this.cachingService = cachingService;
    }
    @Async
    public CompletableFuture<String> getUrlAsync(String url){
        String key = "art:url:" + url;
        String articleUrl = cachingService.get(key);
        if (articleUrl != null) {
            return CompletableFuture.completedFuture(articleUrl);
        }
        articleUrl= repository.getUrl(url);
        if (articleUrl != null) {
            return CompletableFuture.completedFuture(articleUrl);
        }

        CompletableFuture<String> future = new CompletableFuture<String>();
            getArticleAsync(getArticleServiceUri(), url).thenAccept(article -> {
                repository.save(url, article);
                String u = repository.getUrl(url);
                cachingService.set(key, u);
                future.complete(u);
            });
        return future;
    }
    @Async
    public CompletableFuture<Article> getExapndedAsync(String url){
        Article article = null;
        String key = "art:url:" + url;
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
            article = mapper.readValue(json, Article.class);
        } catch (IOException ex) {
            article = null;
        }
        return CompletableFuture.completedFuture(article);
    }
}
