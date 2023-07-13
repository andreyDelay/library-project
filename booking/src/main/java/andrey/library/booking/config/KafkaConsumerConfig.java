package andrey.library.booking.config;

import andrey.library.booking.kafka.BookingStatusEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.IsolationLevel;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, BookingStatusEvent> consumerFactory() {
        var props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        StringDeserializer keyDeserializer = new StringDeserializer();
        JsonDeserializer<BookingStatusEvent> valueDeserializer =
                new JsonDeserializer<>(BookingStatusEvent.class, false);
        var errorHandlingKeyDeserializer = new ErrorHandlingDeserializer<>(keyDeserializer);
        var errorHandlingValueDeserializer = new ErrorHandlingDeserializer<>(valueDeserializer);

        return new DefaultKafkaConsumerFactory<>(props, errorHandlingKeyDeserializer, errorHandlingValueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingStatusEvent> listenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, BookingStatusEvent>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
