package com.example.homework1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class RecordsActivity extends AppCompatActivity {

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        tableLayout = findViewById(R.id.record_table);
        MaterialButton backButton = findViewById(R.id.back_button);

        // Set click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(RecordsActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish current activity
            }
        });

        // Example of adding rows dynamically
        addRow("Player 2", "1200", "Canada");
        addRow("Player 3", "1500", "UK");
    }

    private void addRow(String name, String score, String location) {
        TableRow row = new TableRow(this);

        TextView nameTextView = new TextView(this);
        nameTextView.setText(name);
        nameTextView.setPadding(8, 8, 8, 8);
        row.addView(nameTextView);

        TextView scoreTextView = new TextView(this);
        scoreTextView.setText(score);
        scoreTextView.setPadding(8, 8, 8, 8);
        row.addView(scoreTextView);

        TextView locationTextView = new TextView(this);
        locationTextView.setText(location);
        locationTextView.setPadding(8, 8, 8, 8);
        row.addView(locationTextView);

        tableLayout.addView(row);
    }
}
