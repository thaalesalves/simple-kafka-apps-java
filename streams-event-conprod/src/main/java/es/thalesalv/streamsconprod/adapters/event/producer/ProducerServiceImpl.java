package es.thalesalv.streamsconprod.adapters.event.producer;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import es.thalesalv.avro.MagazineSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

    @Value("${app.kafka.producer.topics.magazines}")
    private String magazinesTopic;

    private final KafkaTemplate<String, SpecificRecord> kafkaProducer;

    @Override
    public void produce(MagazineSchema record) {
        log.debug("Start of event production method");
        ProducerRecord<String, SpecificRecord> producerRecord = new ProducerRecord<>(magazinesTopic, record);
        kafkaProducer.send(producerRecord).addCallback(success -> {
            log.info("Event produced successfully -> {}", record);
        }, failure -> {
            log.error("Exception caught while producing event -> {}", failure);
            throw new RuntimeException("Exception caught while producing event.", failure);
        });
    }

    @Override
    public void produce(SpecificRecord record, String topic) {
        log.debug("Start of event production method");
        ProducerRecord<String, SpecificRecord> producerRecord = new ProducerRecord<>(topic, record);
        kafkaProducer.send(producerRecord).addCallback(success -> {
            log.info("Event produced successfully -> {}", record);
        }, failure -> {
            log.error("Exception caught while producing event -> {}", failure);
            throw new RuntimeException("Exception caught while producing event.", failure);
        });
    }
}
