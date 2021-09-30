package es.thalesalv.streamsconprod.adapters.event.producer;

import org.apache.avro.specific.SpecificRecord;

public interface ProducerService {

    void produce(SpecificRecord record, String topic);
}
