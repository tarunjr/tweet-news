package org.tarun.learning.tweetnews.trends.repository;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import org.tarun.learning.tweetnews.trends.model.Tweet;
import org.tarun.learning.tweetnews.trends.repository.KeyNameSpace;
import org.tarun.learning.tweetnews.trends.model.HashTag;

@Repository
public class HashTagRepository {
  @Autowired
  private StringRedisTemplate redisTemplate;

  public List<HashTag> getAll() {
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

  public List<HashTag> getTop(int top) {
    List<HashTag> hashTags = new ArrayList<HashTag>();

    ZSetOperations<String,String> zSetOps = redisTemplate.opsForZSet();
    Set<ZSetOperations.TypedTuple<String>> entries = zSetOps.reverseRangeWithScores(
                                KeyNameSpace.kHashTags,0,top);

    for(ZSetOperations.TypedTuple<String> entry: entries) {
        HashTag ht = new HashTag();
        String hashtag = entry.getValue();

        ht.setTag(hashtag);
        ht.setCount((new Double(entry.getScore())).longValue());
        ht.setTopTweets(getAssociatedTweets(hashtag, top));
        ht.setUrls(getNItems(getAssociatedUrls(hashtag), top));

        hashTags.add(ht);
    }
    return hashTags;
  }
    private Set<String> getAssociatedUrls(String hashtag) {
        SetOperations<String,String> setOps = redisTemplate.opsForSet();

        String key = KeyNameSpace.kHashTagUrls + ":" + hashtag;
        System.out.println(key);
        Set<String> urls = setOps.members(key);

        return urls;
    }
    private Set<String> getNItems(Set<String> input, int N) {
        int count = 0;
        Set<String> output = new HashSet<String>();
        for(String s: input) {
            if(++count < N)
                output.add(s);
            else
                break;
        }
        return output;
    }
    private List<Tweet> getAssociatedTweets(String hashtag, int top) {
        List<Tweet> topTweets = new ArrayList<Tweet>();

        ObjectMapper mapper = new ObjectMapper();
        ZSetOperations<String,String> zSetOps = redisTemplate.opsForZSet();
        String key = KeyNameSpace.kHashTagTweets + ":" + hashtag;
        System.out.println(key);

        Set<String> entries = zSetOps.reverseRange(
                key,0,top);

        for(String entry: entries) {
            try {
                String json = entry;
                System.out.println(json);
                Tweet tweet = mapper.readValue(json, Tweet.class);
                System.out.println("Have an object");
                topTweets.add(tweet);
            } catch (com.fasterxml.jackson.core.JsonProcessingException jpex) {
                System.out.println(jpex);
                jpex.printStackTrace();
            } catch (java.io.IOException iox) {
                System.out.println(iox);
                iox.printStackTrace();
            } catch (Exception ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        System.out.println(topTweets.size());
        return  topTweets;
    }
}
