package es.thalesalv.streamsconprod.adapters.event.transformer;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;

public class SpecificAvroToGenericAvroTransformer implements ValueTransformer<SpecificRecord, GenericRecord> {

    private ProcessorContext processorContext;

    @Override
    public void init(ProcessorContext context) {
        this.processorContext = context;
    }

    @Override
    public GenericRecord transform(SpecificRecord value) {
        return new GenericData.Record(value.getSchema());
    }

    @Override
    public void close() {
    }
}
