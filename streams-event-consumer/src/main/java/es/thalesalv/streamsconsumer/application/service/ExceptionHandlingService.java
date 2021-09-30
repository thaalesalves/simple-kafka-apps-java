package es.thalesalv.streamsconsumer.application.service;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.streams.errors.StreamsException;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

// import es.thalesalv.avro.ErrorSchema;
// import es.thalesalv.streamsconsumer.adapters.event.producer.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlingService implements StreamsUncaughtExceptionHandler {

    // private final ProducerService producerService;

    @Override
    public StreamThreadExceptionResponse handle(Throwable exception) {

        log.debug("Exception arrived in error handler -> {}", exception);
        if (exception instanceof StreamsException) {
            log.error("There was an error when consuming the stream event.", exception);
        } else if (exception instanceof SerializationException) {
            log.error("Error serializing message", exception);
        }

        // producerService.produce(buildErrorObject(exception.getCause().getClass().getName(), exception.getCause().getMessage(), 500), "dead-letter");
        return StreamThreadExceptionResponse.REPLACE_THREAD;
    }

    // private ErrorSchema buildErrorObject(String exceptionClassName, String stacktrace, int statusCode) {
    //     return ErrorSchema.newBuilder()
    //             .setErrorMessage(stacktrace)
    //             .setErrorName(exceptionClassName)
    //             .setStatusCode(statusCode)
    //             .build();
    // }
}
