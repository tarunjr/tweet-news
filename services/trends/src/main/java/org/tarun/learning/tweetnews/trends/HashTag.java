package org.tarun.learning.tweetnews.trends;

import org.tarun.learning.tweetnews.trends.Tweet;
import org.tarun.learning.tweetnews.trends.Article;
import java.util.Set;
import java.util.List;

public class HashTag {
  private String tag;
  private long count;
  private Set<String> urls;
  private List<Tweet> topTweets;
  private List<Article> articles;

  public String getTag() {
    return this.tag;
  }
  public void setTag(String tag) {
    this.tag = tag;
  }
  public long getCount() {
    return this.count;
  }
  public void setCount(long count) {
    this.count = count;
  }
  public void setUrls(Set<String> urls) {
    this.urls = urls;
  }
  public Set<String> getUrls() {
    return this.urls;
  }
  public void setTopTweets(List<Tweet> topTweets) {
    this.topTweets = topTweets;
  }
  public List<Tweet> getTopTweets() {
    return this.topTweets;
  }
  public List<Article> getArticles() {
    return this.articles;
  }
  public void setArticles(List<Article> articles) {
    this.articles = articles;
  }
}
