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
public class HashTagController {
    @Autowired
    private StringRedisTemplate redisTemplate;


    @RequestMapping("/hashtags")
    public List<HashTag> hashtags() {
      HashOperations<String,String,String> hashOps = redisTemplate.opsForHash();
      List<HashTag> hashTags = new ArrayList<HashTag>();

      String keyPattern = KeyNameSpace.kHashTag + ":*";
      Set<String> keys = redisTemplate.keys(keyPattern);

      for(String key: keys) {
          Map<String, String> entry  =  hashOps.entries(key);
          HashTag ht = new HashTag();
          ht.setTag(entry.get("hashtag"));
          ht.setCount(Long.decode(entry.get("count")));
          hashTags.add(ht);
      }
      return hashTags;
    }

    @RequestMapping("/hashtags/top/{k}")
    public List<HashTag> topHashtags(@PathVariable(value="k") long k) {
      List<HashTag> hashTags = new ArrayList<HashTag>();

      ZSetOperations<String,String> zSetOps = redisTemplate.opsForZSet();
      Set<ZSetOperations.TypedTuple<String>> entries = zSetOps.reverseRangeWithScores(
                                  KeyNameSpace.kHashTags,0,k);

      for(ZSetOperations.TypedTuple<String> entry: entries) {
          HashTag ht = new HashTag();
          ht.setTag(entry.getValue());
          ht.setCount((new Double(entry.getScore())).longValue());
          hashTags.add(ht);
      }
      return hashTags;
    }
}
