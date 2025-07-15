package com.example.world_of_tanks.configuration;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Properties;

@Configuration
public class ApacheKafkaConfig {

    private static final String BOOTSTRAP_SERVERS = "192.168.2.119:9092";
    private static final String TOPIC_NAME = "tank-events";

    @Bean
    public AdminClient adminClient() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        return AdminClient.create(props);
    }

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        return new KafkaProducer<>(props);
    }

    @Bean
    public CommandLineRunner topicCreator(AdminClient adminClient) {
        return args -> {
            try {
                adminClient.createTopics(
                        Collections.singleton(new org.apache.kafka.clients.admin.NewTopic(TOPIC_NAME, 1, (short) 1))
                ).all().get();
                System.out.println("Topic '" + TOPIC_NAME + "' is ready.");
            } catch (Exception e) {
                if (!(e.getCause() instanceof org.apache.kafka.common.errors.TopicExistsException)) {
                    throw new RuntimeException("Неуспешно създаване на тема " + TOPIC_NAME, e);
                }
            }
        };
    }
}