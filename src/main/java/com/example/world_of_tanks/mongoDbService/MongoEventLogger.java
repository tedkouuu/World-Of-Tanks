package com.example.world_of_tanks.mongoDbService;

import com.example.world_of_tanks.models.dto.TankEventLog;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoEventLogger {

    private final MongoCollection<Document> collection;

    public MongoEventLogger() {
        MongoDatabase db = MongoConnection.getDatabase();
        this.collection = db.getCollection("tank_event_logs");
    }

    public void logEvent(TankEventLog event) {
        Document doc = new Document()
                .append("tankId", event.getTankId())
                .append("eventType", event.getEventType())
                .append("timestamp", event.getTimestamp().toString())
                .append("payload", new Document(event.getPayload()));

        collection.insertOne(doc);
        System.out.println(" Log added for tank ID: " + event.getTankId());
    }
}
