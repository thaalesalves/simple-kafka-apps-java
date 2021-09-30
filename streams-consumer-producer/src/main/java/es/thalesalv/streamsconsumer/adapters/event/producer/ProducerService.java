package es.thalesalv.streamsconsumer.adapters.event.producer;

import org.apache.avro.specific.SpecificRecord;

import es.thalesalv.avro.MagazineSchema;

public interface ProducerService {

    void produce(MagazineSchema record);
    void produce(SpecificRecord record, String topic);
}
