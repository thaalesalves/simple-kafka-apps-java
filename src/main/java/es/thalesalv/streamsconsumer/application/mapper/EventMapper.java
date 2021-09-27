package es.thalesalv.streamsconsumer.application.mapper;

public interface EventMapper<Input, Output> {

    Output map(Input input);
}
