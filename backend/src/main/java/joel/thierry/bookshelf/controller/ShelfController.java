package joel.thierry.bookshelf.controller;

import joel.thierry.bookshelf.model.Shelf;
import joel.thierry.bookshelf.model.ShelfBook;
import joel.thierry.bookshelf.service.ShelfService;
import joel.thierry.bookshelf.repository.ShelfBookRepository;
import joel.thierry.bookshelf.repository.ShelfRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Controller for shelf management and adding/removing books from shelves.
 *
 * Notes:
 * - This controller assumes the authenticated principal's username is the application userId.
 *   If your User id is different from username, adapt how you resolve the current user's id.
 * - Endpoints:
 *   POST   /shelves                -> create a new shelf (owner is taken from authenticated user)
 *   GET    /shelves                -> list shelves for current user
 *   GET    /shelves/{shelfId}      -> get a shelf (must belong to current user)
 *   POST   /shelves/{shelfId}/books/{bookId} -> add book to shelf (enforces exclusivity)
 *   DELETE /shelves/{shelfId}/books/{bookId} -> remove book from shelf
 *   GET    /shelves/{shelfId}/books -> list ShelfBook entries for shelf (optional convenience)
 */
@RestController
@RequestMapping("/shelves")
@Validated
public class ShelfController {

    private final ShelfService shelfService;
    private final ShelfRepository shelfRepository;
    private final ShelfBookRepository shelfBookRepository;

    public ShelfController(ShelfService shelfService,
                           ShelfRepository shelfRepository,
                           ShelfBookRepository shelfBookRepository) {
        this.shelfService = shelfService;
        this.shelfRepository = shelfRepository;
        this.shelfBookRepository = shelfBookRepository;
    }

    @PostMapping
    public ResponseEntity<?> createShelf(@RequestBody Shelf shelf,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        shelf.setUserId(userId);
        Shelf created = shelfService.createShelf(shelf);
        // Return 201 Created with Location header
        return ResponseEntity.created(URI.create("/shelves/" + created.getId())).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Shelf>> listShelves(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        List<Shelf> shelves = shelfRepository.findByUserId(userId);
        return ResponseEntity.ok(shelves);
    }

    @GetMapping("/{shelfId}")
    public ResponseEntity<Shelf> getShelf(@PathVariable String shelfId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        Optional<Shelf> s = shelfRepository.findByIdAndUserId(shelfId, userId);
        return s.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{shelfId}/books/{bookId}")
    public ResponseEntity<?> addBookToShelf(@PathVariable String shelfId,
                                            @PathVariable String bookId,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        try {
            shelfService.addBookToShelf(userId, shelfId, bookId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{shelfId}/books/{bookId}")
    public ResponseEntity<?> removeBookFromShelf(@PathVariable String shelfId,
                                                 @PathVariable String bookId,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        // Optional: validate shelf belongs to user before attempt (returns 404 if not)
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
        String userId = userDetails.getUsername();
        Optional<Shelf> shelf = shelfRepository.findByIdAndUserId(shelfId, userId);
        if (shelf.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ShelfBook> entries = shelfBookRepository.findByShelfIdAndUserId(shelfId, userId);
        return ResponseEntity.ok(entries);
    }
}