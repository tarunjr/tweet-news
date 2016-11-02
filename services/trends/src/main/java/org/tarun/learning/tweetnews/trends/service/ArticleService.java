package org.tarun.learning.tweetnews.trends.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.tarun.learning.tweetnews.trends.model.Article;

import org.springframework.web.client.RestTemplate;
import org.tarun.learning.tweetnews.trends.repository.ArticleRepository;

public class ArticleService {

    @Value("${service.article.url:127.0.0.1}")
    private String articleServiceUrl;

    @Value("${service.article.port:8081}")
    private String articleServicePort;

    private final ArticleRepository repository;

    @Autowired
    public ArticleService(ArticleRepository repository) {
        this.repository = repository;
    }
    @Async
    public CompletableFuture<Article> getAsync(String url){

        Article cachedSArticle = repository.get(url);
        if (cachedSArticle != null) {
            return CompletableFuture.completedFuture(cachedSArticle);
        }

        CompletableFuture<Article> future = new CompletableFuture<Article>();
            getArticleAsync(buildEndpoint(), url).thenAccept(article -> {
                repository.save(article);
                future.complete(article);
            });

        return future;
    }
    private String buildEndpoint() {
        return null;
    }
    private CompletableFuture<Article> getArticleAsync(String endpoint, String articleUrl) {
        Article article = null;
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.postForObject("http://localhost:8081/api/v1/articles/", articleUrl, String.class);
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
