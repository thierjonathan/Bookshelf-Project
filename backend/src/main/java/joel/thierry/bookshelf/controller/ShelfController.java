package joel.thierry.bookshelf.controller;

import joel.thierry.bookshelf.model.Shelf;
import joel.thierry.bookshelf.model.ShelfBook;
import joel.thierry.bookshelf.service.ShelfService;
import joel.thierry.bookshelf.repository.ShelfBookRepository;
import joel.thierry.bookshelf.repository.ShelfRepository;
import joel.thierry.bookshelf.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shelves")
@Validated
public class ShelfController {

    private final ShelfService shelfService;
    private final UserService userService;
    private final ShelfRepository shelfRepository;
    private final ShelfBookRepository shelfBookRepository;

    public ShelfController(ShelfService shelfService,
                           ShelfRepository shelfRepository,
                           ShelfBookRepository shelfBookRepository,
                           UserService userService) {
        this.userService = userService;
        this.shelfService = shelfService;
        this.shelfRepository = shelfRepository;
        this.shelfBookRepository = shelfBookRepository;
    }

    @PostMapping
    public ResponseEntity<?> createShelf(@RequestBody Shelf shelf,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        // Extract username from authenticated user
        String username = userDetails.getUsername();
        Shelf created = shelfService.createShelf(username, shelf);
        return ResponseEntity.created(URI.create("/shelves/" + created.getId())).body(created);
    }

    // New method to get all shelves for the logged-in user
    @GetMapping
    public ResponseEntity<List<Shelf>> getAllShelvesForUser(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        String userId = userService.getUserIdByUsername(username);
        List<Shelf> shelves = shelfService.getShelvesForUser(userId);
        return ResponseEntity.ok(shelves);
    }

    @GetMapping("/{shelfId}")
    public ResponseEntity<Shelf> getShelf(@PathVariable String shelfId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        String userId = userService.getUserIdByUsername(username);
        Optional<Shelf> s = shelfRepository.findByIdAndUserId(shelfId, userId);
        return s.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{shelfId}/books/{bookId}")
    public ResponseEntity<?> addBookToShelf(@PathVariable String shelfId,
                                            @PathVariable String bookId,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        try {
            shelfService.addBookToShelf(username, shelfId, bookId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{shelfId}/books/{bookId}")
    public ResponseEntity<?> removeBookFromShelf(@PathVariable String shelfId,
                                                 @PathVariable String bookId,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        String userId = userService.getUserIdByUsername(username);
        Optional<Shelf> shelf = shelfRepository.findByIdAndUserId(shelfId, userId);
        if (shelf.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        shelfService.removeBookFromShelf(userId, shelfId, bookId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{shelfId}/books")
    public ResponseEntity<List<ShelfBook>> listBooksOnShelf(@PathVariable String shelfId,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        String userId = userService.getUserIdByUsername(username);
        Optional<Shelf> shelf = shelfRepository.findByIdAndUserId(shelfId, userId);
        if (shelf.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ShelfBook> entries = shelfBookRepository.findByShelfIdAndUserId(shelfId, userId);
        return ResponseEntity.ok(entries);
    }
}