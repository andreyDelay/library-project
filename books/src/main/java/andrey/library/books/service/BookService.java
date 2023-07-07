package andrey.library.books.service;

import andrey.library.books.dto.BookDto;
import andrey.library.books.kafka.BookingRequestMessage;

import java.util.List;

public interface BookService {

    BookDto save(BookDto book);

    BookDto findByTitle(String title);

    void deleteByTitle(String title);

    void clearDatabase();

    void acceptBookingRequest(List<BookingRequestMessage> bookingRequestMessages);

}
