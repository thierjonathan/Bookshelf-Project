package joel.thierry.bookshelf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.awt.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private String id;
    private VolumeInfo volumeInfo;

    @Data
    public static class VolumeInfo {
        private String title;
        private List<String> authors;
        private String description;
        private int pageCount;
        private double averageRating;
        private ImageLinks imageLinks;
        private String language;
    }

    @Data
    public static class ImageLinks {
        private String thumbnail;
    }

}
