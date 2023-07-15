package andrey.library.booking.kafka;

import andrey.library.booking.model.BookingStatus;

public record BookingStatusEvent(Long id, Integer borrowedQty, BookingStatus bookingStatus) {}
