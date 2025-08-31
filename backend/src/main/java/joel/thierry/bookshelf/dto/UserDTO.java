package joel.thierry.bookshelf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {
    private String username;
    private List<String> favoriteBook = new ArrayList<>();
    private List<String> favoriteAuthor = new ArrayList<>();
    private List<String> wishlist = new ArrayList<>();
    private List<String> currentlyReading = new ArrayList<>();
    private List<String> doneReading = new ArrayList<>();
}