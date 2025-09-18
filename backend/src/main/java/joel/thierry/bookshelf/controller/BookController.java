package joel.thierry.bookshelf.controller;

import joel.thierry.bookshelf.dto.BookDTO;
import joel.thierry.bookshelf.model.Book;
import joel.thierry.bookshelf.model.BookPagination;
import joel.thierry.bookshelf.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("library/books")
public class BookController {
    private BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService; //dependency injection
    }

    @GetMapping("/{title}")
    public BookPagination searchBooks(@PathVariable String title) {
        List<BookDTO> books = bookService.getBooksByTitle(title);
        return new BookPagination(books.size(), books);
    }
    @GetMapping("/{id}/id")
    public BookDTO getBooksByBookId(@PathVariable String id){
        return bookService.getBookById(id);
    }

}
