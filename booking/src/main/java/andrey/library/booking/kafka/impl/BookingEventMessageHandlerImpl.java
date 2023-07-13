package andrey.library.booking.kafka.impl;

import andrey.library.booking.config.KafkaClientProperties;
import andrey.library.booking.kafka.BookingEvent;
import andrey.library.booking.kafka.BookingEventMessageHandler;
import andrey.library.booking.kafka.BookingStatusEvent;
import andrey.library.booking.model.BookingStatus;
import andrey.library.booking.repository.BookingRepository;
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

    private final BookingRepository bookingRepository;
    private final KafkaClientProperties kafkaClientProperties;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @Override
    public void sendBookingEventMessage(BookingEvent bookingEvent) {
        try {
            kafkaTemplate.send(kafkaClientProperties.getProducerTopicName(), bookingEvent)
                .whenComplete(
                    (result, ex) -> {
                        if (Objects.isNull(ex)) {
                            log.info("Booking event have been successfully sent to a broker.");
                        } else {
                            log.error("Fail. Couldn't to send message to broker. Error message: {}", ex.getMessage());
                        }
                    });
        } catch (Exception e) {
            log.error("Error during sending message to kafka topic. Error message: {}", bookingEvent, e);
        }
    }

    @Override
    @Transactional
    @KafkaListener(topics = "${spring.kafka.consumer-topic-name}", containerFactory = "listenerContainerFactory")
    public void consumeBookingStatusEvent(BookingStatusEvent bookingStatusEvent) {
            log.info("Receiving booking status for a booking id: {}.", bookingStatusEvent.id());
            bookingRepository.findById(bookingStatusEvent.id())
                    .ifPresent(booking -> {
                        if (booking.getBookingStatus().equals(BookingStatus.ACTIVE)) {
                            booking.setBorrowedQty(booking.getBorrowedQty() + bookingStatusEvent.borrowedQty());
                        }
                        booking.setBookingStatus(BookingStatus.ACTIVE);
                        bookingRepository.save(booking);
                    });
            log.info("Booking status processed.");
    }
}
