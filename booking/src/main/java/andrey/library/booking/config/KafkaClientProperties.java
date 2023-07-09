package andrey.library.booking.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "app.kafka")
public class KafkaClientProperties {

    @Value("${producer.topic}")
    private String bookingProducerTopic;

    @Value("${consumer.topic}")
    private String bookingConsumerTopic;

    @Value("${producer.partitions}")
    private Integer bookingTopicPartitions;

}
