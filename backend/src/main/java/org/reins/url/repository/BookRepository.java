package org.reins.url.repository;
import org.reins.url.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface BookRepository extends JpaRepository<Book,Integer> {
    @Modifying
    @Query(value="insert into Book(name,author,ISBN,stock,price) values(:name,:author,:ISBN,:stock,:price)",nativeQuery=true)
    int addBook(@Param("name")String name,@Param("author")String author,@Param("ISBN")String ISBN,@Param("stock")int stock,@Param("price")double price);
    @Modifying
    @Query(value="update Book set stock=stock-:number where ID=:ID",nativeQuery=true)
    int buyBook(@Param("ID")int ID,@Param("number")int number);
    @Modifying
    @Query(value="delete from Book where ID=:ID",nativeQuery=true)
    int deleteBook(@Param("ID")int ID);
    @Query("select b from Book b")
    List<Book> getBook();
    @Modifying
    @Query(value="update Book set name=:name,author=:author,ISBN=:ISBN,stock=:stock,price=:price where ID=:ID",nativeQuery=true)
    int saveBook(@Param("ID")int iD,@Param("name")String name,@Param("author")String author,@Param("ISBN")String ISBN,@Param("stock")int stock,@Param("price")double price);
}
