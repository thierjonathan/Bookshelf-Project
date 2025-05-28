package joel.thierry.bookshelf.external.googlebooks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookResponse {
    private String id;
    private VolumeInfo volumeInfo;

    @Data
    public static class VolumeInfo {
        private String title;
        private List<String> authors;
        private String publisher;
        private String publishedDate;
        private String description;
        private int pageCount;
        private double averageRating;
        private List<IndustryIdentifiers> industryIdentifiers;
        private ImageLinks imageLinks;
        private String language;
    }

    @Data
    public static class IndustryIdentifiers {
        private String identifier;
    }
    @Data
    public static class ImageLinks {
        private String thumbnail;
    }

}
