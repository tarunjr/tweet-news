package org.tarun.learning.tweetnews.trends;

public class Tweet {
  private long id;
  private String hashtag;
  private String text;
  private String screenname;
  private int popularitycount;

  public long getId(){
    return this.id;
  }
  public void setId(long id){
    this.id = id;
  }
  public String getText(){
    return this.text;
  }
  public void setText(String text){
    this.text = text;
  }
  public String getHashtag(){
    return this.hashtag;
  }
  public void setHashtag(String hashtag){
    this.hashtag = hashtag;
  }
  public String getScreenname(){
    return this.screenname;
  }
  public void setScreenname(String screenname){
    this.screenname = screenname;
  }
  public void setPopularitycount(int popularitycount){
    this.popularitycount = popularitycount;
  }
  public int getPopularitycount(){
    return this.popularitycount;
  }
}
