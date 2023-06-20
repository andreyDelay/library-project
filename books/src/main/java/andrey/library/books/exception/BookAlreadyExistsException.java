package andrey.library.books.exception;

import org.springframework.http.HttpStatus;

public class BookAlreadyExistsException extends ApiException {
    public BookAlreadyExistsException(String message) {
        super("BOOK_TITLE_ALREADY_EXISTS_ERROR", message, HttpStatus.CONFLICT);
    }
}
