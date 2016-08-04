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

public class HashTagURLMapperBolt extends BaseRichBolt{
	
	public static final String BOLT_NAME = "HashTagURLMapperBolt";
	public static final String FIELD_HASHTAG = "hashtag";
	public static final String FIELD_URL = "url";
	public static final String FIELD_ID = "id";
	
	private OutputCollector collector;
	private AvroBinaryDecoder<Tweet> decoder;
	
	public void execute(Tuple tuple) {
		try {
			TweetData data = (TweetData)tuple.getValue(1);
			
			if (data.getHashTags().size() > 0 && data.getUrls().size() > 0){ 
				for (CharSequence hashtag: data.getHashTags()) {
					for (CharSequence url: data.getUrls()) {
						collector.emit(new Values(data.getId(),hashtag.toString(), url.toString()));
					}
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
		declarer.declare(new Fields(FIELD_ID,FIELD_HASHTAG, FIELD_URL));
	}
}