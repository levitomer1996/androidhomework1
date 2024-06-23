package com.example.homework1;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.util.Log;
import android.widget.TextView;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class TankGameActivity extends AppCompatActivity {
    private static final String LOG_TAG = "TankActivity";

    public int getTankPosition() {
        return tankPosition;
    }


    private int score = 0;
    private int tankPosition = 1;
    public static final int NUM_OF_COLS = 3;
    public static final int NUM_OF_ROWS = 4;


    private int num_of_lives = 2;


    private int[][] type_mat = {{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};
    private int[] lives = {R.id.live0, R.id.live1, R.id.live2};

    private TextView scoreTextView;
    private int[][] leaner_ids = {{R.id.row0col0, R.id.row0col1, R.id.row0col2},
            {R.id.row1col0, R.id.row1col1, R.id.row1col2},
            {R.id.row2col0, R.id.row2col1, R.id.row2col2},
            {R.id.row3col0, R.id.row3col1, R.id.row3col2}};

    private int[] colisionZone = {0, 0, 0};
    private int[] col0;
    private int[] col1;
    private int[] col2;

    private int[] rocketsOnBoard;

    private CountDownTimer gameTimer;
    private MaterialButton leftButton;
    private MaterialButton rightButton;
    public static final int DELAY = 1000;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tank_game);
        this.scoreTextView = findViewById(R.id.scoreTextView);
        leftButton = findViewById(R.id.left_button);
        rightButton = findViewById(R.id.right_button);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Setting tank component at the center


        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tankPosition > 0) {
                    setTankPosition(tankPosition - 1);
                    renderTankPos(tankPosition);
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tankPosition < 2) {
                    setTankPosition(tankPosition + 1);
                    renderTankPos(tankPosition);
                }
            }
        });
        clearImages();
        startTimer();
    }

    private void startTimer() {
        gameTimer = new CountDownTimer(Long.MAX_VALUE, 1000) { // CountDownTimer with 1 second interval
            @Override
            public void onTick(long millisUntilFinished) {
                launchItem();
                moveOneStepDown();
                detectCrash();
            }

            @Override
            public void onFinish() {
                // This method will never be called as we're using Long.MAX_VALUE as the millisInFuture
            }
        };
        gameTimer.start(); // Start the timer
    }

    private void stopTime() {
        gameTimer.cancel();
    }

    public void setTankPosition(int pos) {
        this.tankPosition = pos;
    }

    public void launchItem() {
        Random random = new Random();
        int randomItem = getRandomNumber(1);
        int randomCol = getRandomNumber(2);

        switch (randomItem) {

            case 0: {
                this.setType_mat(0, randomCol, 0);
                break;
            }

            case 1: {
                this.setType_mat(0, randomCol, 1);
                break;
            }
        }
        Log.i(LOG_TAG, "Type is " + this.getType_mat()[0][randomCol]);
        logTypeMat();
        renderItem(0, randomCol);
    }

    public void renderItem(int r, int c) {
        ImageView imageView = findViewById(this.getLeaner_ids()[r][c]);

        switch (this.getType_mat()[r][c]) {
            case -1: {
                imageView.setImageDrawable(null);
                break;
            }
            case 0: {
                imageView.setImageResource(R.drawable.rpghead);
                break;
            }
            case 1: {
                imageView.setImageResource(R.drawable.bamba);
                break;
            }

        }
    }

    public void moveOneStepDown() {
        for (int r = NUM_OF_ROWS - 2; r >= 0; r--) { // Adjusted loop condition
            for (int c = 0; c < NUM_OF_COLS; c++) {
                Log.i("TANK", r + " " + c);
                if (r + 1 != 4) {
                    this.setType_mat(r + 1, c, this.getType_mat()[r][c]); // Move the value down one row
                }
                this.setType_mat(r, c, -1); // Set the current position to -1

                renderItem(r, c);
                if (r + 1 != 4) {
                    renderItem(r + 1, c);
                    renderTankPos(this.getTankPosition());
                }
            }
        }
    }

    public void detectCrash() {
        for (int i = 0; i < NUM_OF_COLS; i++) {
            if (this.getType_mat()[3][i] == 0 && this.getTankPosition() == i) {
                removeOneLive();
                makeVibrate();
            }
            if (this.getType_mat()[3][i] == 1 && this.getTankPosition() == i) {
                this.setScore(this.getScore() + 1);
                this.updateScore();
            }
        }
        if (this.num_of_lives == -1) {
            stopTime();
            Intent intent = new Intent(TankGameActivity.this, GameoverActivity.class);
            startActivity(intent);
        }
    }

    private void logTypeMat() {
        StringBuilder sb = new StringBuilder();
        sb.append("type_mat = [\n");
        for (int i = 0; i < type_mat.length; i++) {
            sb.append("  {");
            for (int j = 0; j < type_mat[i].length; j++) {
                sb.append(this.getType_mat()[i][j]);
                if (j < type_mat[i].length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("}");
            if (i < type_mat.length - 1) {
                sb.append(",\n");
            }
        }
        sb.append("\n]");
        Log.d("TankActivity", sb.toString());
    }

    public static int getRandomNumber(int max) {
        Random random = new Random();
        return random.nextInt(max + 1);
    }

    private void removeOneLive() {
        if (num_of_lives >= 0) {
            // Get the index of the live image to remove
            int indexToRemove = num_of_lives; // Remove the last live image
            findViewById(this.lives[this.num_of_lives]).setVisibility(View.INVISIBLE);
            this.num_of_lives--;
        }
    }

    public void renderTankPos(int pos) {
        ImageView tankcol_0 = findViewById(R.id.row3col0);
        ImageView tankcol_1 = findViewById(R.id.row3col1);
        ImageView tankcol_2 = findViewById(R.id.row3col2);

        switch (pos) {
            case 0:
                tankcol_0.setImageResource(R.drawable.tank);
                tankcol_1.setImageDrawable(null);
                tankcol_2.setImageDrawable(null);
                break;
            case 1:
                tankcol_0.setImageDrawable(null);
                tankcol_1.setImageResource(R.drawable.tank);
                tankcol_2.setImageDrawable(null);
                break;
            case 2:
                tankcol_0.setImageDrawable(null);
                tankcol_1.setImageDrawable(null);
                tankcol_2.setImageResource(R.drawable.tank);
                break;
        }
    }

    public void clearImages() {
        int[][] list = this.getLeaner_ids();
        for (int row = 0; row < list.length; row++) {
            for (int col = 0; col < list[row].length; col++) {
                ImageView imageView = findViewById(list[row][col]);
                if (imageView != null) {
                    imageView.setImageDrawable(null);
                }
            }
        }
    }

    public int[][] getType_mat() {
        return type_mat;
    }

    public void setType_mat(int r, int c, int val) {
        type_mat[r][c] = val;
    }

    public int[][] getLeaner_ids() {
        return leaner_ids;
    }

    public void setLeaner_ids(int[][] leaner_ids) {
        this.leaner_ids = leaner_ids;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private void updateScore() {
        scoreTextView.setText("Score: " + this.score);
    }

    private void makeVibrate() {
        final VibrationEffect vibrationEffect1;
        // this is the only type of the vibration which requires system version Oreo (API 26)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // this effect creates the vibration of default amplitude for 1000ms(1 sec)
            vibrationEffect1 = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
            // it is safe to cancel other vibrations currently taking place
            vibrator.cancel();
            vibrator.vibrate(vibrationEffect1);
        }
    }

}