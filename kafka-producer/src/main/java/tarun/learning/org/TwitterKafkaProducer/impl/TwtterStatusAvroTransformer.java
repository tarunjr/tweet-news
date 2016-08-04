package tarun.learning.org.TwitterKafkaProducer.impl;

import java.util.ArrayList;
import java.util.List;

import tarun.learning.org.TwitterKafkaProducer.interfaces.Transformer;
import tarun.learning.org.tweet.core.schema.Tweet;
import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.URLEntity;

public class TwtterStatusAvroTransformer implements Transformer<Status,Tweet> {

	@Override
	public Tweet transform(Status source) {
		
		List<CharSequence> hashTags = new ArrayList<CharSequence>();
		for(HashtagEntity hashTag: source.getHashtagEntities()) {
			hashTags.add(hashTag.getText());
		}
		List<CharSequence> urls = new ArrayList<CharSequence>();
		for(URLEntity url: source.getURLEntities()) {
			urls.add(url.getURL());
		}
		
		Tweet avro = Tweet.newBuilder()
							.setId(source.getId())
							.setText(source.getText())
							.setRetweetCount(source.getRetweetCount())
							.setFavouriteCount(source.getFavoriteCount())
							.setHashTags(hashTags)
							.setUrls(urls)
							.build();
	
		return avro;
	}
}
