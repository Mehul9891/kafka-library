package com.kafka.consume.processor;

import org.springframework.kafka.support.Acknowledgment;

public interface IkafkaMessageProcessor<T,V> {

    void messageProcessor(T key,V record);
    void messageProcessor(T key, V record, Acknowledgment ack);
}
