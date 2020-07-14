package org.reins.url.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "visit_log")
public class Visit_log implements Serializable {
    private long id;
    private String shortener_id;
    private Date visit_time;
    private String ip;
    private Boolean device;

    public Visit_log() {
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

    public String getShortener_id() {
        return shortener_id;
    }

    public void setShortener_id(String shortener_id) {
        this.shortener_id = shortener_id;
    }

    public Date getVisit_time() {
        return visit_time;
    }

    public void setVisit_time(Date visit_time) {
        this.visit_time = visit_time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getDevice() {
        return device;
    }

    public void setDevice(Boolean device) {
        this.device = device;
    }
}
