package joel.thierry.bookshelf.controller;

import jakarta.validation.Valid;
import joel.thierry.bookshelf.dto.BookProgressRequest;
import joel.thierry.bookshelf.model.BookProgress;
import joel.thierry.bookshelf.service.BookProgressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-progress")
public class BookProgressController {
    private final BookProgressService bookProgressService;

    public BookProgressController(BookProgressService bookProgressService) {
        this.bookProgressService = bookProgressService;
    }

    @PostMapping
    public BookProgress createProgress(@Valid @RequestBody BookProgressRequest request) {
        return bookProgressService.saveProgress(request);
    }

    @GetMapping("/user/{userId}")
    public List<BookProgress> getUserProgress(@PathVariable String userId) {
        return bookProgressService.getProgressByUser(userId);
    }

    @GetMapping("/{id}")
    public BookProgress getProgressById(@PathVariable String id) {
        return bookProgressService.getProgressById(id).orElse(null);
    }
}