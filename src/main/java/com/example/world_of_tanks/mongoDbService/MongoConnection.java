package com.example.world_of_tanks.mongoDbService;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

    private static final String CONNECTION_STRING =
            "mongodb://teo:12345678@wot-mongo:27017/?authSource=admin";

    private static final String DATABASE_NAME = "world-of-tanks";

    private static final MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);

    public static MongoDatabase getDatabase() {
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    public static void close() {
        mongoClient.close();
    }
}

