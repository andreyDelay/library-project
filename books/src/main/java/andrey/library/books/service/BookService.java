package andrey.library.books.service;

import andrey.library.books.dto.BookDto;

public interface BookService {

    BookDto save(BookDto book);

    BookDto findByTitle(String title);

    void deleteByTitle(String title);

    void clearDatabase();

}
