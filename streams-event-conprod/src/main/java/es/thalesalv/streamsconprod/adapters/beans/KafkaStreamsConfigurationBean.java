package es.thalesalv.streamsconprod.adapters.beans;

import java.util.Properties;

import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import es.thalesalv.streamsconprod.adapters.event.streams.BooksTopicListener;
import es.thalesalv.streamsconprod.application.service.ExceptionHandlingService;
import es.thalesalv.streamsconprod.domain.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.glue.model.DataFormat;

@Slf4j
@Profile("aws")
@Configuration
@RequiredArgsConstructor
public class KafkaStreamsConfigurationBean {

    @Value("${app.aws.region}")
    private String awsRegion;

    @Value("${app.kafka.schema-registry-name}")
    private String schemaRegistryName;

    @Value("${app.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.streams.serde.value-class}")
    private String valueSerdeClass;

    @Value("${app.kafka.streams.serde.key-class}")
    private String keySerdeClass;

    @Value("${app.kafka.streams.auto-offset}")
    private String autoOffset;

    @Value("${app.kafka.streams.topics.input.books}")
    private String booksTopic;

    private final BooksTopicListener bookConsumerService;
    private final ExceptionHandlingService exceptionHandlingService;

    @Bean
    public KafkaStreams kafkaStreams() {
        try {
            Properties props = new Properties();
            props.put("auto.register.schemas", true);
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-conprod-poc");
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Class.forName(valueSerdeClass));
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Class.forName(keySerdeClass));
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(StreamsConfig.producerPrefix(ProducerConfig.ACKS_CONFIG), "all");
            props.put(StreamsConfig.consumerPrefix(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG), autoOffset);
            props.put(AWSSchemaRegistryConstants.DATA_FORMAT, DataFormat.AVRO.name());
            props.put(AWSSchemaRegistryConstants.AWS_REGION, awsRegion);
            props.put(AWSSchemaRegistryConstants.REGISTRY_NAME, schemaRegistryName);
            props.put(AWSSchemaRegistryConstants.SCHEMA_AUTO_REGISTRATION_SETTING, true);

            StreamsBuilder builder = new StreamsBuilder();
            bookConsumerService.consume(builder.stream(booksTopic));

            KafkaStreams streams = new KafkaStreams(builder.build(), props);
            streams.setUncaughtExceptionHandler(exceptionHandlingService);

            streams.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Shutting down Kafka listening stream");
                streams.close();
            }));

            return streams;
        } catch (Exception e) {
            log.error("Error creating Kafka Streams configuration", e);
            throw new SystemException("Error creating Kafka Streams configuration", e);
        }
    }
}
