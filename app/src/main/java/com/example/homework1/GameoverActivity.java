package com.example.homework1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homework1.localstorage.RecordsSaver;

public class GameoverActivity extends AppCompatActivity {
    private int score;
    private RecordsSaver rs; // Declare RecordsSaver object
    private EditText playerNameEditText;
    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gameover);

        // Initialize RecordsSaver here
        rs = new RecordsSaver(this); // Assuming 'this' refers to the activity context

        Intent intent = getIntent();
        if (intent != null) {
            this.score = intent.getIntExtra("score", 0);
        }

        playerNameEditText = findViewById(R.id.player_name_edittext);
        scoreTextView = findViewById(R.id.score_textview);
        scoreTextView.setText("Your Score: " + score);

        Button startGameButton = findViewById(R.id.start_game_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start the TankGameActivity
                Intent intent = new Intent(GameoverActivity.this, TankGameActivity.class);
                startActivity(intent);
            }
        });

        Button goToMainButton = findViewById(R.id.go_to_main_button);
        goToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start the MainActivity
                Intent intent = new Intent(GameoverActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = playerNameEditText.getText().toString().trim();
                saveScore(playerName);
            }
        });
    }

    private void saveScore(String playerName) {
        if (playerName.isEmpty()) {
            playerNameEditText.setError("Please enter a name");
            return;
        }

        // Save the score with the player name using your RecordsSaver class
        rs.saveRecord(score, playerName);

        // Optionally, you can navigate to another activity or perform any other action after saving
        // For example, you can go back to the main activity
        Intent intent = new Intent(GameoverActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
