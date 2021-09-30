package es.thalesalv.streamsconprod.adapters.beans;

import java.util.HashMap;
import java.util.Map;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import es.thalesalv.streamsconprod.domain.exception.SystemException;
import es.thalesalv.streamsconprod.domain.util.ConfigConstants;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.serializers.subject.TopicRecordNameStrategy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class KafkaProducerConfigurationBean {

    @Value("${app.kafka.producer.schema-registry-url}")
    private String schemaRegistryUrl;

    @Value("${app.kafka.producer.serde.value-class}")
    private String valueSerdeClass;

    @Value("${app.kafka.producer.serde.key-class}")
    private String keySerdeClass;

    @Value("${app.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> kafkaProducerConfig() {
        log.debug("Setting up Kafka consumer configuration");
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "poc");
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        props.put(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, TopicRecordNameStrategy.class.getName());

        log.debug("Finished setting up Kafka consumer configuration");
        return props;
    }

    @Bean
    public KafkaTemplate<String, SpecificRecord> parametrizedProducerConfig() {
        try {
            log.debug("Setting up KafkaProducer for parametrized messages");
            Map<String, Object> props = kafkaProducerConfig();
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerdeClass);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerdeClass);
            props.put(ConfigConstants.SCHEMA_REGISTRY_URL, schemaRegistryUrl);

            log.debug("Finished setting up Kafka consumer configuration");
            DefaultKafkaProducerFactory<String, SpecificRecord> producerFactory = new DefaultKafkaProducerFactory<String, SpecificRecord>(props);
            return new KafkaTemplate<String, SpecificRecord>(producerFactory);
        } catch (Exception e) {
            log.error("Error creating Kafka configuration for Avro producer", e);
            throw new SystemException("Error creating Kafka configuration", e);
        }
    }

    @Bean
    public KafkaTemplate<String, String> stringProducerConfig() {
        try {
            log.debug("Setting up KafkaProducer for string messages");
            DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<String, String>(kafkaProducerConfig());
            return new KafkaTemplate<String, String>(producerFactory);
        } catch (Exception e) {
            log.error("Error creating Kafka configuration", e);
            throw new SystemException("Error creating Kafka configuration for String producer", e);
        }
    }
}
