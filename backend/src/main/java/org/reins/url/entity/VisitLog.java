package org.reins.url.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "visit_log")
public class VisitLog implements Serializable {
    private long id;
    private String shortenerId;
    private Date visitTime;
    private String ip;
    private Boolean device;

    public VisitLog() {
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

    public String getShortenerId() {
        return shortenerId;
    }

    public void setShortenerId(String shortenerId) {
        this.shortenerId = shortenerId;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
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
