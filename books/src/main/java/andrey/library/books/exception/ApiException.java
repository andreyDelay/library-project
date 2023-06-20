package andrey.library.books.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiException extends RuntimeException {

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
