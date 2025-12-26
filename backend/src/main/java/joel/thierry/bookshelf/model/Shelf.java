package joel.thierry.bookshelf.model;

import joel.thierry.bookshelf.dto.validation.ValidUserId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "shelves")
// Optional uniqueness: prevent two shelves with same name for the same user
@CompoundIndex(name = "user_shelf_name_idx", def = "{'userId': 1, 'name': 1}", unique = true)
public class Shelf {
    @Id
    private String id;

    @ValidUserId
    @Indexed
    private String userId;

    @Indexed
    private String name;

    @Indexed
    private boolean exclusive = false;

    private Instant createdAt = Instant.now();
}