package joel.thierry.bookshelf.controller;

import joel.thierry.bookshelf.dto.BookDTO;
import joel.thierry.bookshelf.model.BookPagination;
import joel.thierry.bookshelf.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("library/books")
public class BookController {
    private final BookService bookService;
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/{title}")
    public BookPagination searchBooks(@PathVariable String title) {
        List<BookDTO> books = bookService.getBooksByTitle(title);
        return new BookPagination(books.size(), books);
    }
}
