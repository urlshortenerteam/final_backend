package org.reins.url.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "edit_log")
public class Edit_log implements Serializable {
  private long id;
  private long editor_id;
  private Date edit_time;
  private String shortener_id;

  public Edit_log() {
  }

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getEditor_id() {
    return editor_id;
  }

  public void setEditor_id(long editor_id) {
    this.editor_id = editor_id;
  }

  public Date getEdit_time() {
    return edit_time;
  }

  public void setEdit_time(Date edit_time) {
    this.edit_time = edit_time;
  }

  public String getShortener_id() {
    return shortener_id;
  }

  public void setShortener_id(String shortener_id) {
    this.shortener_id = shortener_id;
  }
}
