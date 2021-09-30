package es.thalesalv.streamsconprod.adapters.event.mapper;

public interface EventMapper<Input, Output> {

    Output map(Input input);
}
