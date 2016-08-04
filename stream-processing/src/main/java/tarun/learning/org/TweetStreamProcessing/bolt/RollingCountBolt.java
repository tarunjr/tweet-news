/*
 * Credits: This class is copied as it is from the Udacity Storm course.
 * There is no commercial use of this project. Its purely educational.
 */

package tarun.learning.org.TweetStreamProcessing.bolt;

import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import tarun.learning.org.TweetStreamProcessing.tools.NthLastModifiedTimeTracker;
import tarun.learning.org.TweetStreamProcessing.tools.SlidingWindowCounter;
import tarun.learning.org.TweetStreamProcessing.tools.TupleHelpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This bolt performs rolling counts of incoming objects, i.e. sliding window based counting.
 * <p/>
 * The bolt is configured by two parameters, the length of the sliding window in seconds (which influences the output
 * data of the bolt, i.e. how it will count objects) and the emit frequency in seconds (which influences how often the
 * bolt will output the latest window counts). For instance, if the window length is set to an equivalent of five
 * minutes and the emit frequency to one minute, then the bolt will output the latest five-minute sliding window every
 * minute.
 * <p/>
 * The bolt emits a rolling count tuple per object, consisting of the object itself, its latest rolling count, and the
 * actual duration of the sliding window. The latter is included in case the expected sliding window length (as
 * configured by the user) is different from the actual length, e.g. due to high system load. Note that the actual
 * window length is tracked and calculated for the window, and not individually for each object within a window.
 * <p/>
 * Note: During the startup phase you will usually observe that the bolt warns you about the actual sliding window
 * length being smaller than the expected length. This behavior is expected and is caused by the way the sliding window
 * counts are initially "loaded up". You can safely ignore this warning during startup (e.g. you will see this warning
 * during the first ~ five minutes of startup time if the window length is set to five minutes).
 */
public class RollingCountBolt extends BaseRichBolt {

  private static final long serialVersionUID = 5537727428628598519L;
  public static final String BOLT_NAME = "RollingCountBolt";
  public static final String FIELD_TYPE = "type";
  public static final String FIELD_OBJECT = "object";
  public static final String FIELD_COUNT = "count";
  public static final String FIELD_ACTUAL_WINDOW_LEN = "actualWindowLengthInSeconds";
  

  private static final int NUM_WINDOW_CHUNKS = 5;
  private static final int DEFAULT_SLIDING_WINDOW_IN_SECONDS = NUM_WINDOW_CHUNKS * 60;
  private static final int DEFAULT_EMIT_FREQUENCY_IN_SECONDS = DEFAULT_SLIDING_WINDOW_IN_SECONDS / NUM_WINDOW_CHUNKS;
  private static final String WINDOW_LENGTH_WARNING_TEMPLATE =
      "Actual window length is %d seconds when it should be %d seconds"
          + " (you can safely ignore this warning during the startup phase)";

  private final Map<String,SlidingWindowCounter<Object>> counterMap;

  private final int windowLengthInSeconds;
  private final int emitFrequencyInSeconds;
  private OutputCollector collector;
  private NthLastModifiedTimeTracker lastModifiedTracker;

  public RollingCountBolt() {
    this(DEFAULT_SLIDING_WINDOW_IN_SECONDS, DEFAULT_EMIT_FREQUENCY_IN_SECONDS);
  }

  public RollingCountBolt(int windowLengthInSeconds, int emitFrequencyInSeconds) {
	this.windowLengthInSeconds = windowLengthInSeconds;
    this.emitFrequencyInSeconds = emitFrequencyInSeconds;
    
    counterMap = new HashMap<String, SlidingWindowCounter<Object>>();
    counterMap.put(Integer.toString(Constants.HASHTAG_TYPE), new SlidingWindowCounter<Object>(deriveNumWindowChunksFrom(this.windowLengthInSeconds,
            this.emitFrequencyInSeconds)));
    counterMap.put(Integer.toString(Constants.URL_TYPE), new SlidingWindowCounter<Object>(deriveNumWindowChunksFrom(this.windowLengthInSeconds,
            this.emitFrequencyInSeconds)));
  }

  private int deriveNumWindowChunksFrom(int windowLengthInSeconds, int windowUpdateFrequencyInSeconds) {
    return windowLengthInSeconds / windowUpdateFrequencyInSeconds;
  }


  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    this.collector = collector;
    lastModifiedTracker = new NthLastModifiedTimeTracker(deriveNumWindowChunksFrom(this.windowLengthInSeconds,
        this.emitFrequencyInSeconds));
  }


  public void execute(Tuple tuple) {
    if (TupleHelpers.isTickTuple(tuple)) {
     
      emitCurrentWindowCounts();
    }
    else {
      countObjAndAck(tuple);
    }
  }

  private void emitCurrentWindowCounts() {
    for(Map.Entry<String, SlidingWindowCounter<Object>> entry:  counterMap.entrySet()) {
    	String type = entry.getKey();
    	SlidingWindowCounter<Object> counter = entry.getValue();
    	
    	Map<Object, Long> counts = counter.getCountsThenAdvanceWindow();
        int actualWindowLengthInSeconds = lastModifiedTracker.secondsSinceOldestModification();
        lastModifiedTracker.markAsModified();
        
        emit(type, counts, actualWindowLengthInSeconds);
    }
  }

  private void emit(String type, Map<Object, Long> counts, int actualWindowLengthInSeconds) {
    for (Entry<Object, Long> entry : counts.entrySet()) {
      Object obj = entry.getKey();
      Long count = entry.getValue();
      Integer intCount = count != null ? count.intValue() : null;
      collector.emit(new Values(type, obj, intCount, actualWindowLengthInSeconds));
    }
  }

  private void countObjAndAck(Tuple tuple) {
    Object type = tuple.getValue(1);
    Object obj = tuple.getValue(2);
    SlidingWindowCounter<Object> counter = counterMap.get(type);
    counter.incrementCount(obj);
    collector.ack(tuple);
  }

  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields(FIELD_TYPE, FIELD_OBJECT, FIELD_COUNT, FIELD_ACTUAL_WINDOW_LEN));
  }

  @Override
  public Map<String, Object> getComponentConfiguration() {
    Map<String, Object> conf = new HashMap<String, Object>();
    conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, emitFrequencyInSeconds);
    return conf;
  }
}
