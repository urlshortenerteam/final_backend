package org.reins.url.entity;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="book")
public class Book implements Serializable {
    private int ID;
    private String name;
    private String author;
    private String ISBN;
    private int stock;
    private double price;
    public Book() {
    }
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID=ID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name=name;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author=author;
    }
    public String getISBN() {
        return ISBN;
    }
    public void setISBN(String ISBN) {
        this.ISBN=ISBN;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock=stock;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price=price;
    }
    private Figure figure;
    @Transient
    public Figure getFigure(){
        return figure;
    }
    public void setFigure(Figure figure) {
        this.figure=figure;
    }
}
