package org.reins.url.daoimpl;
import org.reins.url.dao.BookDao;
import org.reins.url.entity.Book;
import org.reins.url.entity.Figure;
import org.reins.url.repository.BookRepository;
import org.reins.url.repository.FigureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
@Transactional
@Repository
@Service
public class BookDaoImpl implements BookDao {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private FigureRepository figureRepository;
    @Override
    public int addBook(String name,String author,String ISBN,String base,int stock,double price,String intro) {
        bookRepository.addBook(name,author,ISBN,stock,price);
        List<Book> list=bookRepository.getBook();
        int ID=0;
        for (int i=0;i<list.size();++i) {
            Book book=(Book)list.get(i);
            if (book.getID()>ID) ID=book.getID();
        }
        figureRepository.insert(new Figure(ID,base,intro));
        return 1;
    }
    @Override
    public int buyBook(int ID,int number) {
        return bookRepository.buyBook(ID,number);
    }
    @Override
    public int deleteBook(int ID) {
        figureRepository.deleteById(ID);
        return bookRepository.deleteBook(ID);
    }
    @Override
    public List<Book> getBook() {
        List<Book> list=bookRepository.getBook();
        for (int i=0;i<list.size();++i) {
            Optional<Figure> figure=figureRepository.findById(list.get(i).getID());
            if (figure.isPresent()) list.get(i).setFigure(figure.get());
            else list.get(i).setFigure(null);
        }
        return list;
    }
    @Override
    public int saveBook(int ID,String name,String author,String ISBN,String base,int stock,double price,String intro) {
        figureRepository.save(new Figure(ID,base,intro));
        return bookRepository.saveBook(ID,name,author,ISBN,stock,price);
    }
}
