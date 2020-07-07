package org.reins.url.entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection="shortener")
public class Shortener {
    @Id
    private String id;
    private int shorten_id;
    private String base;
    private String intro;
    public Shortener(int id, String base, String intro) {
        this.id=id;
        this.base=base;
        this.intro=intro;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id=id;
    }
    public String getBase() {
        return base;
    }
    public void setBase(String base) {
        this.base=base;
    }
    public String getIntro() {
        return intro;
    }
    public void setIntro(String intro) {
        this.intro=intro;
    }
}
