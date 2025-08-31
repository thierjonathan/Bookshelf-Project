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

@Service
public class AuthorService {
    private final WebClient webClient;
    private final ObjectMapper jacksonObjectMapper;
    private final Mapper mapper;

    public AuthorService(WebClient.Builder webClientBuilder, Mapper mapper, ObjectMapper jacksonObjectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://www.googleapis.com/books/v1").build();
        this.mapper = mapper;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public List<String> getAuthors(String author) {
        String authorURL = "/volumes?q=inauthor:\"" + author + "\""; // Wrap in quotes
        return webClient.get()
                .uri(authorURL)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> this.extractAuthors(response, author))
                .block();

    }

    public List<BookDTO> getBooksByAuthor(String author) {
        String authorURL = "/volumes?q=inauthor:" + author;
        List<BookDTO> bookDTOS = new ArrayList<>();
        JsonNode jsonNode =  webClient.get()
                .uri(authorURL)
                .retrieve()
                .bodyToMono(JsonNode.class)//this prepares an empty container to be filled later
                .block();

        if(jsonNode != null && jsonNode.has("items")) {
            for(JsonNode item : jsonNode.get("items")) {
                try {
                    Book book = jacksonObjectMapper.treeToValue(item, Book.class);
                    bookDTOS.add(mapper.convertToBookDTO(book));
                }catch (JsonProcessingException e) {
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
