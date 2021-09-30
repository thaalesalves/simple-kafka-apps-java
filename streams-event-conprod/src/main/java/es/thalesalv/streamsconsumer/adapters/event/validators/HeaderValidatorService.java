package es.thalesalv.streamsconsumer.adapters.event.validators;

import org.apache.kafka.common.header.Headers;

public interface HeaderValidatorService {
    void validate(Headers headers);
}
