package andrey.library.client.feign;

import andrey.library.client.config.FeignConfig;
import andrey.library.client.dto.BookDtoRequest;
import andrey.library.client.dto.BookDtoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "client", url = "${api.url}", configuration = FeignConfig.class)
public interface BookClient {

    @PostMapping
    ResponseEntity<BookDtoResponse> createBook(BookDtoRequest bookDtoRequest);

    @GetMapping("/{title}")
    ResponseEntity<BookDtoResponse> getBook(@PathVariable String title);

    @DeleteMapping("/{title}")
    ResponseEntity<Void> removeBook(@PathVariable String title);

    @DeleteMapping("/database/clear")
    void clearDatabase();
}
