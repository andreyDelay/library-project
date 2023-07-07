package andrey.library.booking.kafka.impl;

import andrey.library.booking.kafka.BookingRequestMessage;
import andrey.library.booking.kafka.BookingRequestMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingRequestMessageSenderImpl implements BookingRequestMessageSender {

    @Value("${application.kafka.topic.request}")
    private String topic;
    private final KafkaTemplate<String, BookingRequestMessage> kafkaTemplate;

    @Override
    public void sendMessage(BookingRequestMessage bookingRequest) {
        try {
            log.info("Starting sending booking request to kafka topic. Book:{}.", bookingRequest.title());
            kafkaTemplate.send(topic, bookingRequest)
                .whenComplete(
                    (result, ex) -> {
                        if (Objects.isNull(ex)) {
                            log.info("Message-request to book-service have been successfully sent to broker. "
                                    + "Book: {}", bookingRequest.title());
                        } else {
                            log.error("Fail. Couldn't to send message to broker. Book: {}",
                                    bookingRequest.title());
                        }
                    });
        } catch (Exception e) {
            log.error("Error during sending message to kafka. Message: {}", bookingRequest, e);
        }
    }
}
