package andrey.library.books.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDto {
    String title;
    Integer quantityInStock;
    List<AuthorDto> authors;
}
