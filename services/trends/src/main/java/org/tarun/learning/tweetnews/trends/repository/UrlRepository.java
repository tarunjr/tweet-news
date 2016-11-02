package org.tarun.learning.tweetnews.trends.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.tarun.learning.tweetnews.trends.model.Url;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tarunrathor on 01/11/16.
 */
@Repository
public class UrlRepository {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public List<Url> getAll() {
        HashOperations<String,String,String> hashOps = redisTemplate.opsForHash();
        List<Url> urls = new ArrayList<Url>();

        String keyPattern = KeyNameSpace.kUrl + ":*";
        Set<String> keys = redisTemplate.keys(keyPattern);

        for(String key: keys) {
            Map<String, String> entry  =  hashOps.entries(key);
            Url url = new Url();
            url.setUrl(entry.get("url"));
            url.setCount(Long.decode(entry.get("count")));
            urls.add(url);
        }
        return urls;
    }
    public List<Url> getTop(int top) {
        List<Url> urls = new ArrayList<Url>();

        ZSetOperations<String,String> zSetOps = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> entries = zSetOps.reverseRangeWithScores(
                KeyNameSpace.kUrls,0,top);

        for(ZSetOperations.TypedTuple<String> entry: entries) {
            Url url = new Url();
            url.setUrl(entry.getValue());
            url.setCount((new Double(entry.getScore())).longValue());
            urls.add(url);
        }
        return urls;
    }
}
