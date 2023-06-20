package andrey.library.books.mapper;

import andrey.library.books.dto.AuthorDto;
import andrey.library.books.model.Author;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapStructAuthorMapper {

    Author toAuthor(AuthorDto authorDto);

    @InheritInverseConfiguration
    AuthorDto fromAuthor(Author author);
}
