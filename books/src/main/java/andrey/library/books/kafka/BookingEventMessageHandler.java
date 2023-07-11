package andrey.library.books.kafka;

public interface BookingEventMessageHandler {
    void sendBookingStatus(BookingStatusEvent bookingResponseMessage);

    void consumeBookingEvent(BookingEvent bookingEvent);
}
