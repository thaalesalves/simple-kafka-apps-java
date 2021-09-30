package es.thalesalv.streamsconprod.application.service;

import es.thalesalv.avro.BookSchema;
import es.thalesalv.avro.MagazineSchema;

public interface BookService {

    MagazineSchema execute(BookSchema book);
}
