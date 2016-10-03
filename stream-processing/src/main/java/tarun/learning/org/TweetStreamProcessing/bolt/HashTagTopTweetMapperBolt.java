package tarun.learning.org.TweetStreamProcessing.bolt;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import tarun.learning.org.TweetStreamProcessing.TweetData;
import tarun.learning.org.tweet.core.AvroBinaryDecoder;
import tarun.learning.org.tweet.core.schema.Tweet;

public class HashTagTopTweetMapperBolt extends BaseRichBolt{

	public static final String BOLT_NAME = "HashTagTopTweetMapperBolt";
	public static final String FIELD_HASHTAG = "hashtag";
	public static final String FIELD_TEXT = "text";
	public static final String FIELD_ID = "id";
  public static final String FIELD_POPULARITY_COUNT = "popularityCount";
  public static final String FIELD_SCREENNAME = "screenname";

	private OutputCollector collector;
	private AvroBinaryDecoder<Tweet> decoder;

	public void execute(Tuple tuple) {
		try {
			TweetData data = (TweetData)tuple.getValue(1);

			if (data.getHashTags().size() > 0){
				for (CharSequence hashtag: data.getHashTags()) {
              int popularityCount = data.getRetweetCount() + data.getFavouriteCount();
              collector.emit(new Values(data.getId(),
                                        hashtag.toString(),
                                        data.getScreenName(),
                                        data.getText(),
                                        popularityCount));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			collector.reportError(ex);
		}
	}

	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.decoder = new AvroBinaryDecoder<Tweet>(Tweet.class);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(FIELD_ID,
                                FIELD_HASHTAG,
                                FIELD_SCREENNAME,
                                FIELD_TEXT,
                                FIELD_POPULARITY_COUNT));
	}
}
