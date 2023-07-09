package andrey.library.booking.service.impl;

import andrey.library.booking.dto.BookingRequestDto;
import andrey.library.booking.dto.BookingResponseDto;
import andrey.library.booking.exception.BookingNotFoundException;
import andrey.library.booking.kafka.BookingEventMessageHandler;
import andrey.library.booking.mapper.BookingMapper;
import andrey.library.booking.model.Booking;
import andrey.library.booking.model.BookingStatus;
import andrey.library.booking.repository.BookingRepository;
import andrey.library.booking.repository.ClientRepository;
import andrey.library.booking.service.BookingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    BookingMapper bookingMapper;
    ClientRepository clientRepository;
    BookingRepository bookingRepository;
    BookingEventMessageHandler bookingRequestSender;


    @Override
    @Transactional
    public BookingResponseDto borrowBook(BookingRequestDto bookingRequest) {
        log.info("Processing booking request into booking-service borrowBook() method. "
                        + "Target book tittle: {}", bookingRequest.getBookTitle());
        Booking booking = bookingMapper.toBooking(bookingRequest);
        clientRepository.findByAccount(booking.getClient().getAccount()).ifPresent(booking::setClient);
        booking.setBookingStatus(BookingStatus.CREATED);
        log.info("Setting status CREATED and saving booking entity to a database.");
        Booking savedBooking = bookingRepository.save(booking);

        bookingRequestSender.sendBookingEventMessage(bookingMapper.toBookingRequestMessage(savedBooking));
        return bookingMapper.toBookingResponse(savedBooking);
    }

    @Override
    public BookingResponseDto getBookingStatus(Long bookingId) {
        log.info("Fetching actual booking status. Booking id: {}.", bookingId);
        Booking targetBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Booking with id %s not found.", bookingId)));

        return bookingMapper.toBookingResponse(targetBooking);
    }
}
