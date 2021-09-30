package es.thalesalv.eventproducer.adapters.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponsePayload {

    @JsonProperty(value = "original_message")
    private Object originalMessage;

    @JsonProperty(value = "exception_message")
    @JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
    private String exceptionMessage;

    @JsonProperty(value = "exception_name")
    @JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
    private String exceptionName;

    @JsonProperty(value = "status")
    private String status;
}
