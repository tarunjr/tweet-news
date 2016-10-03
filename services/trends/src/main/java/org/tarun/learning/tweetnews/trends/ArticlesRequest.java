package org.tarun.learning.tweetnews.trends;

import java.util.List;

public class ArticlesRequest {
  List<String> urls;
  public List<String> getUrls() {
    return this.urls;
  }
  public void setUrls(List<String> urls) {
    this.urls = urls;
  }
}
