package joel.thierry.bookshelf.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Book {
    @Id
    private String id;
    private String title;
}
