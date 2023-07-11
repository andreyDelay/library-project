package andrey.library.books.kafka.impl;

import andrey.library.books.config.KafkaClientProperties;
import andrey.library.books.kafka.BookingEvent;
import andrey.library.books.kafka.BookingEventMessageHandler;
import andrey.library.books.kafka.BookingStatus;
import andrey.library.books.kafka.BookingStatusEvent;
import andrey.library.books.repository.BooksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventMessageHandlerImpl implements BookingEventMessageHandler {

    private final BooksRepository booksRepository;
    private final KafkaClientProperties kafkaClientProperties;
    private final KafkaTemplate<String, BookingStatusEvent> kafkaTemplate;

    @Override
    public void sendBookingStatus(BookingStatusEvent bookingResponse) {
        try {
            log.info("Starting sending booking response to kafka topic.");
            kafkaTemplate.send(kafkaClientProperties.getProducerTopicName(), bookingResponse)
                .whenComplete(
                    (result, ex) -> {
                        if (Objects.isNull(ex)) {
                            log.info("Message-response to booking-service have been successfully sent to broker. "
                                    + "Booking id: {}", bookingResponse.getId());
                        } else {
                            log.error("Fail. Couldn't to send message to broker. Booking id: {}",
                                        bookingResponse.getId());
                        }
                    });
        } catch (Exception e) {
            log.error("Error during sending response-message to kafka. Message: {}", bookingResponse, e);
        }
    }

    @Override
    @Transactional
    @KafkaListener(topics = "${spring.kafka.consumer-topic-name}", containerFactory = "listenerContainerFactory")
    public void consumeBookingEvent(BookingEvent bookingEvent) {
        log.info("Accepting booking event a book: {}.", bookingEvent.title());
        BookingStatusEvent bookingResponseMessage = BookingStatusEvent.builder()
                .bookingStatus(BookingStatus.ERROR)
                .id(bookingEvent.id())
                .build();
        booksRepository.findByTitle(bookingEvent.title())
                .ifPresent(book -> {
                    int actualQuantityInStock = book.getQuantityInStock();
                    int desiredBooksQuantity = bookingEvent.desiredQuantity();
                    int decreasedStock = actualQuantityInStock - desiredBooksQuantity;
                    if (decreasedStock >= 0) {
                        bookingResponseMessage.setBorrowedQty(bookingEvent.desiredQuantity());
                        bookingResponseMessage.setBookingStatus(BookingStatus.ACTIVE);
                        book.setQuantityInStock(decreasedStock);
                        booksRepository.save(book);
                    }
                });
        log.info("Booking event processed.");
        sendBookingStatus(bookingResponseMessage);
    }
}
