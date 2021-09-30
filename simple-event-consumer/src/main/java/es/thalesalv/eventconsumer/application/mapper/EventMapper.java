package es.thalesalv.eventconsumer.application.mapper;

public interface EventMapper<A, B> {

    B map(A input);
}
