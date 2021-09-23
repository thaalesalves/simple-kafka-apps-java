package es.thalesalv.streamsconsumer.adapters.beans;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.thalesalv.streamsconsumer.adapters.event.BookConsumerService;
import es.thalesalv.streamsconsumer.application.service.ExceptionHandlingService;
import es.thalesalv.streamsconsumer.domain.exception.SystemException;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class KafkaConfigurationBean {

    @Value("${app.kafka.configuration.schema-registry-url}")
    private String schemaRegistryUrl;

    @Value("${app.kafka.configuration.serde.value-class}")
    private String valueSerdeClass;

    @Value("${app.kafka.configuration.serde.key-class}")
    private String keySerdeClass;

    @Value("${app.kafka.configuration.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.configuration.auto-offset}")
    private String autoOffset;

    @Value("${app.kafka.consumer.topics.books}")
    private String booksTopic;

    @Autowired
    private ExceptionHandlingService exceptionHandlingService;

    @Bean
    public KafkaStreams consumerConfig() {
        try {
            log.debug("Setting up Kafka configuration");

            Properties props = new Properties();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "poc");
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Class.forName(valueSerdeClass));
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, new Serdes.StringSerde().getClass());
            props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffset);

            StreamsBuilder builder = new StreamsBuilder();
            BookConsumerService.consumeBookEvent(builder, booksTopic);

            KafkaStreams streams = new KafkaStreams(builder.build(), props);
            streams.setUncaughtExceptionHandler(exceptionHandlingService);

            streams.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Shutting down Kafka listening stream");
                streams.close();
            }));

            log.debug("Finished setting up Kafka configuration");
            return streams;
        } catch (Exception e) {
            log.error("Error creating Kafka configuration", e);
            throw new SystemException("Error creating Kafka configuration", e);
        }
    }
}
