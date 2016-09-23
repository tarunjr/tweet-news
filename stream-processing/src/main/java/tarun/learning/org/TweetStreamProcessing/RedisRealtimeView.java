package tarun.learning.org.TweetStreamProcessing;

import java.util.List;
import java.util.Map;


import redis.clients.jedis.Jedis;


public class RedisRealtimeView  implements RealtimeView{
	// place holder to keep the connection to redis
	private Jedis jedis;

	public RedisRealtimeView() {
		jedis = new Jedis("127.0.0.1", 6379);
	}
	public void updateHashtagStats(String hashTag, Map<String, String> statsMap) {
		String key = "hashtag:" + hashTag;
		jedis.hmset(key, statsMap);
		jedis.zadd("hashtags", Long.decode(statsMap.get("count")), statsMap.get("hashtag"));
	}

	public void updateUrlStats(String url, Map<String, String> statsMap) {
		String key = "url:" + url;
		jedis.hmset(key, statsMap);
		jedis.zadd("urls", Long.decode(statsMap.get("count")), statsMap.get("url"));
	}
	public void updateHashtagUrlMapping(String hashTag, List<String> urls) {
		String key = "hu:" + hashTag;
		for (String url: urls)
			jedis.sadd(key, url);
	}
	public void close() {
		jedis.close();
	}
}
