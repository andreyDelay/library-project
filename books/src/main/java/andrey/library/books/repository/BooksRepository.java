package andrey.library.books.repository;

import andrey.library.books.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BooksRepository extends CrudRepository<Book, Long> {

    Book save(Book book);

    Optional<Book> findByTitle(String title);
}
