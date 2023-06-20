package andrey.library.books.mapper;

import andrey.library.books.dto.BookDto;
import andrey.library.books.model.Book;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(uses = { MapStructAuthorMapper.class }, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapStructBookMapper {

    Book toBook(BookDto bookDto);

    @InheritInverseConfiguration
    BookDto fromBook(Book book);
}
