package joel.thierry.bookshelf.service;

import joel.thierry.bookshelf.dto.RegisterRequest;
import joel.thierry.bookshelf.model.User;
import joel.thierry.bookshelf.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        return userRepository.save(user);
    }

    public String getUserIdByUsername(String username) {
        // Extract the plain string _id
        return userRepository.findIdByUsername(username)
                .map(UserRepository.UserIdProjection::getId) // Get the id directly
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}