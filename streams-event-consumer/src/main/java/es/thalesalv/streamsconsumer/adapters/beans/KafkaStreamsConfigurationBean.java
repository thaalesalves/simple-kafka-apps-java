package es.thalesalv.streamsconsumer.adapters.beans;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.thalesalv.streamsconsumer.adapters.event.streams.MagazinesTopicListener;
import es.thalesalv.streamsconsumer.application.service.ExceptionHandlingService;
import es.thalesalv.streamsconsumer.domain.exception.SystemException;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaStreamsConfigurationBean {

    @Value("${app.kafka.streams.schema-registry-url}")
    private String schemaRegistryUrl;

    @Value("${app.kafka.streams.serde.value-class}")
    private String valueSerdeClass;

    @Value("${app.kafka.streams.serde.key-class}")
    private String keySerdeClass;

    @Value("${app.kafka.streams.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.streams.auto-offset}")
    private String autoOffset;

    @Value("${app.kafka.streams.topics.input.magazines}")
    private String magazinesTopic;

    private final MagazinesTopicListener magazinesConsumerService;
    private final ExceptionHandlingService exceptionHandlingService;

    @Bean
    public KafkaStreams streamsConfig() {
        try {
            log.debug("Setting up Kafka consumer configuration");

            Properties props = new Properties();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-consumer-poc");
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Class.forName(valueSerdeClass));
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Class.forName(keySerdeClass));
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(StreamsConfig.producerPrefix(ProducerConfig.ACKS_CONFIG), "all");
            props.put(StreamsConfig.consumerPrefix(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG), autoOffset);
            props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

            StreamsBuilder builder = new StreamsBuilder();
            magazinesConsumerService.consume(builder.stream(magazinesTopic));

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
