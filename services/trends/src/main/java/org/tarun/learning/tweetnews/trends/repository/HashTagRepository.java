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
  /*
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
  */
  public List<HashTag> getAll() {
    List<HashTag> hashTags = new ArrayList<HashTag>();

    ZSetOperations<String,String> zSetOps = redisTemplate.opsForZSet();
    Set<ZSetOperations.TypedTuple<String>> entries = zSetOps.reverseRangeWithScores(
                                KeyNameSpace.kHashTags,0,Long.MAX_VALUE);

    for(ZSetOperations.TypedTuple<String> entry: entries) {
        HashTag ht = new HashTag();
        String hashtag = entry.getValue();

        ht.setTag(hashtag);
        ht.setCount((new Double(entry.getScore())).longValue());

        hashTags.add(ht);
    }
    return hashTags;
  }

  public List<HashTag> getTop(int top) {

    List<HashTag> hashTags = getTopHashtags(top);

    for(HashTag ht : hashTags) {
        ht.setTopTweets(getAssociatedTweets(ht.getTag(), top));
        ht.setUrls(getAssociatedUrls(ht.getTag(), top));
    }
    return hashTags;
  }
  private List<HashTag> getTopHashtags(int top) {
      ZSetOperations<String,String> zSetOps = redisTemplate.opsForZSet();
      Set<ZSetOperations.TypedTuple<String>> entries = zSetOps.rangeWithScores(
                                KeyNameSpace.kTopHashTags,0,top);

      List<HashTag> hashTags = new ArrayList<HashTag>();

      for(ZSetOperations.TypedTuple<String> entry: entries) {
        HashTag ht = new HashTag();
        String hashtag = entry.getValue();

        ht.setTag(hashtag);
        ht.setCount((new Double(entry.getScore())).longValue());

        hashTags.add(ht);
    }
    return hashTags;
  }
    private Set<String> getAssociatedUrls(String hashtag, int top) {
        System.out.println(String.format("getAssociatedUrls: %s", hashtag));
        String key = KeyNameSpace.kHashTagUrls + ":" + hashtag;

        ZSetOperations<String,String> zSetOps = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> entries = zSetOps.reverseRangeByScoreWithScores(
                key, 0, Long.MAX_VALUE, 0, top);
        Set<String> urls = new HashSet<String>();
        for(ZSetOperations.TypedTuple<String> entry: entries) {
            System.out.println(entry.getValue());
            urls.add(entry.getValue());
        }
        return urls;
    }

    private List<Tweet> getAssociatedTweets(String hashtag, int top) {
        List<Tweet> topTweets = new ArrayList<Tweet>();

        ObjectMapper mapper = new ObjectMapper();
        ZSetOperations<String,String> zSetOps = redisTemplate.opsForZSet();
        String key = KeyNameSpace.kHashTagTweets + ":" + hashtag;
        //System.out.println(key);

        String json = null;
        Set<ZSetOperations.TypedTuple<String>> entries = zSetOps.reverseRangeByScoreWithScores(
                  key,0,Long.MAX_VALUE, 0, top);

        for(ZSetOperations.TypedTuple<String> entry: entries) {
            try {
                String tweetId = entry.getValue();
                Tweet tweet = getTweet(tweetId);
                topTweets.add(tweet);
            } catch (Exception ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        System.out.println(topTweets.size());
        return  topTweets;
    }
    private Tweet getTweet(String tweetId) {
      HashOperations<String,String,String> hashOps = redisTemplate.opsForHash();
      String key = KeyNameSpace.kTweet + ":" +  tweetId;

      Map<String, String> entry  =  hashOps.entries(key);
      Tweet tweet = new Tweet();
      tweet.setId(Long.decode(entry.get("id")));
      tweet.setText(entry.get("text"));
      tweet.setHashtag(entry.get("hashtag"));
      tweet.setScreenname(entry.get("setScreenname"));
      tweet.setPopularitycount(Integer.decode(entry.get("popularitycount")));

      return tweet;
    }
}
