package org.reins.url.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class Users implements Serializable {
    private long id;
    private String name;
    private String password;
    private int role;
    private long visit_count;

    public Users() {
    }

    public Users(long id, String name, String password, int role, long visit_count) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.visit_count = visit_count;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public long getVisit_count() {
        return visit_count;
    }

    public void setVisit_count(long visit_count) {
        this.visit_count = visit_count;
    }
}
