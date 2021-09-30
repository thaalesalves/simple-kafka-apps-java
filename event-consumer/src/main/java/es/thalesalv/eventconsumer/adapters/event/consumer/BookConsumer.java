package es.thalesalv.eventconsumer.adapters.event.consumer;

import java.util.Map;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import es.thalesalv.avro.BookSchema;
import es.thalesalv.avro.MagazineSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(groupId = "basic-consumer", topics = "${app.config.messaging.consumer.topic}", containerFactory = "kafkaListenerContainerFactory")
public class BookConsumer {

    @KafkaHandler
    public void listen(BookSchema messageBody, @Headers Map<String, Object> messageHeaders, Acknowledgment ack) {
        try {
            log.info("Event consumed. Body -> {}. Headers -> {}", messageBody, messageHeaders);
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaHandler
    public void listen(MagazineSchema messageBody, @Headers Map<String, Object> messageHeaders, Acknowledgment ack) {
        try {
            log.info("Event consumed. Body -> {}. Headers -> {}", messageBody, messageHeaders);
        } finally {
            ack.acknowledge();
        }
    }
}
