package es.thalesalv.streamsconprod.adapters.beans;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import es.thalesalv.streamsconprod.domain.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.glue.model.DataFormat;

@Slf4j
@Profile("aws")
@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfigurationBean {

    @Value("${app.aws.region}")
    private String awsRegion;

    @Value("${app.kafka.schema-registry-name}")
    private String schemaRegistryName;

    @Value("${app.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.streams.auto-offset}")
    private String autoOffset;

    @Value("${app.kafka.streams.topics.input.books}")
    private String booksTopic;

    @Value("${app.kafka.producer.serialization.key-class}")
    private String keySerializerClass;

    @Value("${app.kafka.producer.serialization.value-class}")
    private String valueSerializerClass;

    @Bean
    public KafkaTemplate<String, GenericRecord> kafkaTemplate() {
        try {
            Map<String, Object> props = new HashMap<>();
            props.put("auto.register.schemas", false);
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerClass);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass);
            props.put(AWSSchemaRegistryConstants.DATA_FORMAT, DataFormat.AVRO.name());
            props.put(AWSSchemaRegistryConstants.AWS_REGION, awsRegion);
            props.put(AWSSchemaRegistryConstants.REGISTRY_NAME, schemaRegistryName);
            props.put(AWSSchemaRegistryConstants.SCHEMA_AUTO_REGISTRATION_SETTING, false);

            final ProducerFactory<String, GenericRecord> producerFactory = new DefaultKafkaProducerFactory<>(props);
            return new KafkaTemplate<String, GenericRecord>(producerFactory);
        } catch (Exception e) {
            log.error("Error creating Kafka Producer configuration", e);
            throw new SystemException("Error creating Kafka Producer configuration", e);
        }
    }
}
