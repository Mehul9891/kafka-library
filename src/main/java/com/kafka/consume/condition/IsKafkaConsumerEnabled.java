package com.kafka.consume.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class IsKafkaConsumerEnabled implements Condition {
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String required = conditionContext.getEnvironment().getProperty("kafka.consumer.enable");
        return Boolean.valueOf(required == null ? "false" : required);
    }
}
