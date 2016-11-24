package org.tarun.learning.tweetnews.trends.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.ValueOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.tarun.learning.tweetnews.trends.model.Article;
import org.tarun.learning.tweetnews.trends.repository.KeyNameSpace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tarunrathor on 01/11/16.
 */
@Repository
public class ArticleRepository {
    @Autowired
    private StringRedisTemplate redisTemplate;
      
    public Article get(String url) {
        Article article  = null;
        ValueOperations<String,String> ops = redisTemplate.opsForValue();

        String key = String.format("%s:%s",KeyNameSpace.kHashTagArticle, url);
        if (redisTemplate.hasKey(key)) {
            String articleJson = ops.get(key);
            ObjectMapper mapper = new ObjectMapper();
            try {
              article = mapper.readValue(articleJson, Article.class);
            } catch (Exception ex) {
              article = null;
              ex.printStackTrace();
            }
        }
        return article;
    }
    public void save(String url, Article  article) {
      ValueOperations<String,String> ops = redisTemplate.opsForValue();
      ObjectMapper mapper = new ObjectMapper();

      try {
        String articleJson = mapper.writeValueAsString(article);
        String key = String.format("%s:%s",KeyNameSpace.kHashTagArticle, url);
        ops.set(key, articleJson);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      return;
    }
}
