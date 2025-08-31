package joel.thierry.bookshelf.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "book_progress")
public class BookProgress {
    @Id
    private String id;
    private String userId;
    private String bookId;
    private Status status;

    public enum Status {
        WANT_TO_READ,
        CURRENTLY_READING,
        READ
    }
}
