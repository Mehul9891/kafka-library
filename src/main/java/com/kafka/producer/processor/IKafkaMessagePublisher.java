package com.kafka.producer.processor;

import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.Future;

public interface IKafkaMessagePublisher<T> {

    Future<RecordMetadata> publishMessage(String key, T t);

    String getTopicName();

}
