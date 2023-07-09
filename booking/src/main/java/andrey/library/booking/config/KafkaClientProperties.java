package andrey.library.booking.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app.kafka")
public class KafkaClientProperties {

    @Value("${producer.topic}")
    private String bookingTopic;

    @Value("${consumer.topic}")
    private String bookingStatusTopic;

    @Value("${producer.partitions}")
    private Integer bookingTopicPartitions;

}
