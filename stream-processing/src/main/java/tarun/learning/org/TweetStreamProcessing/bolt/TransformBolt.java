package tarun.learning.org.TweetStreamProcessing.bolt;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import tarun.learning.org.tweet.core.AvroBinaryDecoder;
import tarun.learning.org.tweet.core.schema.Tweet;

public class TransformBolt extends BaseRichBolt{
	public static final String BOLT_NAME = "TransformBolt";
	public static final String FIELD_ID = "id";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_OBJECT = "object";
	
	private OutputCollector collector;
	private AvroBinaryDecoder<Tweet> decoder;
	
	public void execute(Tuple tuple) {
		byte[] bytes = tuple.getBinaryByField("bytes");
		;
		Tweet status = decoder.decode(bytes);
		if (status.getHashTags().size() > 0){ 
			for (CharSequence hashtag: status.getHashTags()) {
				collector.emit(new Values(status.getId(), Integer.toString(Constants.HASHTAG_TYPE) , hashtag.toString()));
			}
		} 
		if (status.getUrls().size() > 0) {
			for (CharSequence url: status.getUrls()) {
				collector.emit(new Values(status.getId(),Integer.toString(Constants.URL_TYPE), url.toString()));
			}
		} 
	}

	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.decoder = new AvroBinaryDecoder<Tweet>(Tweet.class);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(FIELD_ID,FIELD_TYPE, FIELD_OBJECT));
	}
}
