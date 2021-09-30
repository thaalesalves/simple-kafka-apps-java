package es.thalesalv.streamsconsumer.adapters.event.validators;

import java.util.List;

import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeaderValidatorServiceImpl implements HeaderValidatorService {

    @Value("#{'${app.kafka.required-headers:authentication,correlation-id,source}'.split(',')}")
    private List<String> requiredHeaders;

    @Override
    public void validate(Headers headers) {
        log.debug("Started event header validation");
    }
}
