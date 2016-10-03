package tarun.learning.org.TweetStreamProcessing.bolt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import tarun.learning.org.TweetStreamProcessing.RealtimeView;
import tarun.learning.org.TweetStreamProcessing.RealtimeViewFactory;

public class HashTagTopTweetMapSinkBolt extends BaseRichBolt {
	private RealtimeView view;
	public static final String BOLT_NAME = "HashTagTopTweetMapSinkBolt";

	public void execute(Tuple tuple) {
		try {
			String hashTag = tuple.getStringByField(HashTagTopTweetMapperBolt.FIELD_HASHTAG);

      Map<String, String> tweetInfo = new HashMap<String, String>();
      tweetInfo.put("id", tuple.getLongByField(HashTagTopTweetMapperBolt.FIELD_ID).toString());
      tweetInfo.put("text", tuple.getStringByField(HashTagTopTweetMapperBolt.FIELD_TEXT));
      tweetInfo.put("hashtag", hashTag);
      tweetInfo.put("screenname", tuple.getStringByField(HashTagTopTweetMapperBolt.FIELD_SCREENNAME));
      tweetInfo.put("popularitycount", tuple.getIntegerByField(HashTagTopTweetMapperBolt.FIELD_POPULARITY_COUNT).toString());

			view.updateHashtagPopularTweet(hashTag, tweetInfo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		view = RealtimeViewFactory.getInstance();
	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {

	}

}
