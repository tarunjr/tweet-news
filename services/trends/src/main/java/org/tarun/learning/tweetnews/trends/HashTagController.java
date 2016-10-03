package org.tarun.learning.tweetnews.trends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

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
          ht.setUrls(getUrls(entry.get("hashtag")));
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
          ht.setUrls( getNItems(getUrls(getUrls(entry.getValue())),5 ));
          ht.setTopTweets(getTopTweets(entry.getValue()));
          ht.setArticles(getArticles(ht.getUrls()));
          hashTags.add(ht);
      }
      return hashTags;
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
    private Set<String> getUrls(String hashtag) {
      SetOperations<String,String> setOps = redisTemplate.opsForSet();

      String key = KeyNameSpace.kHashTagUrls + ":" + hashtag;
      System.out.println(key);
      Set<String> urls = setOps.members(key);
      return urls;
    }
    private List<Tweet> getTopTweets(String hashtag){
        List<Tweet> topTweets = new ArrayList<Tweet>();

        ObjectMapper mapper = new ObjectMapper();
        ZSetOperations<String,String> zSetOps = redisTemplate.opsForZSet();
        String key = KeyNameSpace.kHashTagTweets + ":" + hashtag;
        System.out.println(key);
        Set<String> entries = zSetOps.reverseRange(
                                  key,0,5);

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
    private List<Article> getArticles(Set<String> urls){
        List<Article> articles = new ArrayList<Article>();
        ObjectMapper mapper = new ObjectMapper();

        try {
          RestTemplate restTemplate = new RestTemplate();
          ArticlesRequest req = new ArticlesRequest();
          List<String> urlList = new ArrayList<String>();
          urlList.addAll(urls);
          req.setUrls(urlList);
          String json = restTemplate.postForObject("http://localhost:8081/api/v1/articles/", req, String.class);
          System.out.println(json);
          ArticlesResponse arts = mapper.readValue(json, ArticlesResponse.class);
          articles = arts.getArticles();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return articles;
    }
}
