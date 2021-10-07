package es.thalesalv.streamsconprod.adapters.beans;

import java.util.HashMap;
import java.util.Map;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import es.thalesalv.streamsconprod.domain.exception.SystemException;
import es.thalesalv.streamsconprod.domain.util.ConfigConstants;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.serializers.subject.TopicRecordNameStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("onprem")
@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfigurationBean {

    @Value("${app.kafka.schema-registry-url}")
    private String schemaRegistryUrl;

    @Value("${app.kafka.streams.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.streams.auto-offset}")
    private String autoOffset;

    @Value("${app.kafka.streams.topics.input.books}")
    private String booksTopic;

    @Value("${app.kafka.producer.serde.value-class}")
    private String valueSerializerClass;

    @Value("${app.kafka.producer.serde.key-class}")
    private String keySerializerClass;

    @Bean
    public Map<String, Object> kafkaProducerConfig() {
        final Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "poc");
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        props.put(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, TopicRecordNameStrategy.class.getName());
        return props;
    }

    @Bean
    public KafkaTemplate<String, GenericRecord> avroKafkaTemplate() {
        try {
            final Map<String, Object> props = kafkaProducerConfig();
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerClass);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass);
            props.put(ConfigConstants.SCHEMA_REGISTRY_URL, schemaRegistryUrl);

            final DefaultKafkaProducerFactory<String, GenericRecord> producerFactory = new DefaultKafkaProducerFactory<>(props);
            return new KafkaTemplate<String, GenericRecord>(producerFactory);
        } catch (Exception e) {
            log.error("Error creating Kafka configuration for Avro producer", e);
            throw new SystemException("Error creating Kafka Producer configuration", e);
        }
    }

    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate() {
        try {
            final DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(kafkaProducerConfig());
            return new KafkaTemplate<String, String>(producerFactory);
        } catch (Exception e) {
            log.error("Error creating Kafka configuration for String producer", e);
            throw new SystemException("Error creating Kafka Producer configuration", e);
        }
    }
}
