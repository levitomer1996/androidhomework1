package com.example.homework1.localstorage;

public interface LocalStorageHandler {
    void save(String key, String value);

    String retrieve(String key);
}
