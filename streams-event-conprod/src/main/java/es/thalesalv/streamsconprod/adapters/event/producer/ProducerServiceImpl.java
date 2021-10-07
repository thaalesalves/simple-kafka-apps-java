package es.thalesalv.streamsconprod.adapters.event.producer;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
 
@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

    private final KafkaTemplate<String, GenericRecord> kafkaTemplate;

    @Override
    public void produce(SpecificRecord record, String topic) {

        log.debug("Start of event production method");
        ProducerRecord<String, GenericRecord> producerRecord = new ProducerRecord<String, GenericRecord>(topic, (GenericRecord) GenericData.get().newRecord(record, record.getSchema()));
        kafkaTemplate.send(producerRecord).addCallback(success -> {
            log.info("Event produced successfully -> {}", record);
        }, failure -> {
            log.error("Exception caught while producing event -> {}", failure);
            throw new RuntimeException("Exception caught while producing event.", failure);
        });
    }
}
