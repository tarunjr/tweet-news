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

public class CountTokenizerBolt  extends BaseRichBolt{
	public static final String BOLT_NAME = "CountTokenizerBolt";
	public static final String FIELD_ID = "id";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_VALUE = "value";
	
	private OutputCollector collector;
	
	public void execute(Tuple tuple) {
		try {
			TweetData data = (TweetData)tuple.getValue(1);
		
			if (data.getHashTags().size() > 0){ 
				for (CharSequence hashtag: data.getHashTags()) {
					collector.emit(new Values(data.getId(), Integer.toString(Constants.HASHTAG_TYPE) , hashtag.toString()));
				}
			} 
			if (data.getUrls().size() > 0) {
				for (CharSequence url: data.getUrls()) {
					collector.emit(new Values(data.getId(),Integer.toString(Constants.URL_TYPE), url.toString()));
				}
			} 
		} catch (Exception ex) {
			ex.printStackTrace();
			collector.reportError(ex);
		}
	}

	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(FIELD_ID,FIELD_TYPE, FIELD_VALUE));
	}
}
