package es.thalesalv.streamsconsumer.adapters.beans;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.thalesalv.streamsconsumer.application.service.ExceptionHandlingService;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfigurationBean {

    @Value("${app.kafka.configuration.schema-registry-url}")
    private String schemaRegistryUrl;

    @Value("${app.kafka.configuration.serde.value-serde-class}")
    private String valueSerdeClass;

    @Value("${app.kafka.configuration.serde.key-serde-class}")
    private String keySerdeClass;

    @Value("${app.kafka.configuration.boostrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.configuration.auto-offset}")
    private String autoOffset;

    private final ExceptionHandlingService exceptionHandlingService;

    @Bean
    public StreamsBuilder streamsBuilder() {
        return new StreamsBuilder();
    }

    @Bean
    public KafkaStreams producerConfig() {
        log.debug("Setting up Kafka configuration");

        Properties props = new Properties();
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, valueSerdeClass);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, keySerdeClass);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffset);

        KafkaStreams streams = new KafkaStreams(streamsBuilder().build(), props);
        streams.setUncaughtExceptionHandler(exceptionHandlingService);
        log.debug("Finished setting up Kafka configuration");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down stream");
            streams.close();
        }));

        return streams;
    }
}
