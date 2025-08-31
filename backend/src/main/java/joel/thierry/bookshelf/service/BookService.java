package joel.thierry.bookshelf.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import joel.thierry.bookshelf.dto.BookDTO;
import joel.thierry.bookshelf.mapper.Mapper;
import joel.thierry.bookshelf.model.Book;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import java.util.List;

@Service
public class BookService {
    private final WebClient webClient;
    private final Mapper mapper;
    private final ObjectMapper jacksonObjectMapper;

    public BookService(WebClient.Builder webClientBuilder, Mapper mapper, ObjectMapper jacksonObjectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://www.googleapis.com/books/v1").build();
        this.mapper = mapper;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public List<BookDTO> getBooksByTitle(String title) {
        String titleURL = "/volumes?q=intitle:" + title; // Wrap title in quotes
        List<BookDTO> bookDTO = new ArrayList<>();

        JsonNode jsonNode = webClient.get()
                .uri(titleURL)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if(jsonNode != null && jsonNode.has("items")) {
            for(JsonNode item : jsonNode.get("items")) {
                try {
                    Book book = jacksonObjectMapper.treeToValue(item, Book.class);
                    bookDTO.add(mapper.convertToBookDTO(book));
                }catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return bookDTO;
    }
}