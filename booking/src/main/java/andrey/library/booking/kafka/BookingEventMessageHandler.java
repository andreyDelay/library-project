package andrey.library.booking.kafka;

public interface BookingEventMessageHandler {
    void sendBookingEventMessage(BookingEvent eventMessage);

    void consumeBookingStatusEvent(BookingStatusEvent bookingStatusEvents);
}
