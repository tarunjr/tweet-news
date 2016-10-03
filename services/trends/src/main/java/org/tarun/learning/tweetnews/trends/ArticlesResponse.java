package org.tarun.learning.tweetnews.trends;

import java.util.List;

public class ArticlesResponse {
  List<Article> articles;
  public List<Article> getArticles() {
    return this.articles;
  }
  public void setArticles(List<Article> articles) {
    this.articles = articles;
  }
}
