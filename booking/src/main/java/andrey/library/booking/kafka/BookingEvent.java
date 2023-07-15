package andrey.library.booking.kafka;

public record BookingEvent(Long id, String title, Integer desiredQuantity) {}
