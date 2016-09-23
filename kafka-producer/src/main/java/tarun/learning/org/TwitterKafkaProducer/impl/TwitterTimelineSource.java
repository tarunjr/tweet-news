package tarun.learning.org.TwitterKafkaProducer.impl;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import com.google.common.io.Resources;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import tarun.learning.org.TwitterKafkaProducer.interfaces.Source;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.StatusListener;
import twitter4j.TwitterFactory;
import twitter4j.Paging;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterTimelineSource implements Source {
  private final  BlockingQueue<Status> queue;

  public TwitterTimelineSource(BlockingQueue<Status> queue) {
      this.queue = queue;
  }

  @Override
  public void start() {
    try {
      Twitter unauthenticatedTwitter = new TwitterFactory(getConfig().build()).getInstance();
      //First param of Paging() is the page number, second is the number per page (this is capped around 200 I think.
      Paging paging = new Paging(1, 100);

      for(String username: getUserNames()) {
        List<Status> statuses = unauthenticatedTwitter.getUserTimeline(username,paging);
        for(Status status: statuses)
          queue.offer(status);
        }
    }catch (TwitterException tex) {
        tex.printStackTrace();
    }
  }

  @Override
  public void close() {

  }
  private Set<String> getUserNames() {
    Set<String> setNames = new HashSet();
    try (InputStream props = Resources.getResource("filter.props").openStream()) {
          Properties properties = new Properties();
          properties.load(props);

          String screeNames = properties.getProperty("screennames");

          for (String name: screeNames.split(",")) {
                setNames.add(name);
                //System.out.println(name);
          }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return setNames;
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
}
