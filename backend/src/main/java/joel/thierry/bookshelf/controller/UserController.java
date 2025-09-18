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
    public UserController(UserService userService, Mapper mapper) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }
}
