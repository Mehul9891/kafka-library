package com.kafka.consume.processor;

import org.springframework.kafka.support.Acknowledgment;

public  abstract class KafkaMessageProcessorAutoCommit<T,V> implements IkafkaMessageProcessor {

    public final void messageProcessor(Object key, Object record, Acknowledgment ack) {
        //do Nothing
    }
}
