package andrey.library.books.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ApiErrorMessageResponse> handleBookErrors(ApiException apiException) {
        log.error("Exception during operation. Message: {}. Cause: {}",
                    apiException.getMessage(),
                    apiException.getCause());
        ApiErrorMessageResponse apiErrorMessageResponse =
                new ApiErrorMessageResponse(apiException.getCode(), apiException.getMessage());
        return new ResponseEntity<>(apiErrorMessageResponse, apiException.getHttpStatus());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiErrorMessageResponse commonHandler(Exception e) {
        log.error("Unexpected exception during operation. Message: {}. Cause: {}",
                    e.getMessage(),
                    e.getCause());
        return new ApiErrorMessageResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
