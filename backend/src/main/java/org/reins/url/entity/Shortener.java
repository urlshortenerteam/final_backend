package org.reins.url.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shortener")
public class Shortener {
  @Id
  private String id;
  private long shorten_id;
  private String short_url;
  private String long_url;

  public Shortener() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public long getShorten_id() {
    return shorten_id;
  }

  public void setShorten_id(long shorten_id) {
    this.shorten_id = shorten_id;
  }

  public String getShort_url() {
    return short_url;
  }

  public void setShort_url(String short_url) {
    this.short_url = short_url;
  }

  public String getLong_url() {
    return long_url;
  }

  public void setLong_url(String long_url) {
    this.long_url = long_url;
  }
}
