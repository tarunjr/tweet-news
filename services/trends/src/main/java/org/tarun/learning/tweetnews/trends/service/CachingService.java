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

@Service
public interface CachingService {
    public String get(String key);
    public void set(String key, String value);
}
