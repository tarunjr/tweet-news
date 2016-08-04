package tarun.learning.org.TweetStreamProcessing;

import java.util.List;
import java.util.Map;


public interface RealtimeView {
	void updateHashtagStats(String hashTag, Map<String, String> statsMap);
	void updateUrlStats(String url, Map<String, String> statsMap);
	void updateHashtagUrlMapping(String hashtag, List<String> urls);
	void close();
	
}
