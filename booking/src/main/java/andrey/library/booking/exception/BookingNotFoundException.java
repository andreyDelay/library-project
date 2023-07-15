package andrey.library.booking.exception;

import org.springframework.http.HttpStatus;

public class BookingNotFoundException extends ApiException {
    public BookingNotFoundException(String message) {
        super("BOOKING_NOT_FOUND_ERROR", message, HttpStatus.NOT_ACCEPTABLE);
    }
}
