package uz.formal.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import uz.formal.payload.Book;


import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {


    public List<Book> getAllBook(Boolean remove, Integer id) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/book",
                "postgres",
                "root123"
        );
        if (remove){
            String removeQuery="delete from book where id="+id;
            Statement statement = connection.createStatement();
            statement.execute(removeQuery);
        }
        String query="select * from book";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        List<Book> books=new ArrayList<>();
        while (resultSet.next()){
            Book book=new Book();
            book.setId(resultSet.getInt(1));
            book.setTitle(resultSet.getString(2));
            book.setAuthor(resultSet.getString(3));
            book.setPrice(resultSet.getDouble(4));
            books.add(book);
        }
        return books;
    }

    @SneakyThrows
    public void addBook( HttpServletRequest request) {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/book",
                "postgres",
                "root123"
        );
        String name = request.getParameter("title");
        String author = request.getParameter("author");
        Double price =Double.parseDouble(request.getParameter("price"));
        String saveBook="insert into book(title,author,price) values(?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(saveBook);
        preparedStatement.setString(1,name);
        preparedStatement.setString(2,author);
        preparedStatement.setDouble(3,price);
        preparedStatement.execute();
    }

    public Book getOneById(int id) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/book",
                "postgres",
                "root123"
        );
        String getOne="select * from book where id="+id;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(getOne);
        while (resultSet.next()){
            Book book = new Book();
            book.setId(resultSet.getInt(1));
            book.setTitle(resultSet.getString(2));
            book.setAuthor(resultSet.getString(3));
            book.setPrice(resultSet.getDouble(4));
            return book;
        }
        return null;
    }

    public void saveOrEdit(Book book) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/book",
                "postgres",
                "root123"
        );
        String query="";
        if (book.getId()!=null){
            query="update book set title='"+book.getTitle()+"', author='"+book.getAuthor()+"',price="+book.getPrice()+"  where id="+book.getId();
        }else {
            query="insert into book (title,author,price) values ('"+book.getTitle()+"','"+book.getAuthor()+"',"+book.getPrice()+")";
        }
        Statement statement = connection.createStatement();
        statement.execute(query);
    }
}
