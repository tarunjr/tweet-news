package tarun.learning.org.TweetStreamProcessing.bolt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import tarun.learning.org.TweetStreamProcessing.RealtimeView;
import tarun.learning.org.TweetStreamProcessing.RealtimeViewFactory;

public class HashTagURLMapSinkBolt extends BaseRichBolt {
	private RealtimeView view;
	public static final String BOLT_NAME = "HashTagURLMapSinkBolt";
	
	public void execute(Tuple tuple) {
		try {
			String hashTag = tuple.getStringByField(HashTagURLMapperBolt.FIELD_HASHTAG);
			String url = tuple.getStringByField(HashTagURLMapperBolt.FIELD_URL);
			List<String> urls = new ArrayList<String>();
			urls.add(url);
			view.updateHashtagUrlMapping(hashTag, urls);
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
