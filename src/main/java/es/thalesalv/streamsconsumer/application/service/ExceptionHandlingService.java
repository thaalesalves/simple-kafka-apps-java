package es.thalesalv.streamsconsumer.application.service;

import org.apache.kafka.streams.errors.StreamsException;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlingService implements StreamsUncaughtExceptionHandler {

    @Override
    public StreamThreadExceptionResponse handle(Throwable exception) {
        log.debug("Exception arrived in error handler -> {}", exception);
        if (exception.getCause() instanceof StreamsException) {
            log.error("There was an error when consuming the stream event.", exception);
        }
        return null;
    }
}
