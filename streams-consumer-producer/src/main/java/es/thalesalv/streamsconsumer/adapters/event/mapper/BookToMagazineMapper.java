package es.thalesalv.streamsconsumer.adapters.event.mapper;

import org.springframework.stereotype.Component;

import es.thalesalv.avro.BookSchema;
import es.thalesalv.avro.MagazineSchema;

@Component
public class BookToMagazineMapper implements EventMapper<BookSchema, MagazineSchema> {

    @Override
    public MagazineSchema map(BookSchema input) {
        return MagazineSchema.newBuilder()
                .setPublisher(input.getPublisher())
                .setIssue("01-2021")
                .setTitle("A Revistinha da Cidade")
                .build();
    }
}