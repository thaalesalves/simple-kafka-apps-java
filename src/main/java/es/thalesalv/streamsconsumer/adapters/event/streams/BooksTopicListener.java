package es.thalesalv.streamsconsumer.adapters.event.streams;

import org.apache.kafka.streams.kstream.KStream;
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
public class BooksTopicListener {
    
    @Value("${app.kafka.producer.topics.magazines}")
    private String magazinesTopic;

    private final BookService bookService;

    public void consume(KStream<String, BookSchema> stream) {

        log.debug("Started consuming event");
        stream.mapValues(book -> {
            try {
                log.info("Event consumed -> {}", book);
                return bookService.execute(book);
            } catch (Exception e) {
                log.error("Error consuming event", e);
                throw new SystemException("Error consuming event.", e);
            }
        })
        .peek((k, magazine) -> log.info("Event produced -> {}", magazine))
        .to(magazinesTopic);
    }
}
