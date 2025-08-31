package joel.thierry.bookshelf.controller;

import joel.thierry.bookshelf.dto.BookDTO;
import joel.thierry.bookshelf.dto.UserDTO;
import joel.thierry.bookshelf.mapper.Mapper;
import joel.thierry.bookshelf.model.User;
import joel.thierry.bookshelf.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final Mapper mapper;
    public UserController(UserService userService, Mapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        UserDTO userDTO = mapper.convertToUserDTO(user.get());
        return ResponseEntity.ok(userDTO);
    }
    @GetMapping("/{username}/favorite-books")
    public List<BookDTO> getFavoriteBooks(@PathVariable String username) {
        try {
            return userService.getFavoriteBook(username);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/{username}/wishlist")
    public List<BookDTO> getWishlist(@PathVariable String username) {
        try {
            return userService.getWishlist(username);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/{username}/currently-reading")
    public List<BookDTO> getCurrentlyReading(@PathVariable String username) {
        try {
            return userService.getCurrentlyReading(username);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/{username}/done-reading")
    public List<BookDTO> getDoneReading(@PathVariable String username) {
        try {
            return userService.getDoneReading(username);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{username}/favorite-books/{bookId}")
    public ResponseEntity<User> addFavoriteBook(@PathVariable String username, @PathVariable String bookId) {
        try {
            User updatedUser = userService.addFavoriteBook(username, bookId);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{username}/favorite-authors/{authorName}")
    public ResponseEntity<User> addFavoriteAuthor(@PathVariable String username, @PathVariable String authorName) {
        try{
            User updatedUser = userService.addFavoriteAuthor(username, authorName);
            return ResponseEntity.ok(updatedUser);
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{username}/wishlist/{bookId}")
    public ResponseEntity<User> addWishlist(@PathVariable String username, @PathVariable String bookId) {
        try {
            User updatedUser = userService.addToWishlist(username, bookId);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{username}/currently-reading/{bookId}")
    public ResponseEntity<User> addCurrentlyReading(@PathVariable String username, @PathVariable String bookId) {
        try {
            User updatedUser = userService.addToCurrentlyReading(username, bookId);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{username}/done-reading/{bookId}")
    public ResponseEntity<User> addDoneReading(@PathVariable String username, @PathVariable String bookId) {
        try {
            User updatedUser = userService.addToDoneReading(username, bookId);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{username}/favorite-books/{bookId}")
    public ResponseEntity<User> removeFavoriteBook(@PathVariable String username, @PathVariable String bookId) {
        try {
            User updatedUser = userService.removeFavoriteBook(username, bookId);
            return ResponseEntity.ok(updatedUser);
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{username}/favorite-authors/{bookId}")
    public ResponseEntity<User> removeFavoriteAuthor(@PathVariable String username, @PathVariable String bookId) {
        try {
            User updatedUser = userService.removeFavoriteAuthor(username, bookId);
            return ResponseEntity.ok(updatedUser);
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{username}/wishlist/{bookId}")
    public ResponseEntity<User> removeWishlist(@PathVariable String username, @PathVariable String bookId) {
        try {
            User updatedUser = userService.removeWishlist(username, bookId);
            return ResponseEntity.ok(updatedUser);
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{username}/currently-reading/{bookId}")
    public ResponseEntity<User> removeCurrentlyreading(@PathVariable String username, @PathVariable String bookId) {
        try {
            User updatedUser = userService.removeCurrentlyReading(username, bookId);
            return ResponseEntity.ok(updatedUser);
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{username}/done-reading/{bookId}")
    public ResponseEntity<User> removeDonereading(@PathVariable String username, @PathVariable String bookId) {
        try {
            User updatedUser = userService.removeDoneReading(username, bookId);
            return ResponseEntity.ok(updatedUser);
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
