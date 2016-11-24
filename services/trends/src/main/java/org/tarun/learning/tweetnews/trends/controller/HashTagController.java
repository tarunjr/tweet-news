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
@RequestMapping("/trends/api/v1/hashtags")
public class HashTagController {

    @Autowired
    private HashTagService hashTagService;

    @Autowired
    private ArticleService articleService;

    @RequestMapping("")
    public List<HashTag> hashtags() {
        return hashTagService.getAll();
    }

    @RequestMapping("/top/{k}")
    public List<HashTag> topHashtags(@PathVariable(value="k") int k) {

        List<HashTag> hashTags = hashTagService.getTop(k);

        for(HashTag hashtag: hashTags) {

            List<CompletableFuture<Article>> futures =
                    hashtag.getUrls().stream()
                            .map(url -> articleService.getAsync(url))
                            .collect(Collectors.toList());

            List<Article> articles = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

            hashtag.setArticles(articles);
        }
        return hashTags;
    }

}
