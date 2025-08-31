package joel.thierry.bookshelf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class BookDTO {
    private String id;
    private String title;
    private List<String> authors;
    private String description;
    private int pageCount;
    private double averageRating;
    private String thumbnail;
    private String language;
}
