package es.thalesalv.streamsconprod.domain.exception;

public class InvalidHeaderException extends RuntimeException {

    public InvalidHeaderException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidHeaderException(Throwable t) {
        super(t);
    }

    public InvalidHeaderException(String msg) {
        super(msg);
    } 
}
