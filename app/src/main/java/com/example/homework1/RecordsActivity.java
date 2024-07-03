package com.example.homework1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homework1.localstorage.RecordsSaver;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class RecordsActivity extends AppCompatActivity {

    private static final String TAG = "RecordsActivity";
    private TableLayout tableLayout;
    private RecordsSaver recordsSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        tableLayout = findViewById(R.id.record_table);
        MaterialButton backButton = findViewById(R.id.back_button);
        recordsSaver = new RecordsSaver(this);

        // Set click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(RecordsActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish current activity
                Log.d(TAG, "Back button clicked, navigating to MainActivity.");
            }
        });

        // Retrieve sorted records and display them
        displaySortedRecords();
    }

    private void displaySortedRecords() {
        List<RecordsSaver.Record> sortedRecords = recordsSaver.getSortedRecords();
        Log.d(TAG, "Retrieved sorted records: " + sortedRecords);

        for (RecordsSaver.Record record : sortedRecords) {
            String playerName = record.getPlayerName();
            String score = String.valueOf(record.getScore());
            String location = "Unknown"; // Replace with actual location data if available
            addRow(playerName, score, location);
            Log.d(TAG, "Added row: " + playerName + ", Score: " + score + ", Location: " + location);
        }
    }

    private void addRow(String name, String score, String location) {
        TableRow row = new TableRow(this);

        TextView nameTextView = createTextView(name);
        row.addView(nameTextView);

        TextView scoreTextView = createTextView(score);
        row.addView(scoreTextView);

        TextView locationTextView = createTextView(location);
        row.addView(locationTextView);

        tableLayout.addView(row);
        Log.d(TAG, "Added TableRow to TableLayout.");
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)); // 1:1 weight
        textView.setPadding(8, 8, 8, 8);
        return textView;
    }
}
