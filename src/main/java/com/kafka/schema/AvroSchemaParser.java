package com.kafka.schema;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

public class AvroSchemaParser {

    public static GenericRecord createGenericRecord(String schema){

        Schema.Parser parser = new Schema.Parser();
        Schema schemaObj = parser.parse(schema);
        GenericRecord avroRec = new  GenericData.Record(schemaObj);
        return  avroRec;
    }
}
