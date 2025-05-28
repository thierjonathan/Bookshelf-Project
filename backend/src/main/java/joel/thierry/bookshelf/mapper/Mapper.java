package joel.thierry.bookshelf.mapper;

import joel.thierry.bookshelf.dto.BookDTO;
import joel.thierry.bookshelf.external.googlebooks.BookResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    public BookDTO convertToBookDTO(BookResponse book){
        BookResponse.VolumeInfo volumeInfo = book.getVolumeInfo();
        String identifier = "";
        if(volumeInfo.getIndustryIdentifiers() != null){
            identifier = volumeInfo.getIndustryIdentifiers().get(0).getIdentifier();
        }
        return new BookDTO(
                book.getId(),
                volumeInfo.getTitle(),
                volumeInfo.getAuthors(),
                volumeInfo.getPublisher(),
                volumeInfo.getPublishedDate(),
                volumeInfo.getDescription(),
                volumeInfo.getPageCount(),
                volumeInfo.getAverageRating(),
                identifier,
                (volumeInfo.getImageLinks() !=null)? volumeInfo.getImageLinks().getThumbnail() : "",
                volumeInfo.getLanguage()
        );
    }
}
