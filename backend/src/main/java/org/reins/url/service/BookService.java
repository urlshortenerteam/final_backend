package org.reins.url.service;
import org.reins.url.entity.Book;
import java.util.List;
public interface BookService {
    int addBook(String name,String author,String ISBN,String base,int stock,double price,String intro);
    int buyBook(int ID,int number);
    int deleteBook(int ID);
    List<Book> getBook();
    int saveBook(int ID,String name,String author,String ISBN,String base,int stock,double price,String intro);
}
