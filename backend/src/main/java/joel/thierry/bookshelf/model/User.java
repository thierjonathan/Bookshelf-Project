package joel.thierry.bookshelf.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users") // Mongo users collection
public class User {

    @Id
    private String id; // MongoDB auto `_id`

    @Indexed(unique = true)  // <-- Enforces unique usernames
    private String username;

    private String password; // For example only; use password hashing
}