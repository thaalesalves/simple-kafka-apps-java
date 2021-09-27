package es.thalesalv.streamsconsumer.adapters.event.producer;

import org.apache.avro.specific.SpecificRecord;

public interface ProducerService {

    void produce(SpecificRecord record);
}
