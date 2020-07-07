package org.reins.url.dao;
import org.reins.url.entity.Users;
import java.util.List;
public interface BookDao {
    int addBook(String name,String author,String ISBN,String base,int stock,double price,String intro);
    int buyBook(int ID,int number);
    int deleteBook(int ID);
    List<Users> getBook();
    int saveBook(int ID,String name,String author,String ISBN,String base,int stock,double price,String intro);
}
