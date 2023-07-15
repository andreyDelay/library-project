package andrey.library.books.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaClientProperties {

    private String producerTopicName;

    private String consumerTopicName;
}
