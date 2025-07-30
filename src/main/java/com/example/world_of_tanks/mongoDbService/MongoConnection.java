package com.example.world_of_tanks.mongoDbService;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

    private static final String CONNECTION_STRING = "mongodb://192.168.2.109:27017";
    private static final String DATABASE_NAME = "world-of-tanks";

    private static final MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);

    public static MongoDatabase getDatabase() {
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    public static void close() {
        mongoClient.close();
    }
}

