package com.kafka.consume.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class IsConsumerOffsetCommitDisabled implements Condition {
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {

        String required = conditionContext.getEnvironment().getProperty("kafka.consumer.enable");

        if( Boolean.valueOf(required == null ? "false" : required)){
            String autoEnable = conditionContext.getEnvironment().getProperty("enable.auto.commit");
            return !(Boolean.valueOf(autoEnable == null ? "true": autoEnable));
        }
        else {
            return false;
        }
    }
}
