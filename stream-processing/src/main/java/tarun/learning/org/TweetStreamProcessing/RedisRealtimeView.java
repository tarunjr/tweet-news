package tarun.learning.org.TweetStreamProcessing;

import java.util.List;
import java.util.Map;
import java.lang.StringBuilder;
import java.util.Properties;

import com.google.common.io.Resources;

import redis.clients.jedis.Jedis;


public class RedisRealtimeView  implements RealtimeView{
	// place holder to keep the connection to redis
	private Jedis jedis;

	public RedisRealtimeView() {
		jedis = getConfiguredJedis();
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
	public void updateHashtagPopularTweet(String hashTag, Map<String, String> tweetInfo){
			String key = "ht:" + hashTag;
			StringBuilder sb = new StringBuilder();
			sb.append("{");
						sb.append("\"id\":"); sb.append(tweetInfo.get("id"));
						sb.append(", \"text\":"); sb.append("\""); sb.append(tweetInfo.get("text"));sb.append("\"");
						sb.append(", \"hashtag\":"); sb.append("\"");sb.append(tweetInfo.get("hashtag"));sb.append("\"");
						sb.append(", \"screenname\":"); sb.append("\"");sb.append(tweetInfo.get("screenname"));sb.append("\"");
						sb.append(", \"popularitycount\":"); sb.append(tweetInfo.get("popularitycount"));
			sb.append("}");
		  jedis.zadd(key, Long.decode(tweetInfo.get("popularitycount")), sb.toString());
	}
	public void close() {
		jedis.close();
	}
	private Jedis getConfiguredJedis() {
		Jedis jedis = null;

		Properties properties = new Properties();
		try (InputStream props = Resources.getResource("redis.props").openStream()) {
            properties.load(props);
						jedis = new Jedis(properties.getProperty("redis.server.url"),
															Integer.valueOf(properties.getProperty("redis.server.port")));
    } catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException nfex) {
			nfex.printStackTrace();
		}
		return jedis;
	}
}
