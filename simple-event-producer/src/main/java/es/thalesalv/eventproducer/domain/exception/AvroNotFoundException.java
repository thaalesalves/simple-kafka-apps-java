package es.thalesalv.eventproducer.domain.exception;

public class AvroNotFoundException extends RuntimeException {

    public AvroNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public AvroNotFoundException(String msg) {
        super(msg);
    }
}
