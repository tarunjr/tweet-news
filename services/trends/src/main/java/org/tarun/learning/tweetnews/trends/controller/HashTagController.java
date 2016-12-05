package org.tarun.learning.tweetnews.trends.controller;

import org.tarun.learning.tweetnews.trends.model.Article;
import org.tarun.learning.tweetnews.trends.service.HashTagService;
import org.tarun.learning.tweetnews.trends.service.ArticleService;
import org.tarun.learning.tweetnews.trends.model.HashTag;
import org.tarun.learning.tweetnews.trends.model.Tweet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/trends/api/v1/topics")
public class HashTagController {
    @Autowired
    private HashTagService hashTagService;
    @Autowired
    private ArticleService articleService;

    @RequestMapping("")
    public List<HashTag> hashtags() {
        return hashTagService.getAll();
    }

    @RequestMapping("/expanded")
    public List<HashTag> topHashtags(@RequestParam(value="limit", defaultValue="5") Integer limit,
                                     @RequestParam(value="articleformat", defaultValue="link") String articleformat) {
        limit = Math.min(10, limit);
        System.out.println(String.format("limit:{0}", limit));

        List<HashTag> hashTags = hashTagService.getTop(limit);

        for(HashTag hashtag: hashTags) {
            if(articleformat.equals("expanded")) {
                List<Article> articles = getArticlesExpanded(hashtag);
                hashtag.setArticles(articles);
            } else {
                List<String> urls = getArticleUrl(hashtag);
                hashtag.setArticleUrls(urls);
            }
        }
        return hashTags;
    }
    private List<String> getArticleUrl(HashTag hashtag) {

      List<CompletableFuture<String>> futures =
              hashtag.getUrls().stream()
                      .map(url -> articleService.getUrlAsync(url))
                      .collect(Collectors.toList());

      List<String> urls = futures.stream()
              .map(CompletableFuture::join)
              .collect(Collectors.toList());

      return urls;
    }
    private List<Article> getArticlesExpanded(HashTag hashtag) {

      List<CompletableFuture<Article>> futures =
                hashtag.getUrls().stream()
                      .map(url -> articleService.getExapndedAsync(url))
                      .collect(Collectors.toList());

      List<Article> articles = futures.stream()
              .map(CompletableFuture::join)
              .collect(Collectors.toList());

      return articles;
    }
}
