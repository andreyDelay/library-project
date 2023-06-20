package andrey.library.books.exception;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends ApiException {
    public BookNotFoundException(String message) {
        super("BOOK_NOT_FOUND_ERROR", message, HttpStatus.NOT_ACCEPTABLE);
    }
}
