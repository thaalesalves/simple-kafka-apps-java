package es.thalesalv.streamsconsumer.adapters.event.consumer;

import org.apache.kafka.streams.StreamsBuilder;
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

    private final BookService bookService;

    public void consumeBookEvent(StreamsBuilder builder, String booksTopic) {

        log.debug("Started consuming event");
        builder.stream(booksTopic)
            .mapValues((key, value) -> {
                try {
                    BookSchema book = (BookSchema) value;
                    return bookService.execute(book);
                } catch (Exception e) {
                    log.error("Error consuming event", e);
                    throw new SystemException("Error consuming event.", e);
                }
            });
    }
}
