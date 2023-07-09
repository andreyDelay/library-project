package andrey.library.books.kafka;

public record BookingEvent(Long id, String title, Integer desiredQuantity) {}
