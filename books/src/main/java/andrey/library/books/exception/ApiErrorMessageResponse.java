package andrey.library.books.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorMessageResponse {

    private String code;
    private String message;

    public ApiErrorMessageResponse(Enum<?> code, String message) {
        this.code = code.name();
        this.message = message;
    }
}
