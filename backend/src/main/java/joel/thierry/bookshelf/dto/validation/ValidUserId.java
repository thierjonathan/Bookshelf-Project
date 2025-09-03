package joel.thierry.bookshelf.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidUserIdValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserId {
    String message() default "Invalid user ID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}