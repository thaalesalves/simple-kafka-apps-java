package es.thalesalv.eventproducer.domain.enums;

import lombok.Getter;

@Getter
public enum EventTypes {

    BOOK("books", "es.thalesalv.avro.BookSchema"),
    PUBLISHER("books", "es.thalesalv.avro.PublisherSchema"),
    AUTHOR("books", "es.thalesalv.avro.AuthorSchema"),
    MAGAZINE("books", "es.thalesalv.avro.MagazineSchema");

    private String topic;
    private String avroClass;

    private EventTypes(String topic, String avroClass) {
        this.avroClass = avroClass;
        this.topic = topic;
    }
}
