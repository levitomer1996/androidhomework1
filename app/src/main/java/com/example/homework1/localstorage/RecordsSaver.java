package com.example.homework1.localstorage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordsSaver {

    private static final String PREFS_NAME = "game_records";
    private static final String RECORDS_KEY = "records";

    private SharedPreferences sharedPreferences;

    public RecordsSaver(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveRecord(int score, String playerName) {
        List<Record> records = getRecords();
        records.add(new Record(score, playerName));
        saveRecords(records);
    }

    public List<Record> getSortedRecords() {
        List<Record> records = getRecords();
        Collections.sort(records, Collections.reverseOrder()); // Sort records in descending order
        return records;
    }

    private List<Record> getRecords() {
        String recordsString = sharedPreferences.getString(RECORDS_KEY, "");
        List<Record> records = new ArrayList<>();
        if (!recordsString.isEmpty()) {
            String[] recordsArray = recordsString.split(";");
            for (String recordStr : recordsArray) {
                String[] parts = recordStr.split(",");
                int score = Integer.parseInt(parts[0]);
                String playerName = parts[1];
                records.add(new Record(score, playerName));
            }
        }
        return records;
    }

    private void saveRecords(List<Record> records) {
        StringBuilder recordsString = new StringBuilder();
        for (Record record : records) {
            recordsString.append(record.getScore()).append(",").append(record.getPlayerName()).append(";");
        }
        if (recordsString.length() > 0) {
            recordsString.deleteCharAt(recordsString.length() - 1); // Remove the trailing semicolon
        }
        sharedPreferences.edit().putString(RECORDS_KEY, recordsString.toString()).apply();
    }

    public static class Record implements Comparable<Record> {
        private int score;
        private String playerName;

        public Record(int score, String playerName) {
            this.score = score;
            this.playerName = playerName;
        }

        public int getScore() {
            return score;
        }

        public String getPlayerName() {
            return playerName;
        }

        @Override
        public int compareTo(Record another) {
            return Integer.compare(this.score, another.score);
        }
    }
}
