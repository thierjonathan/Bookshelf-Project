package joel.thierry.bookshelf.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import joel.thierry.bookshelf.dto.BookDTO;
import joel.thierry.bookshelf.dto.UserDTO;
import joel.thierry.bookshelf.model.Book;
import joel.thierry.bookshelf.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    public BookDTO convertToBookDTO(Book book) {
        Book.VolumeInfo volumeInfo = book.getVolumeInfo();
        return new BookDTO(
                book.getId(),
                volumeInfo.getTitle(),
                volumeInfo.getAuthors(),
                volumeInfo.getDescription(),
                volumeInfo.getPageCount(),
                volumeInfo.getAverageRating(),
                (volumeInfo.getImageLinks() !=null)? volumeInfo.getImageLinks().getThumbnail() : "",
                volumeInfo.getLanguage()
        );
    }

}
