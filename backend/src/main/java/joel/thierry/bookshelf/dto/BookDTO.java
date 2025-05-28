package joel.thierry.bookshelf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class BookDTO {
    private String id;
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private int pageCount;
    private double averageRating;
    private String identifier;
    private String thumbnail;
    private String language;
}
