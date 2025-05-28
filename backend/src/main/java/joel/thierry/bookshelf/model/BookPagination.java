package joel.thierry.bookshelf.model;

import joel.thierry.bookshelf.dto.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookPagination {
    private int totalResults;
    private List<BookDTO> books;
}
