package es.thalesalv.streamsconprod.application.service;

import org.springframework.stereotype.Component;

import es.thalesalv.avro.BookSchema;
import es.thalesalv.avro.MagazineSchema;
import es.thalesalv.streamsconprod.adapters.event.mapper.EventMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final EventMapper<BookSchema, MagazineSchema> bookMagazineMapper;

    @Override
    public MagazineSchema execute(BookSchema book) {
        return bookMagazineMapper.map(book);
    }
}
