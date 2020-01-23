package com.kafka.config;

import com.kafka.consume.condition.IsKafkaConsumerEnabled;
import com.kafka.producer.condition.IsKafkaPublisherEnabled;
import com.sun.xml.internal.ws.api.FeatureListValidatorAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Properties;

@EnableKafka
@Slf4j
@Configuration
@ComponentScan("com.kafka")
public class KafkaConfiguration {

    @Value("${kafka.bootstrap.server.url}")
    private String kafkaBootstapServer;

    @Value("${kafka.schema.registry.url}")
    private String schemaRegistryUrl;

    @Value("${kafka.ack.config}")
    private String ackConfig;

    @Value("${kafka.retries.config}")
    private String retriesConfig;

    @Value("${kafka.linger.ms}")
    private String lingerTime;

    @Value("${kafka.batch.size}")
    private String batchSize;

    @Value("${kafka.ssl.enabled:false}")
    private Boolean isSSLEnabled;

    @Value("${kafka.keystore.config.path:#{null}}")
    private String keystoreConfigPath;

    @Value("${kafka.keystore.config.password:#{null}}")
    private String keystoreConfigPassword;

    @Value("${kafka.truststore.config.path:#{null}}")
    private String truststoreConfigPath;

    @Value("${kafka.truststore.config.password:#{null}}")
    private String truststoreConfigPassword;

    @Value("${enable.auto.commit:true}")
    private Boolean enableAutoCommit;

    @Value("${auto.commit.interval.ms}")
    private String autoCommitInterval;

    @Value("${kafka.consumer.concurrency:1}")
    private int concurrency;

    @Value("${kafka.producer.key.serializer:}")
    private String keySerializer;

    @Value("${kafka.producer.value.serializer:}")
    private String valueSerializer;

    @Value("${kafka.consumer.key.deserializer:#{null}}")
    private String keyDeserializer;

    @Value("${kafka.consumer.value.deserializer:#{null}}")
    private String valueDeserializer;

    public KafkaConfiguration(){
        log.debug("Initialization of KafkaConfiguration completed");
    }

    @Bean
    @Conditional(IsKafkaPublisherEnabled.class)
    public Producer<String,GenericRecord> producer() throws ClassNotFoundException{
        log.debug("Producer Initialization completed");
        return new KafkaProducer<String, GenericRecord>(getProducerProperties());
    }

    @Bean
    @Conditional(IsKafkaPublisherEnabled.class)
    public ProducerFactory<String, Object> producerFactory() throws ClassNotFoundException{

        log.debug("Initialization Completed for ProducerFactory");
        return new DefaultKafkaProducerFactory(getProducerProperties());
    }

    @Bean
    @Conditional(IsKafkaPublisherEnabled.class)
    public KafkaTemplate<String, Object> kafkaTemplate() throws  ClassNotFoundException{

        log.debug("Initialization Completed for KafkaTemplate");
        return  new KafkaTemplate(producerFactory());
    }


    @Bean
    @Conditional(IsKafkaConsumerEnabled.class)
    public ConsumerFactory<String, GenericRecord> consumerFactory() throws ClassNotFoundException{
        log.debug("Initialization of ConsumerFactory completed");
        return  new DefaultKafkaConsumerFactory(getConsumerProperties());
    }

    @Bean
    @Conditional(IsKafkaConsumerEnabled.class)
    public ConcurrentKafkaListenerContainerFactory<String, GenericRecord> kafkaListenerContainerFactory() throws Exception{

        ConcurrentKafkaListenerContainerFactory<String,GenericRecord> factory=
                new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency);
        if(!enableAutoCommit){
            factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        }
        return  factory;
    }

    private Properties getProducerProperties() throws ClassNotFoundException{

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstapServer);
        //properties.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,schemaRegistryUrl);
        properties.put(ProducerConfig.RETRIES_CONFIG, retriesConfig);
        properties.put(ProducerConfig.ACKS_CONFIG, ackConfig);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, lingerTime);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Class.forName(keySerializer));
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Class.forName(valueSerializer));

        if(isSSLEnabled){
            properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SSL");
            properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keystoreConfigPath);
            properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keystoreConfigPassword);
            properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreConfigPath);
            properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststoreConfigPassword);
        }

        return properties;
    }


    private Properties getConsumerProperties() throws ClassNotFoundException{

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstapServer);
        //properties.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,schemaRegistryUrl);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Class.forName(keyDeserializer));
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Class.forName(valueDeserializer));

        if(isSSLEnabled){
            properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SSL");
            properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keystoreConfigPath);
            properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keystoreConfigPassword);
            properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreConfigPath);
            properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststoreConfigPassword);
        }

        return properties;
    }

}
