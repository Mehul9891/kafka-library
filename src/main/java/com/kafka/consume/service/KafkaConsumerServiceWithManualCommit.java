package com.kafka.consume.service;

import com.kafka.consume.condition.IsConsumerOffsetCommitDisabled;
import com.kafka.consume.processor.IkafkaMessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Conditional(IsConsumerOffsetCommitDisabled.class)
public class KafkaConsumerServiceWithManualCommit {


    @Value("${kafka.topic.name}")
    private String topicName;

    @Value("${enable.auto.commit}")
    private Boolean enableAutoCommit;

    @Autowired
    IkafkaMessageProcessor processor;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group.id}"
            ,containerFactory = "kafkaListenerContainerFactory")
    public void OnMessage(ConsumerRecord record,
                          @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                          @Header(KafkaHeaders.OFFSET) long offset, Acknowledgment ack){

        log.info("Received new Message on "+
                "Topic [ "+ topicName +"]\t" +
                "Key :" + record.key() + "\t" +
                "Value :" + record.partition() + "\t" +
                "Partition Id [ "+ partition +"]\t" +
                "Offset :"+ offset);
        processor.messageProcessor(record.key(), record.value(), ack);
    }

    public String getTopicName() {
        return topicName;
    }
}
