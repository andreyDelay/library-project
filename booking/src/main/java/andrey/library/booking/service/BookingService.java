package andrey.library.booking.service;

import andrey.library.booking.dto.BookingRequestDto;
import andrey.library.booking.dto.BookingResponseDto;
import andrey.library.booking.kafka.BookingResponseMessage;

import java.util.List;

public interface BookingService {
    BookingResponseDto borrowBook(BookingRequestDto bookingRequest);

    BookingResponseDto getBookingStatus(Long bookingId);

    void acceptBookingResponse(List<BookingResponseMessage> bookingResponseMessages);
}