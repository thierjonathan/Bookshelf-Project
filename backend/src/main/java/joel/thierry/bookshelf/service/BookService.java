package joel.thierry.bookshelf.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import joel.thierry.bookshelf.dto.BookDTO;
import joel.thierry.bookshelf.mapper.Mapper;
import joel.thierry.bookshelf.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class BookService {
    private final WebClient webClient;
    private final Mapper mapper;
    private final ObjectMapper jacksonObjectMapper;
    private final String apiKey;

    public BookService(WebClient.Builder webClientBuilder, Mapper mapper, ObjectMapper jacksonObjectMapper, @Value("${GOOGLE_BOOK_API_KEY}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl("https://www.googleapis.com/books/v1").build();
        this.mapper = mapper;
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.apiKey = apiKey;
    }

    public List<BookDTO> getBooksByTitle(String title) {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String titleURL = String.format("/volumes?q=intitle:%s&key=%s", encodedTitle, apiKey);
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