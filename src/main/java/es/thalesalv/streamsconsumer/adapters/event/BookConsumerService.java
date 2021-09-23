package es.thalesalv.streamsconsumer.adapters.event;

import org.apache.kafka.streams.StreamsBuilder;

import es.thalesalv.streamsconsumer.domain.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookConsumerService {

    public static void consumeBookEvent(StreamsBuilder builder, String booksTopic) {

        log.debug("Started consuming event");
        builder.stream(booksTopic)
            .mapValues((recordKey, book) -> {
                throw new SystemException("teste");
                // return null;
            });
    }
}
