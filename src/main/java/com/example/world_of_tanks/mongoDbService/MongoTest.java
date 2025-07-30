package com.example.world_of_tanks.mongoDbService;

import com.mongodb.client.MongoDatabase;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;

@Component
public class MongoTest {

    @PostConstruct
    public void testMongoConnection() {
        MongoDatabase db = MongoConnection.getDatabase();
        System.out.println("✔ Свързано с MongoDB база: " + db.getName());
    }
}