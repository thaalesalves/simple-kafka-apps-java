package es.thalesalv.eventproducer.application.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.stereotype.Component;

@Component
public class EventProducerErrorHandler implements ErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProducerErrorHandler.class);

    @Override
    public void handle(Exception thrownException, ConsumerRecord<?, ?> consumerRecord) {
        LOGGER.error("Exception arrived in error handler. Event -> {}", consumerRecord.value(), thrownException);
    }
}

