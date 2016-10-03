package tarun.learning.org.TweetStreamProcessing;

import java.io.Serializable;
import java.util.List;

public class TweetData implements Serializable {
	private static final long serialVersionUID = 1L;
	private final long id;
	private final String screenName;
	private final String text;
	private final int retweetCount;
	private final int favouriteCount;
	private final List<String> hashTags;
	private final List<String> urls;

	public TweetData(long id, String text, String screenName, int retweetCount, int favouriteCount, List<String> hashTags, List<String> urls) {
		this.id = id;
		this.text = text;
		this.screenName = screenName;
		this.retweetCount = retweetCount;
		this.favouriteCount = favouriteCount;
		this.hashTags = hashTags;
		this.urls = urls;
	}
	public long getId() {
		return id;
	}
	public String getText() {
		return text;
	}
	public String getScreenName() {
		return screenName;
	}
	public int getRetweetCount() {
		return retweetCount;
	}
	public int getFavouriteCount() {
		return favouriteCount;
	}
	public List<String> getHashTags(){
		return hashTags;
	}
	public List<String> getUrls(){
		return urls;
	}
}
