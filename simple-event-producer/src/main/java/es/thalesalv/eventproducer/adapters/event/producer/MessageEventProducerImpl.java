package es.thalesalv.eventproducer.adapters.event.producer;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageEventProducerImpl implements MessageEventProducer {

    private final KafkaTemplate<String, GenericRecord> kafkaTemplate;

    private ProducerRecord<String, GenericRecord> producerRecord;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageEventProducerImpl.class);

    @Override
    public GenericRecord sendMessage(GenericRecord record, String topic) {
        producerRecord = new ProducerRecord<>(topic, (GenericRecord) record);
        kafkaTemplate.send(producerRecord).addCallback(success -> {
            LOGGER.info("Event produced successfully -> {}", record);
        }, failure -> {
            LOGGER.error("Exception caught while producing event -> {}", failure);
            throw new RuntimeException("Exception caught while producing event.", failure);
        });

        return record;
    }
}
