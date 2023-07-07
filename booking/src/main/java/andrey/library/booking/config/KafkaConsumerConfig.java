package andrey.library.booking.config;

import andrey.library.booking.kafka.BookingResponseMessage;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    private final String BROKER_SERVER;
    private final String BOOKING_RESPONSE_TOPIC;

    public KafkaConsumerConfig(
            @Value("${spring.kafka.producer.bootstrap-servers}") String brokerServer,
            @Value("${application.kafka.topic.response}") String bookingRequestTopic) {
        BROKER_SERVER = brokerServer;
        BOOKING_RESPONSE_TOPIC = bookingRequestTopic;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_SERVER);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic bookingRequestTopic() {
        return TopicBuilder.name(BOOKING_RESPONSE_TOPIC)
                .partitions(3)
                .build();
    }

    @Bean
    public ConsumerFactory<String, BookingResponseMessage> consumerFactory(KafkaProperties kafkaProperties) {
        var props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 3_000);

        StringDeserializer keyDeserializer = new StringDeserializer();
        JsonDeserializer<BookingResponseMessage> valueDeserializer =
                new JsonDeserializer<>(BookingResponseMessage.class, false);
        var errorHandlingKeyDeserializer = new ErrorHandlingDeserializer<>(keyDeserializer);
        var errorHandlingValueDeserializer = new ErrorHandlingDeserializer<>(valueDeserializer);

        return new DefaultKafkaConsumerFactory<>(props, errorHandlingKeyDeserializer, errorHandlingValueDeserializer);
    }

    @Bean("listenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, BookingResponseMessage>>
    listenerContainerFactory(ConsumerFactory<String, BookingResponseMessage> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, BookingResponseMessage>();
        factory.getContainerProperties().setIdleBetweenPolls(1_000);
        factory.getContainerProperties().setPollTimeout(1_000);

        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);

        var executor = new SimpleAsyncTaskExecutor("book-service-consumer-");
        executor.setConcurrencyLimit(10);

        var listenerTaskExecutor = new ConcurrentTaskExecutor(executor);
        factory.getContainerProperties().setListenerTaskExecutor(listenerTaskExecutor);
        return factory;
    }
}
