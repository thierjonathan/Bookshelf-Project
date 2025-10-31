package joel.thierry.bookshelf.model;

import joel.thierry.bookshelf.dto.validation.ValidUserId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "shelves")
public class Shelf {
    @Id
    private String id;
    @ValidUserId
    private String userId;
    private String name;
    private boolean exclusive = false;
}
