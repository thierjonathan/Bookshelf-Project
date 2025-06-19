package joel.thierry.bookshelf.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import joel.thierry.bookshelf.dto.BookDTO;
import joel.thierry.bookshelf.external.googlebooks.BookResponse;
import joel.thierry.bookshelf.external.googlebooks.GoogleBookService;
import joel.thierry.bookshelf.mapper.Mapper;
import joel.thierry.bookshelf.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class AuthorService {
    private final GoogleBookService googleBookService;
    private final ObjectMapper objectMapper;
    private final Mapper mapper;

    public AuthorService(GoogleBookService googleBookService, ObjectMapper objectMapper, Mapper mapper) {
        this.googleBookService = googleBookService;
        this.objectMapper = objectMapper;
        this.mapper = mapper;
    }

    public List<String> getAuthors(String author) {
        String rawResponse = googleBookService.searchBooksRaw("inauthor:\"" + author + "\"");
        return extractAuthors(rawResponse, author);
    }

    public List<BookDTO> getBooksByAuthor(String author) {
        JsonNode jsonNode = googleBookService.searchBooks("inauthor:" + author);
        List<BookDTO> bookDTOS = new ArrayList<>();

        if (jsonNode != null && jsonNode.has("items")) {
            for (JsonNode item : jsonNode.get("items")) {
                try {
                    BookResponse book = objectMapper.treeToValue(item, BookResponse.class);
                    bookDTOS.add(mapper.convertToBookDTO(book));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        return bookDTOS;
    }

    private List<String> extractAuthors(String apiResponse, String authorName) {
        List<String> authors = new ArrayList<>();
        try {
            JsonNode base = objectMapper.readTree(apiResponse);
            JsonNode items = base.get("items");

            if (items != null) {
                for (JsonNode item : items) {
                    JsonNode volumeInfo = item.get("volumeInfo");

                    if (volumeInfo.has("authors")) {
                        List<String> authorList = StreamSupport.stream(volumeInfo.get("authors").spliterator(), false)
                                .map(JsonNode::asText)
                                .filter(name -> name.toLowerCase().contains(authorName.toLowerCase()))
                                .collect(Collectors.toList());

                        for (String a : authorList) {
                            if (!authors.contains(a)) {
                                authors.add(a);
                            }
                        }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return authors;
    }
}
