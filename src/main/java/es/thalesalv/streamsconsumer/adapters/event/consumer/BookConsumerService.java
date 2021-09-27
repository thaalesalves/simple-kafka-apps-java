package es.thalesalv.streamsconsumer.adapters.event.consumer;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.thalesalv.avro.BookSchema;
import es.thalesalv.streamsconsumer.application.service.BookService;
import es.thalesalv.streamsconsumer.domain.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookConsumerService {

    @Value("${app.kafka.producer.topics.magazines}")
    private String magazinesTopic;

    private final BookService bookService;

    public void consumeBookEvent(StreamsBuilder builder, String booksTopic) {

        log.debug("Started consuming event");
        builder.stream(booksTopic)
            .mapValues((key, value) -> {
                try {
                    return bookService.execute((BookSchema) value);
                } catch (Exception e) {
                    log.error("Error consuming event", e);
                    throw new SystemException("Error consuming event.", e);
                }
            })
            .map((key, value) -> new KeyValue<>(key, value))
            .to(magazinesTopic);
    }
}
