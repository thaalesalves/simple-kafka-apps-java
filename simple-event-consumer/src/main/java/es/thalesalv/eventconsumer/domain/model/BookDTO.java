package es.thalesalv.eventconsumer.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BookDTO {

    private String title;
    private String author;
    private int releaseYear;
    private String genre;
    private String publisher;
    private long isbn;
}
