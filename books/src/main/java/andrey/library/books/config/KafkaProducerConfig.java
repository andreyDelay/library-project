package andrey.library.books.config;

import andrey.library.books.kafka.BookingResponseMessage;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, BookingResponseMessage> kafkaTemplate(
            ProducerFactory<String, BookingResponseMessage> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, BookingResponseMessage> producerFactory(KafkaProperties kafkaProperties) {
        var props = kafkaProperties.buildProducerProperties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, BookingResponseMessage>(props);
        kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(JacksonUtils.enhancedObjectMapper()));
        return kafkaProducerFactory;
    }

    @Bean
    public NewTopic requestsTopic() {
        return TopicBuilder.name("booking-responses")
                .partitions(2)
                .replicas(3)
                .compact()
                .build();
    }
}
