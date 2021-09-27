package es.thalesalv.streamsconsumer.adapters.event.producer;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
    public void produce(SpecificRecord record) {
        log.debug("Start of event production method");
        ProducerRecord<String, SpecificRecord> producerRecord = new ProducerRecord<>(magazinesTopic, record);
        kafkaProducer.send(producerRecord).addCallback(success -> {
            log.info("Event produced successfully -> {}", record);
        }, failure -> {
            log.error("Exception caught while producing event -> {}", failure);
            throw new RuntimeException("Exception caught while producing event.", failure);
        });
    }
}
