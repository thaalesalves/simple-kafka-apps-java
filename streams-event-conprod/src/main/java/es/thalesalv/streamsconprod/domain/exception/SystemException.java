package es.thalesalv.streamsconprod.domain.exception;

public class SystemException extends RuntimeException {

    public SystemException(String msg, Throwable t) {
        super(msg, t);
    }

    public SystemException(Throwable t) {
        super(t);
    }

    public SystemException(String msg) {
        super(msg);
    }
}
