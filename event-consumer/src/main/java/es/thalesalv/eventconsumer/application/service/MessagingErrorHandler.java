package es.thalesalv.eventconsumer.application.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessagingErrorHandler implements ErrorHandler {

    @Override
    public void handle(Exception thrownException, ConsumerRecord<?, ?> consumerRecord) {
        log.error("Exception arrived in error handler. Event -> {}", consumerRecord.value(), thrownException);
    }
}
