package tarun.learning.org.TweetStreamProcessing.bolt;

import java.util.ArrayList;
import java.util.List;
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


public class AvroDecoderBolt extends BaseRichBolt{
	public static final String BOLT_NAME = "AvroDecoderBolt";
	public static final String FIELD_ID = "id";
	public static final String FIELD_OBJECT = "object";
	
	private OutputCollector collector;
	private AvroBinaryDecoder<Tweet> decoder;
	
	public void execute(Tuple tuple) {
		byte[] bytes = tuple.getBinaryByField("bytes");
		
		Tweet tweetAvro = decoder.decode(bytes);
		TweetData data = tranform(tweetAvro);
		collector.emit(new Values(data.getId(), data));
	}
	private TweetData tranform(Tweet tweetAvro) {
		List<String> urls = new ArrayList<String>();
		List<String> hashTags = new ArrayList<String>();
		
		for(CharSequence cs: tweetAvro.getHashTags()) {
			hashTags.add(cs.toString());
		}
		for(CharSequence cs: tweetAvro.getUrls()) {
			urls.add(cs.toString());
		}
		
		TweetData data = new TweetData(tweetAvro.getId(), 
										tweetAvro.getText().toString(), 
										tweetAvro.getRetweetCount(), 
										tweetAvro.getFavouriteCount(),
										hashTags,
										urls);
		return data;
	}

	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.decoder = new AvroBinaryDecoder<Tweet>(Tweet.class);
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(FIELD_ID, FIELD_OBJECT));
	}
}