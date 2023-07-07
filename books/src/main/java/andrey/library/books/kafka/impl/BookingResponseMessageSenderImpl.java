package andrey.library.books.kafka.impl;

import andrey.library.books.kafka.BookingResponseMessage;
import andrey.library.books.kafka.BookingResponseMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingResponseMessageSenderImpl implements BookingResponseMessageSender {

    @Value("${application.kafka.topic.response}")
    private String topic;
    private final KafkaTemplate<String, BookingResponseMessage> kafkaTemplate;

    @Override
    public void sendMessage(BookingResponseMessage bookingResponse) {
        try {
            log.info("Starting sending booking response to kafka topic.");
            kafkaTemplate.send(topic, bookingResponse)
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
}
