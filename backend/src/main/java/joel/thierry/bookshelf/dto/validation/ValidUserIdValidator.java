package joel.thierry.bookshelf.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import joel.thierry.bookshelf.repository.UserRepository; // adjust if needed

@Component
public class ValidUserIdValidator implements ConstraintValidator<ValidUserId, String> {
    private final UserRepository userRepository;

    public ValidUserIdValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public boolean isValid(String userId, ConstraintValidatorContext context) {
        if (userId == null || userId.isBlank()) {
            return false;
        }
        return userRepository.existsById(userId); // Checks if user exists
    }
}
