package com.kafka.producer.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class IsKafkaPublisherEnabled implements Condition {
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {

        String required = conditionContext.getEnvironment().getProperty("kafka.publisher.enabled");
        return Boolean.valueOf(required == null ? "false" : required);
    }
}
