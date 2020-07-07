package org.reins.url.controller;
import com.alibaba.fastjson.JSONArray;
import org.reins.url.entity.Users;
import org.reins.url.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
@RestController
public class BookController {
    @Autowired
    private BookService bookService;
    @CrossOrigin
    @PostMapping(value="/addBook")
    public int addBook(HttpServletRequest request) {
        String name=request.getParameter("name");
        String author=request.getParameter("author");
        String ISBN=request.getParameter("ISBN");
        String base=request.getParameter("base");
        int stock=Integer.parseInt(request.getParameter("stock"));
        double price=Double.parseDouble(request.getParameter("price"));
        String intro=request.getParameter("intro");
        return bookService.addBook(name,author,ISBN,base,stock,price,intro);
    }
    @CrossOrigin
    @PostMapping(value="/deleteBook")
    public int deleteBook(HttpServletRequest request) {
        int ID=Integer.parseInt(request.getParameter("ID"));
        return bookService.deleteBook(ID);
    }
    @CrossOrigin
    @PostMapping(value="/getBook")
    public ArrayList<JSONArray> getBook(HttpServletRequest request) {
        String name=request.getParameter("name");
        ArrayList<JSONArray> bookJson=new ArrayList<>();
        List<Users> list=bookService.getBook();
        for (int i=0;i<list.size();++i) {
            Users users =(Users)list.get(i);
            if (name==null || users.getName().toLowerCase().contains(name.toLowerCase())) {
                ArrayList<String> temp=new ArrayList<>();
                temp.add(Integer.toString(users.getID()));
                temp.add(users.getName());
                temp.add(users.getAuthor());
                temp.add(users.getISBN());
                temp.add(users.getShortener().getBase());
                temp.add(Integer.toString(users.getStock()));
                temp.add(Double.toString(users.getPrice()));
                temp.add(users.getShortener().getIntro());
                bookJson.add((JSONArray)JSONArray.toJSON(temp));
            }
        }
        return bookJson;
    }
    @CrossOrigin
    @PostMapping(value="/saveBook")
    public int saveBook(HttpServletRequest request) {
        int ID=Integer.parseInt(request.getParameter("ID"));
        String name=request.getParameter("name");
        String author=request.getParameter("author");
        String ISBN=request.getParameter("ISBN");
        String base=request.getParameter("base");
        int stock=Integer.parseInt(request.getParameter("stock"));
        double price=Double.parseDouble(request.getParameter("price"));
        String intro=request.getParameter("intro");
        return bookService.saveBook(ID,name,author,ISBN,base,stock,price,intro);
    }
}
