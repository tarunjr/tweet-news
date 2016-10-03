package org.tarun.learning.tweetnews.trends;

import java.util.List;

public class Article {
  private String id;
  private String url;
  private String source;
  private String title;
  private String summary;
  private String beginning;
  private List<String> keywords;


  public String getId(){
    return this.id;
  }
  public void setId(String id){
    this.id = id;
  }
  public String getUrl(){
    return this.url;
  }
  public void setUrl(String url){
    this.url = url;
  }
  public String getTitle(){
    return this.title;
  }
  public void setTitle(String title){
    this.title = title;
  }
  public String getSummary(){
    return this.summary;
  }
  public void setSummary(String summary){
    this.summary = summary;
  }
  public void setBeginning(String beginning){
    this.beginning = beginning;
  }
  public String getBeginning(){
    return this.beginning;
  }
  public List<String> getKeywords(){
    return this.keywords;
  }
  public void setKeywords(List<String> keywords){
    this.keywords = keywords;
  }
}
