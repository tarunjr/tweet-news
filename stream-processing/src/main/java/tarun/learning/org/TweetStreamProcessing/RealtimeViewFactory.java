package tarun.learning.org.TweetStreamProcessing;

public class RealtimeViewFactory {
	public static RealtimeView getInstance() {
		RedisRealtimeView redisView = new RedisRealtimeView();
		return redisView;
	}
};
