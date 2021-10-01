package es.thalesalv.streamsconsumer.adapters.event.streams;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.stereotype.Component;

import es.thalesalv.avro.MagazineSchema;
import es.thalesalv.streamsconsumer.domain.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MagazinesTopicListener {

    public void consume(KStream<String, MagazineSchema> stream) {

        log.debug("Started consuming event");
        stream.mapValues(magazine -> {
            try {
                log.info("Event consumed -> {}", magazine);
                return magazine;
            } catch (Exception e) {
                log.error("Error consuming event", e);
                throw new SystemException("Error consuming event.", e);
            }
        });
    }
}
