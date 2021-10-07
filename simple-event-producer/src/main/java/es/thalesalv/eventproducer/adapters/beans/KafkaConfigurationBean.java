package es.thalesalv.eventproducer.adapters.beans;

import java.util.HashMap;
import java.util.Map;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import lombok.RequiredArgsConstructor;

@Profile("local")
@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfigurationBean {

    @Value("${app.kafka.schema-registry-url}")
    private String schemaRegistryUrl;

    @Value("${app.kafka.producer.serialization.key-class}")
    private String keySerializerClass;

    @Value("${app.kafka.producer.serialization.value-class}")
    private String valueSerializerClass;

    @Value("${app.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaTemplate<String, GenericRecord> kafkaTemplate() {
        final Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerClass);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass);
        props.put("schema.registry.url", schemaRegistryUrl);

        final ProducerFactory<String, GenericRecord> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<String, GenericRecord>(producerFactory);
    }
}