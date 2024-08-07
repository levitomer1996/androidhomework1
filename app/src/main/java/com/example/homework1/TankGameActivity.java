package com.example.homework1;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homework1.localstorage.RecordsSaver;
import com.example.homework1.utilies.SoundPlayer;
import com.google.android.material.button.MaterialButton;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class TankGameActivity extends AppCompatActivity implements SensorEventListener {
    private static final String LOG_TAG = "TankActivity";
    private static final int NUM_OF_COLS = 5;
    private static final int NUM_OF_ROWS = 9;
    private static final float TILT_THRESHOLD = 2.0f; // Adjust this threshold as needed

    private MediaPlayer mediaPlayer;
    private SoundPlayer soundPlayer;
    private int max_record;
    private RecordsSaver rs;
    private int score = 0;
    private int DELAY = 500;
    private boolean SENSOR_MODE = false;


    private int distance = 0;
    private int tankPosition = 1;
    private int num_of_lives = 2;

    private int[][] type_mat = {
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1}
    };

    private int[][] leaner_ids = {
            {R.id.row0col0, R.id.row0col1, R.id.row0col2, R.id.row0col3, R.id.row0col4},
            {R.id.row1col0, R.id.row1col1, R.id.row1col2, R.id.row1col3, R.id.row1col4},
            {R.id.row2col0, R.id.row2col1, R.id.row2col2, R.id.row2col3, R.id.row2col4},
            {R.id.row3col0, R.id.row3col1, R.id.row3col2, R.id.row3col3, R.id.row3col4},
            {R.id.row4col0, R.id.row4col1, R.id.row4col2, R.id.row4col3, R.id.row4col4},
            {R.id.row5col0, R.id.row5col1, R.id.row5col2, R.id.row5col3, R.id.row5col4},
            {R.id.row6col0, R.id.row6col1, R.id.row6col2, R.id.row6col3, R.id.row6col4},
            {R.id.row7col0, R.id.row7col1, R.id.row7col2, R.id.row7col3, R.id.row7col4},
            {R.id.row8col0, R.id.row8col1, R.id.row8col2, R.id.row8col3, R.id.row8col4}
    };

    private int[] lives = {R.id.live0, R.id.live1, R.id.live2};

    private TextView scoreTextView;
    private TextView distanceTextView;

    private CountDownTimer gameTimer;
    private CountDownTimer explosionTimer;
    private MaterialButton leftButton;
    private MaterialButton rightButton;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Vibrator vibrator;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tank_game);
        Intent intent = getIntent();
        DELAY = intent.getIntExtra("DELAY", 500);
        SENSOR_MODE = intent.getBooleanExtra("SENSOR", false);
        this.rs = new RecordsSaver(this); // Initialize RecordsSaver here
        this.soundPlayer = new SoundPlayer(this);
        this.scoreTextView = findViewById(R.id.scoreTextView);
        leftButton = findViewById(R.id.left_button);
        rightButton = findViewById(R.id.right_button);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTankLeft();
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTankRight();
            }
        });

        clearImages();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            if (x > TILT_THRESHOLD) {
                moveTankLeft();
            } else if (x < -TILT_THRESHOLD) {
                moveTankRight();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Implement if sensor accuracy changes need to be handled
    }

    private void moveTankLeft() {
        if (tankPosition > 0) {
            setTankPosition(tankPosition - 1);
            renderTankPos(tankPosition);
        }
    }

    private void moveTankRight() {
        if (tankPosition < NUM_OF_COLS - 1) {
            setTankPosition(tankPosition + 1);
            renderTankPos(tankPosition);
        }
    }

    private void startTimer() {
        gameTimer = new CountDownTimer(Long.MAX_VALUE, DELAY) {
            @Override
            public void onTick(long millisUntilFinished) {
                launchItem();
                moveOneStepDown();
                detectCrash();
            }

            @Override
            public void onFinish() {
                Log.i(LOG_TAG, "Time finished");
            }
        };
        gameTimer.start();
    }

    private void stopTime() {
        gameTimer.cancel();
    }

    public int getTankPosition() {
        return tankPosition;
    }

    public void setTankPosition(int pos) {
        this.tankPosition = pos;
    }

    public void launchItem() {
        Random random = new Random();
        int randomItem = getRandomNumber(1);
        int randomCol = getRandomNumber(NUM_OF_COLS - 1);

        switch (randomItem) {
            case 0:
                this.setType_mat(0, randomCol, 0);
                break;
            case 1:
                this.setType_mat(0, randomCol, 1);
                break;
        }

        renderItem(0, randomCol);
    }

    public void renderItem(int r, int c) {
        ImageView imageView = findViewById(this.getLeaner_ids()[r][c]);
        switch (this.getType_mat()[r][c]) {
            case -1:
                imageView.setImageDrawable(null);
                break;
            case 0:
                imageView.setImageResource(R.drawable.rpghead);
                break;
            case 1:
                imageView.setImageResource(R.drawable.bamba);
                break;
        }
    }

    public void moveOneStepDown() {
        for (int r = NUM_OF_ROWS - 2; r >= 0; r--) {
            for (int c = 0; c < NUM_OF_COLS; c++) {
                if (r + 1 != NUM_OF_ROWS) {
                    this.setType_mat(r + 1, c, this.getType_mat()[r][c]);
                }
                this.setType_mat(r, c, -1);
                renderItem(r, c);
                if (r + 1 != NUM_OF_ROWS) {
                    renderItem(r + 1, c);
                    renderTankPos(this.getTankPosition());
                }
            }
        }
        this.setDistance(this.getDistance() + 1);
        setDistanceText(this.getDistance());
    }

    public void detectCrash() {
        for (int i = 0; i < NUM_OF_COLS; i++) {
            if (this.getType_mat()[8][i] == 0 && this.getTankPosition() == i) {
                removeOneLive();
                makeVibrate();
                ImageView exp = findViewById(this.getLeaner_ids()[8][this.getTankPosition()]);
                explosionTimer = new CountDownTimer(Long.MAX_VALUE, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        soundPlayer.playSound(R.raw.esound);
                        exp.setImageResource(R.drawable.exp);
                        explosionTimer.cancel();
                    }

                    @Override
                    public void onFinish() {
                        // This method will never be called as we're using Long.MAX_VALUE as the millisInFuture
                    }
                };
                explosionTimer.start();
            }
            if (this.getType_mat()[NUM_OF_ROWS - 1][i] == 1 && this.getTankPosition() == i) {
                this.setScore(this.getScore() + 1);
                this.updateScore();
            }
        }
        if (this.num_of_lives == -1) {
            stopTime();
            Intent intent = new Intent(TankGameActivity.this, GameoverActivity.class);
            intent.putExtra("score", this.getScore());
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
            int indexToRemove = num_of_lives;
            findViewById(this.lives[this.num_of_lives]).setVisibility(View.INVISIBLE);
            this.num_of_lives--;
        }
    }

    public void renderTankPos(int pos) {
        ImageView tankcol_0 = findViewById(R.id.row8col0);
        ImageView tankcol_1 = findViewById(R.id.row8col1);
        ImageView tankcol_2 = findViewById(R.id.row8col2);
        ImageView tankcol_3 = findViewById(R.id.row8col3);
        ImageView tankcol_4 = findViewById(R.id.row8col4);

        switch (pos) {
            case 0:
                tankcol_0.setImageResource(R.drawable.tank);
                tankcol_1.setImageDrawable(null);
                tankcol_2.setImageDrawable(null);
                tankcol_3.setImageDrawable(null);
                tankcol_4.setImageDrawable(null);
                break;
            case 1:
                tankcol_0.setImageDrawable(null);
                tankcol_1.setImageResource(R.drawable.tank);
                tankcol_2.setImageDrawable(null);
                tankcol_3.setImageDrawable(null);
                tankcol_4.setImageDrawable(null);
                break;
            case 2:
                tankcol_0.setImageDrawable(null);
                tankcol_1.setImageDrawable(null);
                tankcol_2.setImageResource(R.drawable.tank);
                tankcol_3.setImageDrawable(null);
                tankcol_4.setImageDrawable(null);
                break;
            case 3:
                tankcol_0.setImageDrawable(null);
                tankcol_1.setImageDrawable(null);
                tankcol_2.setImageDrawable(null);
                tankcol_3.setImageResource(R.drawable.tank);
                tankcol_4.setImageDrawable(null);
                break;
            case 4:
                tankcol_0.setImageDrawable(null);
                tankcol_1.setImageDrawable(null);
                tankcol_2.setImageDrawable(null);
                tankcol_3.setImageDrawable(null);
                tankcol_4.setImageResource(R.drawable.tank);
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrationEffect1 = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.cancel();
            vibrator.vibrate(vibrationEffect1);
        }
    }

    public void setDistanceText(int distance) {
        this.distanceTextView = findViewById(R.id.distanceTextView);
        if (this.distanceTextView != null) {
            distanceTextView.setText("Distance: " + distance);
        }
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    private void playSound(int soundResourceId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, soundResourceId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mediaPlayer = null;
            }
        });
        mediaPlayer.start();
    }

    public int getMax_record() {
        return max_record;
    }

    public void setMax_record(int max_record) {
        this.max_record = max_record;
    }
}
