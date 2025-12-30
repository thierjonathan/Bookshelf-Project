package joel.thierry.bookshelf.controller;

import jakarta.validation.Valid;
import joel.thierry.bookshelf.dto.BookProgressRequest;
import joel.thierry.bookshelf.model.BookProgress;
import joel.thierry.bookshelf.service.BookProgressService;
import joel.thierry.bookshelf.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-progress")
public class BookProgressController {
    private final BookProgressService bookProgressService;
    private final UserService userService;

    public BookProgressController(BookProgressService bookProgressService, UserService userService) {
        this.userService = userService;
        this.bookProgressService = bookProgressService;
    }

    @PostMapping
    public ResponseEntity<?> saveBookProgress(@RequestBody BookProgressRequest request,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        // Get the authenticated user's ID from the token
        String authenticatedUserId = userService.getUserIdByUsername(userDetails.getUsername());

        // Save progress with authenticated user's ID
        BookProgress savedProgress = bookProgressService.saveProgress(request, authenticatedUserId);

        return ResponseEntity.ok(savedProgress);
    }

    @GetMapping("/user/{userId}")
    public List<BookProgress> getUserProgress(@PathVariable String userId) {
        return bookProgressService.getProgressByUser(userId);
    }

    @GetMapping("/{id}")
    public BookProgress getProgressById(@PathVariable String id) {
        return bookProgressService.getProgressById(id).orElse(null);
    }
    @DeleteMapping("/{id}")
    public void deleteProgress(@PathVariable String id){
        bookProgressService.deleteProgress(id);
    }
}