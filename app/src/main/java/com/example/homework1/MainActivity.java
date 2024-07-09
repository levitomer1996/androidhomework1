package com.example.homework1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private int DELAY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the start game button and set an onClickListener
        Button startGameButton = findViewById(R.id.start_game_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start the TankGameActivity with DELAY
                Intent intent = new Intent(MainActivity.this, TankGameActivity.class);
                intent.putExtra("DELAY", DELAY);
                startActivity(intent);
            }
        });

        // Find the view records button and set an onClickListener
        Button viewRecordsButton = findViewById(R.id.view_records_button);
        viewRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start the RecordsActivity
                Intent intent = new Intent(MainActivity.this, RecordsActivity.class);
                startActivity(intent);
            }
        });

        // Find the slow button and set an onClickListener
        Button slowButton = findViewById(R.id.slow_button);
        slowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDELAY(1000);
            }
        });

        // Find the fast button and set an onClickListener
        Button fastButton = findViewById(R.id.fast_button);
        fastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDELAY(500);
            }
        });

        // Find the sensor mode button and set an onClickListener
        Button sensorModeButton = findViewById(R.id.sensor_mode_button);
       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public int getDELAY() {
        return DELAY;
    }

    public void setDELAY(int DELAY) {
        this.DELAY = DELAY;
    }
}
