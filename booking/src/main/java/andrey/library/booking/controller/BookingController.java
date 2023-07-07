package andrey.library.booking.controller;

import andrey.library.booking.dto.BookingRequestDto;
import andrey.library.booking.dto.BookingResponseDto;
import andrey.library.booking.service.BookingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/booking/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    BookingService bookingService;

    @PostMapping
    public BookingResponseDto borrowBook(@RequestBody BookingRequestDto bookingRequest) {
        log.info("Accepting POST HTTP request into Controller class, to set new booking for a BOOK.");
        return bookingService.borrowBook(bookingRequest);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto bookingStatus(@PathVariable Long bookingId) {
        log.info("Accepting GET HTTP request into Controller class, to get actual booking status.");
        return bookingService.getBookingStatus(bookingId);
    }
}
