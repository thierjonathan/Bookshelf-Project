package joel.thierry.bookshelf.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import joel.thierry.bookshelf.dto.BookDTO;
import joel.thierry.bookshelf.external.googlebooks.BookResponse;
import joel.thierry.bookshelf.external.googlebooks.GoogleBookService;
import joel.thierry.bookshelf.mapper.Mapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private final GoogleBookService googleBookService;
    private final ObjectMapper objectMapper;
    private final Mapper mapper;

    public BookService(GoogleBookService googleBookService, ObjectMapper objectMapper, Mapper mapper) {
        this.googleBookService = googleBookService;
        this.objectMapper = objectMapper;
        this.mapper = mapper;
    }

    public List<BookDTO> getBooksByTitle(String title){
        JsonNode jsonNode = googleBookService.searchBooks(title);
        List<BookDTO> books = new ArrayList<>();

        if(jsonNode != null && jsonNode.has("items")){
            for(JsonNode item : jsonNode.get("items")){
                try {
                    BookResponse book = objectMapper.treeToValue(item, BookResponse.class);
                    books.add(mapper.convertToBookDTO(book));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return books;
    }

}
