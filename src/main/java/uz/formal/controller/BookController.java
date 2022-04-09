package uz.formal.controller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.formal.payload.Book;
import uz.formal.service.BookService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookService bookService;
    @GetMapping("/allBook")
    public String getAllBook(Model model) throws SQLException, ClassNotFoundException {
        List<Book> allBook = bookService.getAllBook(false,0);
        model.addAttribute("books",allBook);
        return "book";
    }

    @GetMapping("/addBook")
    public String getaddBook(){
        return "add";
    }

    @SneakyThrows
    @PostMapping("/addBook")
    public String addBook(Model model, HttpServletRequest request){
        bookService.addBook(request);
        List<Book> allBook = bookService.getAllBook(false,0);
        model.addAttribute("books",allBook);
        return "book";
    }

    @GetMapping("/editOrRemove/{id}/{remove}")
    public String editOrRemove(@PathVariable Integer id, @PathVariable Boolean remove, Model model) throws SQLException, ClassNotFoundException {
        if (remove) {
            List<Book> allBook = bookService.getAllBook(true, id);
            model.addAttribute("books", allBook);
            return "book";
        }
        else {
        Book oneById = bookService.getOneById(id);
        model.addAttribute("book",oneById);
        return "saveOrEdit";}
    }

    @PostMapping("/saveOrEdit")
    public String saveOrEdit(@RequestParam(required = false) Integer id,
                             @RequestParam String title,
                             @RequestParam String author,
                             @RequestParam Double price,
                             Model model) throws SQLException, ClassNotFoundException {
        bookService.saveOrEdit(new Book(id,title,author,price));
        List<Book> allBook = bookService.getAllBook(false,0);
        model.addAttribute("books",allBook);
        return "book";
    }
}
