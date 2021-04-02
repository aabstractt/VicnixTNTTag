package net.vicnix.tnttag.provider;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.vicnix.tnttag.TNTTag;
import net.vicnix.tnttag.session.SessionStorage;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class MongoDBProvider {

    private final static MongoDBProvider instance = new MongoDBProvider();

    private MongoCollection<Document> collection;

    public static MongoDBProvider getInstance() {
        return instance;
    }

    public void init() {
        try {
            FileConfiguration config = TNTTag.getInstance().getConfig();

            MongoClient mongoClient = new MongoClient(new MongoClientURI(config.getString("mongouri")));

            MongoDatabase database = mongoClient.getDatabase("TNTTag");

            this.collection = database.getCollection("user_stats");
        } catch (Exception e) {
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public void saveSessionStorage(SessionStorage sessionStorage) {
        if (this.collection == null) {
            Bukkit.getLogger().warning("MongoDB not initialized...");

            return;
        }

        Document document = this.collection.find(Filters.eq("uuid", sessionStorage.getUniqueId().toString())).first();

        Document newDocument = new Document("uuid", sessionStorage.getUniqueId().toString())
                .append("name", sessionStorage.getName())
                .append("gamesPlayed", sessionStorage.getGamesPlayed())
                .append("wins", sessionStorage.getWins())
                .append("currentWinStreak", sessionStorage.getCurrentWinStreak());

        if (document == null || document.isEmpty()) {
            this.collection.insertOne(newDocument);
        } else {
            this.collection.findOneAndReplace(Filters.eq("uuid", sessionStorage.getUniqueId().toString()), newDocument);
        }
    }

    public SessionStorage loadSessionStorage(String name) {
        if (this.collection == null) {
            Bukkit.getLogger().warning("MongoDB not initialized...");

            return null;
        }

        Document document = this.collection.find(Filters.eq("name", name)).first();

        if (document == null) {
            return null;
        }

        return new SessionStorage(
                document.getString("name"),
                UUID.fromString(document.getString("uuid")),
                document.getInteger("gamesPlayed", 0),
                document.getInteger("wins", 0),
                document.getInteger("currentWinStreak", 0)
        );
    }

    public SessionStorage loadSessionStorage(String name, UUID uuid) {
        if (this.collection == null) {
            Bukkit.getLogger().warning("MongoDB not initialized...");

            return null;
        }

        Document document = this.collection.find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null || document.isEmpty()) {
            return new SessionStorage(name, uuid);
        }

        if (name == null) {
            name = document.getString("name");
        }

        return new SessionStorage(
                name,
                uuid,
                document.getInteger("gamesPlayed", 0),
                document.getInteger("wins", 0),
                document.getInteger("currentWinStreak", 0)
        );
    }
}