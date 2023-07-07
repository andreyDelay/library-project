package andrey.library.books.kafka;

public interface BookingResponseMessageSender {
    void sendMessage(BookingResponseMessage bookingResponseMessage);
}
