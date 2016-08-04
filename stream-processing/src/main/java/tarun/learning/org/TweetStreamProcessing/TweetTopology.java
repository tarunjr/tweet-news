package tarun.learning.org.TweetStreamProcessing;



import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;

import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.RawMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

import tarun.learning.org.TweetStreamProcessing.bolt.AvroDecoderBolt;
import tarun.learning.org.TweetStreamProcessing.bolt.CountReportSinkBolt;
import tarun.learning.org.TweetStreamProcessing.bolt.CountTokenizerBolt;
import tarun.learning.org.TweetStreamProcessing.bolt.HashTagURLMapSinkBolt;
import tarun.learning.org.TweetStreamProcessing.bolt.HashTagURLMapperBolt;
import tarun.learning.org.TweetStreamProcessing.bolt.RollingCountBolt;
import tarun.learning.org.tweet.core.Constants;

public class TweetTopology {
	private static final String TWEET_SPOUT = "kafkaTweetSpout";

	
	public static void main(String[] args) throws Exception
	{
		  TopologyBuilder builder = new TopologyBuilder();
		  builder.setSpout(TWEET_SPOUT, getKafkaSpout(Constants.KAFKA_TWEET_TOPIC), 1);
		  
		  builder.setBolt(AvroDecoderBolt.BOLT_NAME, new AvroDecoderBolt(), 3)
		  		.shuffleGrouping(TWEET_SPOUT);
		  
		  builder.setBolt(CountTokenizerBolt.BOLT_NAME, new CountTokenizerBolt(), 3)
		  		.shuffleGrouping(AvroDecoderBolt.BOLT_NAME);
		  
		  builder.setBolt(RollingCountBolt.BOLT_NAME, new RollingCountBolt(), 3)
		  		.fieldsGrouping(CountTokenizerBolt.BOLT_NAME, new Fields(CountTokenizerBolt.FIELD_TYPE,CountTokenizerBolt.FIELD_VALUE));
		  
		  builder.setBolt(CountReportSinkBolt.BOLT_NAME, new CountReportSinkBolt(), 1)
		  		.globalGrouping(RollingCountBolt.BOLT_NAME);
		  
		  
		  builder.setBolt(HashTagURLMapperBolt.BOLT_NAME, new HashTagURLMapperBolt(), 5)
	  			.shuffleGrouping(AvroDecoderBolt.BOLT_NAME);
	  
		  builder.setBolt(HashTagURLMapSinkBolt.BOLT_NAME, new HashTagURLMapSinkBolt(), 1)
	  			.globalGrouping(HashTagURLMapperBolt.BOLT_NAME);
	  
		  // create the default config object
		  Config conf = new Config();
		  conf.registerSerialization(TweetData.class);
		  // set the config in debugging mode
		  conf.setDebug(true);

		  if (args != null && args.length > 0) {

		      // run it in a live cluster

		      // set the number of workers for running all spout and bolt tasks
		      conf.setNumWorkers(3);

		      // create the topology and submit with config
		      StormSubmitter.submitTopology(args[0], conf, builder.createTopology());

		   } else {

		      // run it in a simulated local cluster

		      // set the number of threads to run - similar to setting number of workers in live cluster
		      conf.setMaxTaskParallelism(3);

		      // create the local cluster instance
		      LocalCluster cluster = new LocalCluster();

		      // submit the topology to the local cluster
		      cluster.submitTopology("tweet-stats", conf, builder.createTopology());

		      // let the topology run for 300 seconds. note topologies never terminate!
		      Utils.sleep(300000);

		      // now kill the topology
		      cluster.killTopology("tweet-stats");

		      // we are done, so shutdown the local cluster
		      cluster.shutdown();
		    }
	 }
	
	 private static KafkaSpout getKafkaSpout(String topicName) {
		  ZkHosts hosts = new ZkHosts("127.0.0.1:2181");
		  SpoutConfig spoutConfig = new SpoutConfig(hosts, topicName, "/" + topicName, "TweetTopologyKafkaSpout");
		  spoutConfig.scheme = new RawMultiScheme();
		  KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
		  return kafkaSpout;
	  }
}
