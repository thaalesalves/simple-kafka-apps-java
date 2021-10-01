package es.thalesalv.eventproducer.domain.enums;

import lombok.Getter;

@Getter
public enum EventTypes {

    BOOK("books", "es.thalesalv.avro.BookSchema"),
    MAGAZINE("magazines", "es.thalesalv.avro.MagazineSchema");

    private String topic;
    private String avroClass;

    private EventTypes(String topic, String avroClass) {
        this.avroClass = avroClass;
        this.topic = topic;
    }
}
