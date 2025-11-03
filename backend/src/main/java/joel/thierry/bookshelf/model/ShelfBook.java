package joel.thierry.bookshelf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "shelfbook")
public class ShelfBook {
    @Id
    private String id;
    private String shelfId;
    private String bookId;
    private String userId;
}
