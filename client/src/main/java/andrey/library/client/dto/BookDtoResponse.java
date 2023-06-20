package andrey.library.client.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDtoResponse {
        String title;
        Integer quantityInStock;
        List<AuthorDto> authors;
}
