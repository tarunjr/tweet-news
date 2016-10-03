package org.tarun.learning.tweetnews.trends;

import java.util.Set;

public class HashTag {
  private String tag;
  private long count;
  private Set<String> urls;

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
}
