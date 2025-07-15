// ApacheKafkaProducerService.java
package com.example.world_of_tanks.services;

import com.example.world_of_tanks.models.dto.TankEventDTO;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Service
public class ApacheKafkaProducerService {

    private static final String TOPIC = "tank-events";
    private final KafkaProducer<String, String> producer;

    public ApacheKafkaProducerService(KafkaProducer<String, String> producer) {
        this.producer = producer;
    }

    public void sendTankEvent(TankEventDTO event) {
        String key  = event.getTankName();
        String json = String.format(
                "{\"tankName\":\"%s\",\"action\":\"%s\"}",
                key, event.getAction()
        );

        producer.send(
                new ProducerRecord<>(TOPIC, key, json),
                (metadata, ex) -> {
                    if (ex != null) {
                        System.err.printf(
                                "❌ Error sending message to topic %s: %s%n",
                                TOPIC, ex.getMessage()
                        );
                        ex.printStackTrace();
                    } else {
                        System.out.printf(
                                "✅ Sent record to topic=%s partition=%d offset=%d key=%s%n",
                                metadata.topic(),
                                metadata.partition(),
                                metadata.offset(),
                                key
                        );
                    }
                }
        );
    }
}
