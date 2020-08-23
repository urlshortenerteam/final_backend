package org.reevoo.url.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "edit_log")
public class EditLog implements Serializable {
    private long id;
    private long editorId;
    private Date editTime;
    private String shortenerId;

    public EditLog() {
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

    public long getEditorId() {
        return editorId;
    }

    public void setEditorId(long editorId) {
        this.editorId = editorId;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getShortenerId() {
        return shortenerId;
    }

    public void setShortenerId(String shortenerId) {
        this.shortenerId = shortenerId;
    }
}
