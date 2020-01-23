# Kafka Library  

The goal of this library is to quick and easy set of kafka service.

## Getting started

Please follow below steps:

1. Include the artifact as dependency:

       <dependency>
        <groupId>com.coderview</groupId>
        <artifactId>kafka-library</artifactId>
        <version>1.0-SNAPSHOT</version>
       </dependency>

2. Define application.properties in src/main/resources folder. Please provide values accordingly:

    ###### Generic properties

    kafka.bootstrap.server.url=
    kafka.schema.registry.url=
    kafka.ack.config=
    kafka.retries.config=
    kafka.linger.ms=
    kafka.batch.size=
    kafka.offset.config=
    kafka.topic.name=
    kafka.group.id=
    kafka.ssl.enabled=

    ###### Enable Producer

    kafka.publisher.enable=
    kafka.producer.key.serializer=
    kafka.producer.value.serializer=

    ##### Enable Consumer

    kafka.consumer.enable=
    kafka.consumer.key.deserializer=
    kafka.consumer.value.deserializer=
    enable.auto.commit=
    auto.commit.interval.ms=
    kafka.consumer.concurrency=   // No of Concurrent instance of Consumer , by default is 1


    ##### SSL Properties to enable kafka

    kafka.keystore.config.path=
    kafka.keystore.config.password=
    kafka.truststore.config.path=
    kafka.truststore.config.password=

3. enable Kafka Consumer
    a. set kafka.consumer.enable=true to enable kafka consumer.
    b. Provide implementation class of 'KafkaMessageProcessor' interface.
        This will consist of processing logic for the message consumed by consumer.
        * Implement 'KafkaMessageProcessorAutoCommit' Class if 'enable.auto.commit=true' it means offset will be automatically commited.
        else implement 'KafkaMessageProcessorManualCommit' Class to manually acknowledge the offset using Acknowledge.acknowledge().

4. enable Kafka Publisher:
    a. set kafka.publisher.enable=true to enable kafka publisher.
    b. Call 'KafkaMessagePublisherService.publishMessage(String key, GenericRecord record)' method.
       Publisher will be configured in the background and will publish the message on topic as per configuration.

