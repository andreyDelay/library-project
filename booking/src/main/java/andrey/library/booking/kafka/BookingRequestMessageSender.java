package andrey.library.booking.kafka;

public interface BookingRequestMessageSender {
    void sendMessage(BookingRequestMessage bookingRequest);
}
