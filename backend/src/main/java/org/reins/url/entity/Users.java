package org.reins.url.entity;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="users")
public class Users implements Serializable {
    private int id;
    private String name;
    private String password;
    public Users() {
    }
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id=id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name=name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password=password;
    }
    private Shortener shortener;
    @Transient
    public Shortener getShortener(){
        return shortener;
    }
    public void setShortener(Shortener shortener) {
        this.shortener = shortener;
    }
}
