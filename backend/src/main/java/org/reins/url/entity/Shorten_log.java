package org.reins.url.entity;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Entity
@Table(name="shorten_log")
public class Shorten_log implements Serializable {
    private long id;
    private long creator_id;
    private Date create_time;
    public Shorten_log() {
    }
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id=id;
    }
    public long getCreator_id() {
        return creator_id;
    }
    public void setCreator_id(long creator_id) {
        this.creator_id=creator_id;
    }
    public Date getCreate_time() {
        return create_time;
    }
    public void setCreate_time(Date create_time) {
        this.create_time=create_time;
    }
    private List<Shortener> shortener;
    @Transient
    public List<Shortener> getShortener() {
        return shortener;
    }
    public void setShortener(List<Shortener> shortener) {
        this.shortener=shortener;
    }
}
