package joel.thierry.bookshelf.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @ManyToMany
    @JoinTable(name = "user_favorite_books")
    private List<Book> favoriteBooks = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_favorite_Author")
    private List<Author> favoriteAuthors = new ArrayList<>();
}
