package net.vicnix.tnttag.provider;

import net.vicnix.tnttag.session.SessionStorage;

import java.util.UUID;

public class MongoDBProvider {

    private final static MongoDBProvider instance = new MongoDBProvider();

    public static MongoDBProvider getInstance() {
        return instance;
    }

    public void init() {

    }

    public SessionStorage loadSessionStorage(String name) {
        return null;
    }

    public SessionStorage loadSessionStorage(UUID uuid) {
        return null;
    }

    public void saveSessionStorage(SessionStorage sessionStorage) {

    }
}