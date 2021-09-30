package es.thalesalv.eventproducer.adapters.event.producer;

import org.apache.avro.generic.GenericRecord;

public interface MessageEventProducer {

    GenericRecord sendMessage(GenericRecord record, String topic);
}
