package andrey.library.books.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app.kafka")
public class KafkaClientProperties {

    @Value("${producer.topic}")
    private String bookingProducerTopic;

    @Value("${consumer.topic}")
    private String bookingConsumerTopic;

    @Value("${producer.partitions}")
    private Integer bookingTopicPartitions;

}
