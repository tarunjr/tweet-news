package org.tarun.learning.tweetnews.trends.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import org.tarun.learning.tweetnews.trends.model.Article;


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
        return null;
    }
    public void save(Article  article) {
        return;
    }
}
