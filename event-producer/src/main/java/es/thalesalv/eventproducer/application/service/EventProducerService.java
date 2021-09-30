package es.thalesalv.eventproducer.application.service;

import org.apache.avro.generic.GenericRecord;

public interface EventProducerService {

    GenericRecord transformEvent(String jsonMessage, String eventType) throws Exception;
}
