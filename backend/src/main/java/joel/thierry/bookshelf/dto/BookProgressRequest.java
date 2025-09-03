package joel.thierry.bookshelf.dto;
import joel.thierry.bookshelf.dto.validation.ValidUserId;
import joel.thierry.bookshelf.model.BookProgress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookProgressRequest {
    @NotNull
    @ValidUserId
    private String userId;
    @NotBlank
    private String bookId;
    private BookProgress.Status status = BookProgress.Status.WANT_TO_READ;
}