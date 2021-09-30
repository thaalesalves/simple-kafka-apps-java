package es.thalesalv.streamsconsumer.adapters.event.mapper;

public interface EventMapper<Input, Output> {

    Output map(Input input);
}
