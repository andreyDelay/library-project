package andrey.library.booking.kafka;

public record BookingRequestMessage(Long id, String title, Integer desiredQuantity) {}
