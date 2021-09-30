package es.thalesalv.eventproducer.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import es.thalesalv.eventproducer.adapters.event.producer.MessageEventProducer;
import es.thalesalv.eventproducer.domain.enums.EventTypes;
import es.thalesalv.eventproducer.domain.exception.AvroNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventProducerServiceImpl implements EventProducerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProducerServiceImpl.class);

    private final ObjectMapper objectMapper;
    private final MessageEventProducer producer;

    @Override
    public GenericRecord transformEvent(String jsonMessage, String eventType) throws Exception {
        try {
            GenericRecord avroEvent = (GenericRecord) objectMapper.readValue(jsonMessage, Class.forName(EventTypes.valueOf(eventType).getAvroClass()));
            return producer.sendMessage(avroEvent, EventTypes.valueOf(eventType).getTopic());
        } catch (IllegalArgumentException | EnumConstantNotPresentException e) {
            LOGGER.error("The JSON payload supplied to the API does not have a matching Avro schema.", e);
            throw new AvroNotFoundException("The JSON payload supplied to the API does not have a matching Avro schema.", e);
        } catch (Exception e) {
            LOGGER.error("Error while transforming JSON into an Avro object", e);
            throw e;
        }
    }
}
