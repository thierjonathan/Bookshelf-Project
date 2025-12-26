package joel.thierry.bookshelf.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "shelfbook")
@CompoundIndex(name = "user_shelf_book_idx", def = "{'userId': 1, 'shelfId': 1, 'bookId': 1}", unique = true)
public class ShelfBook {
    @Id
    private String id;

    @Indexed
    private String shelfId;

    @Indexed
    private String bookId;

    @Indexed
    private String userId;

    private Instant createdAt = Instant.now();

}