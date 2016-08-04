package tarun.learning.org.TweetStreamProcessing.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import tarun.learning.org.TweetStreamProcessing.RealtimeView;
import tarun.learning.org.TweetStreamProcessing.RealtimeViewFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A bolt that prints the word and count to redis
 */
public class CountReportSinkBolt extends BaseRichBolt
{
	private RealtimeView view;
	public static final String BOLT_NAME = "CountReportSinkBolt";
	
	public void prepare(
			Map                     map,
			TopologyContext         topologyContext,
			OutputCollector         outputCollector)
	{	  
		view = RealtimeViewFactory.getInstance();
	}

	public void execute(Tuple tuple)
	{
		try {
			String typeStr = tuple.getStringByField("type");
			int type = Integer.parseInt(typeStr);
			String object = tuple.getStringByField("object");
			long count = tuple.getIntegerByField("count");
			
			Map<String, String> statsMap = new HashMap<String, String>();
			if (type == Constants.HASHTAG_TYPE) {
				statsMap.put("hashtag", object);
				statsMap.put("count", Long.toString(count));
				view.updateHashtagStats(object, statsMap);
			} else if (type == Constants.URL_TYPE) {
				statsMap.put("url", object);
				statsMap.put("count", Long.toString(count));
				view.updateUrlStats(object, statsMap);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// Nothing this is a terminal bolt;
	}
}