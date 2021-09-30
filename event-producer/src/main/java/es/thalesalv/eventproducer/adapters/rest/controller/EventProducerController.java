package es.thalesalv.eventproducer.adapters.rest.controller;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.thalesalv.eventproducer.adapters.rest.model.ResponsePayload;
import es.thalesalv.eventproducer.application.service.EventProducerService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/producer")
@RequiredArgsConstructor
public class EventProducerController {

    private final ObjectMapper objectMapper;
    private final EventProducerService eventProducerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProducerController.class);

    @PostMapping("/events")
    public Mono<Object> produceEvent(@RequestBody String body) {
        return Mono.just(body).map(bodyJson -> {

            LOGGER.info("Request received from API. Converting data into an Avro object -> {}", body);
            Object originalMessage = null;
            try {
                final Map<String, String> payloadProps = (Map<String, String>) objectMapper.readValue(bodyJson, Map.class);
                originalMessage = objectMapper.readValue(stringifyJson(payloadProps.get("message")), Object.class);
                eventProducerService.transformEvent(stringifyJson(originalMessage), payloadProps.get("event_type"));
                return ResponsePayload.builder()
                        .originalMessage(originalMessage)
                        .status("SUCCESS")
                        .build();
            } catch (Exception e) {
                LOGGER.error("Error while processing request.", e);
                return ResponsePayload.builder()
                        .exceptionMessage(e.getMessage())
                        .exceptionName(e.getCause().getClass().getName())
                        .originalMessage(originalMessage)
                        .status("ERROR")
                        .build();
            }
        });
    }

    private String stringifyJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while parsing JSON object.", e);
            throw new RuntimeException("Error while parsing JSON object.", e);
        }
    }
}
