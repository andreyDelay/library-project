package andrey.library.books.controller;

import andrey.library.books.dto.BookDto;
import andrey.library.books.service.BookServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BooksController {

    BookServiceImpl bookService;
    final String bookTitleRegExpPattern = "(\\w+\\D+\\d+)+";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto addBook(@RequestBody BookDto bookDto) {
        log.info("Accepting POST http request for bookDto: {}", bookDto);
        return bookService.save(bookDto);
    }

    @GetMapping("/{bookTitle}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto getBookByTitle(@Valid @Pattern(regexp = bookTitleRegExpPattern)
                                  @PathVariable String bookTitle) {
        log.info("Accepting GET http request for book title: {}", bookTitle);
        return bookService.findByTitle(bookTitle);
    }

    @DeleteMapping("/{bookTitle}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookByTitle(@Valid @Pattern(regexp = bookTitleRegExpPattern)
                                  @PathVariable String bookTitle) {
        log.info("Accepting DELETE http request for book title: {}", bookTitle);
        bookService.deleteByTitle(bookTitle);
    }

    @DeleteMapping("/database/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearDatabase() {
        log.info("Accepting DELETE http request to clear database completely.");
        bookService.clearDatabase();
    }

}
