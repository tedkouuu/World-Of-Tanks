package com.example.world_of_tanks.mongoDbService;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.concurrent.TimeUnit;

public class MongoConnection {

    private static final String CONNECTION_STRING =
            "mongodb://root:1234@wot-mongo:27017/world-of-tanks?authSource=admin";

    private static final String DATABASE_NAME = "world-of-tanks";

    private static final MongoClient mongoClient;

    static {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                .applyToClusterSettings(builder ->
                        builder.serverSelectionTimeout(2, TimeUnit.SECONDS))
                .applyToSocketSettings(builder -> {
                    builder.connectTimeout(2, TimeUnit.SECONDS);
                    builder.readTimeout(2, TimeUnit.SECONDS);
                })
                .build();
        mongoClient = MongoClients.create(settings);
    }

    public static MongoDatabase getDatabase() {
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    public static void close() {
        mongoClient.close();
    }
}
