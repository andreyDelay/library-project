package andrey.library.books.kafka;

public record BookingRequestMessage(Long id, String title, Integer desiredQuantity) {}
