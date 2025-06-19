package joel.thierry.bookshelf.controller;

import joel.thierry.bookshelf.dto.BookDTO;
import joel.thierry.bookshelf.model.BookPagination;
import joel.thierry.bookshelf.service.AuthorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;
    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    @GetMapping("/{name}")
    public List<String> getAuthors(@PathVariable String name) {
        List<String> authors = new ArrayList<>();
        authors = authorService.getAuthors(name);
        return authors;
    }
    @GetMapping("/{author}/books")
    public List<BookDTO> getBooksByAuthor(@PathVariable String author) {
        return authorService.getBooksByAuthor(author);
    }
}
