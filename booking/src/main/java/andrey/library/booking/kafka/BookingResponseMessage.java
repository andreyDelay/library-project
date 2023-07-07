package andrey.library.booking.kafka;

import andrey.library.booking.model.BookingStatus;

public record BookingResponseMessage(Long id, Integer borrowedQty, BookingStatus bookingStatus) {}
