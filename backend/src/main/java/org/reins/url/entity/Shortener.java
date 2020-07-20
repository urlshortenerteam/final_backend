package org.reins.url.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shortener")
public class Shortener {
  @Id
  private String id;
  private long shortenId;
  private String longUrl;

  public Shortener() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public long getShortenId() {
    return shortenId;
  }

  public void setShortenId(long shortenId) {
    this.shortenId = shortenId;
  }

  public String getLongUrl() {
    return longUrl;
  }

  public void setLongUrl(String longUrl) {
    this.longUrl = longUrl;
  }
}
