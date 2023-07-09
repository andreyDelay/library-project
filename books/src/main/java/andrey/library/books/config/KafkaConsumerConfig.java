package andrey.library.books.config;

import andrey.library.books.kafka.BookingEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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
    public ConsumerFactory<String, BookingEvent> consumerFactory() {
        var props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());

        StringDeserializer keyDeserializer = new StringDeserializer();
        JsonDeserializer<BookingEvent> valueDeserializer =
                new JsonDeserializer<>(BookingEvent.class, false);
        var errorHandlingKeyDeserializer = new ErrorHandlingDeserializer<>(keyDeserializer);
        var errorHandlingValueDeserializer = new ErrorHandlingDeserializer<>(valueDeserializer);

        return new DefaultKafkaConsumerFactory<>(props, errorHandlingKeyDeserializer, errorHandlingValueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingEvent> listenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, BookingEvent>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
