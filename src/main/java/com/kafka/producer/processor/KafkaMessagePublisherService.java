package com.kafka.producer.processor;

import com.kafka.producer.condition.IsKafkaPublisherEnabled;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.Future;

@Slf4j
@Service
@Conditional(IsKafkaPublisherEnabled.class)
public class KafkaMessagePublisherService implements IKafkaMessagePublisher<Object> {

    @Value("${kafka.topic.name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    public KafkaMessagePublisherService() {

        log.debug("Initialization of KafkaMessagePublisherService completed.");
    }

    public Future<RecordMetadata> publishMessage(final String key, final Object record) {

        try{
            if(record == null){
                throw new RuntimeException("Invalid value passed as record to the publisher");
            }

            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, key, record);

            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

                public void onSuccess(SendResult<String, Object> result) {
                    log.info("Message published successfully with Offset [{}] , key [[]] on topic [{}]"
                            , result.getRecordMetadata().offset(), key, topicName);
                }
                public void onFailure(Throwable ex) {
                    log.error("Unable to send message with key [{}] on topic [{}] due to :"
                            , key, topicName, ex.getMessage());
                }
            });
        }catch (Exception e){
            log.error("error while processing message {}", record, e);
        }

        return null;
    }

    public String getTopicName() {
        return topicName;
    }
}
