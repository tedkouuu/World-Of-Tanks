package com.example.world_of_tanks.services;

import com.example.world_of_tanks.models.dto.TankEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Service
public class ApacheKafkaProducerService {

    private static final String TOPIC = "tank-events";
    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;

    public ApacheKafkaProducerService(KafkaProducer<String, String> producer) {
        this.producer = producer;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void sendTankEvent(TankEventDTO event) {
        String key = event.getTankName();

        try {
            String json = objectMapper.writeValueAsString(event);

            producer.send(
                    new ProducerRecord<>(TOPIC, key, json),
                    (metadata, ex) -> {
                        if (ex != null) {
                            System.err.printf("❌ Error sending message to topic %s: %s%n", TOPIC, ex.getMessage());
                        } else {
                            System.out.printf("✅ Sent record to topic=%s partition=%d offset=%d key=%s%n",
                                    metadata.topic(), metadata.partition(), metadata.offset(), key);
                        }
                    }
            );
        } catch (Exception e) {
            System.err.printf("❌ Kafka unavailable or serialization failed for tank: %s — %s%n", key, e.getMessage());
        }
    }
}
