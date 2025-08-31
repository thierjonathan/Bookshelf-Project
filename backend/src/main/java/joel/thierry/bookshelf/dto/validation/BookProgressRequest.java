package joel.thierry.bookshelf.dto.validation;

import joel.thierry.bookshelf.model.BookProgress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookProgressRequest {
    @NotBlank
    private String userId;
    @NotBlank
    private String bookId;
    @NotNull
    private BookProgress.Status status;
    // getters and setters
}