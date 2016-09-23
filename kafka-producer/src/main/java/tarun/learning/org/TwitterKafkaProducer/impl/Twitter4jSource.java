package tarun.learning.org.TwitterKafkaProducer.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

import com.google.common.io.Resources;

import tarun.learning.org.TwitterKafkaProducer.interfaces.Source;
import tarun.learning.org.TwitterKafkaProducer.interfaces.Filter;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Twitter4jSource  implements Source{
	// Class for listening on the tweet stream - for twitter4j
	  private class TweetListener implements StatusListener {

	    // Implement the callback function when a tweet arrives
	    @Override
	    public void onStatus(Status status)
	    {
	    	 // filter and add to the queue
	    	if (statusFilter.filter(status))
	    		queue.offer(status);
	    }
	    @Override
	    public void onTrackLimitationNotice(int i)
	    {
	    }
	    @Override
	    public void onScrubGeo(long l, long l1)
	    {
	    }
	    @Override
	    public void onException(Exception e)
	    {
	      e.printStackTrace();
	    }
			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
			}
	  };
	private final  BlockingQueue<Status> queue;
	// Twitter4j - twitter stream to get tweets
	private TwitterStream twitterStream;
	private final Filter statusFilter;

	public Twitter4jSource(BlockingQueue<Status> queue, Filter statusFilter) {
			this.queue = queue;
			this.statusFilter = statusFilter;
	}

	@Override
	public void start() {
			ConfigurationBuilder config = getConfig();

	    // create the twitter stream factory with the config
	    TwitterStreamFactory fact =
	        new TwitterStreamFactory(config.build());

	    // get an instance of twitter stream
	    twitterStream = fact.getInstance();

	 		// provide the handler for twitter stream
	    twitterStream.addListener(new TweetListener());
			twitterStream.sample();
	}
	private ConfigurationBuilder getConfig() {

		Properties properties = new Properties();
		try (InputStream props = Resources.getResource("twitter.props").openStream()) {

            properties.load(props);
        } catch (IOException e) {
			e.printStackTrace();
		}
		// build the config with credentials for twitter 4j
	    ConfigurationBuilder config =
	        new ConfigurationBuilder()
	               .setOAuthConsumerKey(properties.getProperty("consumerKey"))
	               .setOAuthConsumerSecret(properties.getProperty("consumerSecret"))
	               .setOAuthAccessToken(properties.getProperty("accessToken"))
	               .setOAuthAccessTokenSecret(properties.getProperty("accessKey"));
	    return config;
	}
	@Override
	public void close() {
			twitterStream.cleanUp();
	    twitterStream.shutdown();
	}
}
