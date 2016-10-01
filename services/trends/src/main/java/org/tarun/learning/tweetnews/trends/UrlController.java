package org.tarun.learning.tweetnews.trends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin
@RestController
public class UrlController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/urls")
    public List<Url> urls() {
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
    @RequestMapping("/urls/top/{k}")
    public List<Url> topUrls(@PathVariable(value="k") long k) {
      List<Url> urls = new ArrayList<Url>();

      ZSetOperations<String,String> zSetOps = redisTemplate.opsForZSet();
      Set<ZSetOperations.TypedTuple<String>> entries = zSetOps.reverseRangeWithScores(
                                  KeyNameSpace.kUrls,0,k);

      for(ZSetOperations.TypedTuple<String> entry: entries) {
          Url url = new Url();
          url.setUrl(entry.getValue());
          url.setCount((new Double(entry.getScore())).longValue());
          urls.add(url);
      }
      return urls;
    }
}
