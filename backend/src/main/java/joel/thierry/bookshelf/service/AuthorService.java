// backend/src/main/java/joel/thierry/bookshelf/service/AuthorService.java
package joel.thierry.bookshelf.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import joel.thierry.bookshelf.dto.BookDTO;
import joel.thierry.bookshelf.mapper.Mapper;
import joel.thierry.bookshelf.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorService {
    private final WebClient webClient;
    private final ObjectMapper jacksonObjectMapper;
    private final Mapper mapper;
    private final String apiKey;

    public AuthorService(WebClient.Builder webClientBuilder, Mapper mapper, ObjectMapper jacksonObjectMapper,
                         @Value("${GOOGLE_BOOK_API_KEY}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl("https://www.googleapis.com/books/v1").build();
        this.mapper = mapper;
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.apiKey = apiKey;
    }

    public List<String> getAuthors(String author) {
        String authorURL = "/volumes?q=inauthor:\"" + author + "\"&key=" + apiKey;
        return webClient.get()
                .uri(authorURL)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> this.extractAuthors(response, author))
                .block();
    }

    public List<BookDTO> getBooksByAuthor(String author) {
        String authorURL = "/volumes?q=inauthor:" + author + "&key=" + apiKey;
        List<BookDTO> bookDTOS = new ArrayList<>();
        JsonNode jsonNode =  webClient.get()
                .uri(authorURL)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if(jsonNode != null && jsonNode.has("items")) {
            for(JsonNode item : jsonNode.get("items")) {
                try {
                    JsonNode volumeInfo = item.get("volumeInfo");
                    if(volumeInfo != null && volumeInfo.has("authors")) {
                        boolean hasExactAuthor = StreamSupport.stream(volumeInfo.get("authors").spliterator(), false)
                                .anyMatch(a -> a.asText().equalsIgnoreCase(author));
                        if(hasExactAuthor) {
                            Book book = jacksonObjectMapper.treeToValue(item, Book.class);
                            bookDTOS.add(mapper.convertToBookDTO(book));
                        }
                    }
                }catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return bookDTOS;
    }

    public List<BookDTO> searchBooksByAuthor(String query){
        String authorURL = "/volumes?q=inauthor:" + query + "&key=" + apiKey;
        List<BookDTO> bookDTOS = new ArrayList<>();
        JsonNode jsonNode =  webClient.get()
                .uri(authorURL)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        if (jsonNode != null && jsonNode.has("items")) {
            for (JsonNode item : jsonNode.get("items")) {
                try {
                    JsonNode volumeInfo = item.get("volumeInfo");
                    if (volumeInfo != null && volumeInfo.has("authors")) {
                        boolean matches = StreamSupport.stream(volumeInfo.get("authors").spliterator(), false)
                                .anyMatch(a -> a.asText().toLowerCase().contains(query.toLowerCase()));

                        if (matches) {
                            Book book = jacksonObjectMapper.treeToValue(item, Book.class);
                            bookDTOS.add(mapper.convertToBookDTO(book));
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return bookDTOS;
    }

    private List<String> extractAuthors(String ApiResponse, String authorName){
        List<String> authors = new ArrayList<>();
        try {
            JsonNode base = jacksonObjectMapper.readTree(ApiResponse);
            JsonNode items = base.get("items");

            if(items != null) {
                for (JsonNode item : items) {
                    JsonNode volumeInfo = item.get("volumeInfo");

                    if(volumeInfo.has("authors")) {
                        List<String> authorList = StreamSupport.stream(volumeInfo.get("authors").spliterator(), false)
                                .map(JsonNode::asText)
                                .filter(name -> name.toLowerCase().contains(authorName.toLowerCase()))
                                .collect(Collectors.toList());

                        for(String author : authorList){
                            if(!authors.contains(author)){
                                authors.add(author);
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
