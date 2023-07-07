package andrey.library.books.service;

import andrey.library.books.dto.BookDto;
import andrey.library.books.exception.BookAlreadyExistsException;
import andrey.library.books.exception.BookNotFoundException;
import andrey.library.books.kafka.BookingRequestMessage;
import andrey.library.books.kafka.BookingResponseMessage;
import andrey.library.books.kafka.BookingResponseMessageSender;
import andrey.library.books.kafka.BookingStatus;
import andrey.library.books.mapper.MapStructBookMapper;
import andrey.library.books.model.Author;
import andrey.library.books.model.Book;
import andrey.library.books.repository.AuthorRepository;
import andrey.library.books.repository.BooksRepository;
import andrey.library.books.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookServiceImpl implements BookService {
    MapStructBookMapper bookMapper;
    BooksRepository booksRepository;
    AuthorRepository authorRepository;
    BookingResponseMessageSender messageSender;

    @Override
    @Retryable
    @Transactional
    public BookDto save(BookDto bookDto) {
        booksRepository.findByTitle(bookDto.getTitle()).ifPresent(book -> {
            log.error("Error during saving new Book object. Book with title: \"{}\" already exists.",
                    bookDto.getTitle());
            throw new BookAlreadyExistsException(
                    String.format("Book title already exists. Title: %s", bookDto.getTitle()));
        });
        Book bookToSave = bookMapper.toBook(bookDto);
        log.info("Checking if specified authors exist in repository.");
        Set<Author> existingAuthors = bookToSave.getAuthors().stream()
                .map(author ->
                        authorRepository.findByFirstNameAndLastName(
                                author.getFirstName(),
                                author.getLastName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        existingAuthors.addAll(bookToSave.getAuthors());
        bookToSave.setAuthors(existingAuthors);
        log.info("Saving book into repository. Book object: {}.", bookToSave);
        return Optional.ofNullable(bookMapper.fromBook(booksRepository.save(bookToSave)))
                .orElseThrow(() -> new RuntimeException("Couldn't save book in repository."));
    }

    @Override
    public BookDto findByTitle(String title) {
        log.info("Fetching book from repository by title: {}", title);
        return bookMapper.fromBook(booksRepository.findByTitle(title)
                .orElseThrow(() -> new BookNotFoundException(
                        String.format("Book with title %s not found", title))));
    }

    @Override
    @Transactional
    public void deleteByTitle(String title) {
        log.info("Checking if book exists into repository for specified title: {}", title);
        Book book = booksRepository.findByTitle(title)
                .orElseThrow(() -> new BookNotFoundException(
                        String.format("Book title not found. Title: %s", title)));
        log.info("Deleting book from repository for specified title: {}", title);
        booksRepository.delete(book);
        log.info("Book of title: \"{}\" successfully deleted.", title);
    }

    @Override
    @Transactional
    public void clearDatabase() {
        log.info("Starting clearing database.");
        booksRepository.deleteAll();
        authorRepository.deleteAll();
        log.info("Database has been successfully cleaned.");
    }

    @Override
    @Transactional
    @KafkaListener(
            topics = "${application.kafka.topic.request}",
            containerFactory = "listenerContainerFactory")
    public void acceptBookingRequest(@Payload List<BookingRequestMessage> bookingRequestMessages) {
        bookingRequestMessages.forEach(bookingRequestMessage -> {
            log.info("Accepting booking request in a BOOK-SERVICE for a book: {}.", bookingRequestMessage.title());
            BookingResponseMessage bookingResponseMessage = BookingResponseMessage.builder()
                    .bookingStatus(BookingStatus.ERROR)
                    .id(bookingRequestMessage.id())
                    .build();
            booksRepository.findByTitle(bookingRequestMessage.title())
                    .ifPresent(book -> {
                        int actualQuantityInStock = book.getQuantityInStock();
                        int desiredBooksQuantity = bookingRequestMessage.desiredQuantity();
                        int decreasedStock = actualQuantityInStock - desiredBooksQuantity;
                        if (decreasedStock >= 0) {
                            bookingResponseMessage.setBorrowedQty(bookingRequestMessage.desiredQuantity());
                            bookingResponseMessage.setBookingStatus(BookingStatus.ACTIVE);
                            book.setQuantityInStock(decreasedStock);
                            booksRepository.save(book);
                        }
                    });
            log.info("Booking request in a BOOK-SERVICE for a book: {}, processed.", bookingRequestMessage.title());
            messageSender.sendMessage(bookingResponseMessage);
        });
    }
}
