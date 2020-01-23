package com.kafka.consume.processor;

public abstract class KafkaMessageProcessorManualCommit implements IkafkaMessageProcessor {

    public final void messageProcessor(Object key, Object record) {
      // Do Nothing
    }
}
