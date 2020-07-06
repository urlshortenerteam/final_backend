package org.reins.url.serviceimpl;
import org.reins.url.dao.BookDao;
import org.reins.url.entity.Book;
import org.reins.url.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookDao bookDao;
    @Override
    public int addBook(String name,String author,String ISBN,String base,int stock,double price,String intro) {
        return bookDao.addBook(name,author,ISBN,base,stock,price,intro);
    }
    @Override
    public int buyBook(int ID,int number) {
        return bookDao.buyBook(ID,number);
    }
    @Override
    public int deleteBook(int ID) {
        return bookDao.deleteBook(ID);
    }
    @Override
    public List<Book> getBook() {
        return bookDao.getBook();
    }
    @Override
    public int saveBook(int ID,String name,String author,String ISBN,String base,int stock,double price,String intro) {
        return bookDao.saveBook(ID,name,author,ISBN,base,stock,price,intro);
    }
}
