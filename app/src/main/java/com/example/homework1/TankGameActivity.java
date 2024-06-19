package com.example.homework1;

import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class TankGameActivity extends AppCompatActivity {
    private int tankPosition = 1;
    public static final int NUM_OF_COLS = 3;
    public static final int NUM_OF_ROWS = 4;
    private int num_of_lives = 3;

    private int[][] leaner_ids;
    private int[] lives = {R.id.live0, R.id.live1, R.id.live2};
    private int[] col0;
    private int[] col1;
    private int[] col2;

    private int[] rocketsOnBoard;


    private CountDownTimer gameTimer;
    private MaterialButton leftButton = findViewById(R.id.left_button);
    private  MaterialButton rightButton = findViewById(R.id.right_button);
    public static final int DELAY = 1000;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tank_game);

        // Setting tank component at the center
        ImageView tankcol_0 = findViewById(R.id.row3col0);
        tankcol_0.setVisibility(View.INVISIBLE);

        ImageView tankcol_1 = findViewById(R.id.row3col1);
        ImageView tankcol_2 = findViewById(R.id.row3col2);
        tankcol_2.setVisibility(View.INVISIBLE);
        //l

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

//        startTimer();
    }



    public void setTankPosition(int pos) {
        this.tankPosition = pos;
    }

    public void renderTankPos(int pos) {
        ImageView tankcol_0 = findViewById(R.id.row3col0);
        ImageView tankcol_1 = findViewById(R.id.row3col1);
        ImageView tankcol_2 = findViewById(R.id.row3col2);

        switch (pos) {
            case 0:
                tankcol_0.setVisibility(View.VISIBLE);
                tankcol_1.setVisibility(View.INVISIBLE);
                tankcol_2.setVisibility(View.INVISIBLE);
                break;
            case 1:
                tankcol_0.setVisibility(View.INVISIBLE);
                tankcol_1.setVisibility(View.VISIBLE);
                tankcol_2.setVisibility(View.INVISIBLE);
                break;
            case 2:
                tankcol_0.setVisibility(View.INVISIBLE);
                tankcol_1.setVisibility(View.INVISIBLE);
                tankcol_2.setVisibility(View.VISIBLE);
                break;
        }
    }

    public int[][] recognizeImageViews() {

        int[][] ids = {
                {R.id.row0col0, R.id.row0col1, R.id.row0col2},
                {R.id.row1col0, R.id.row1col1, R.id.row1col2},
                {R.id.row2col0, R.id.row2col1, R.id.row2col2}
        };

        this.setLeaner_ids(ids);

        setCol0(ids[0]);
        setCol1(ids[1]);
        setCol2(ids[2]);
        for (int row = 0; row < ids.length; row++) {
            for (int col = 0; col < ids[row].length; col++) {
                ImageView imageView = findViewById(ids[row][col]);
                if (imageView != null) {
                   imageView.setImageDrawable(null);
                }
            }
        }
        return ids;
    }

//    private void startTimer() {
//        gameTimer = new CountDownTimer(Long.MAX_VALUE, 1000) { // CountDownTimer with 1 second interval
//            @Override
//            public void onTick(long millisUntilFinished) {
//                launchRocket();
//                moveOneStepDown();
//            }
//
//            @Override
//            public void onFinish() {
//                // This method will never be called as we're using Long.MAX_VALUE as the millisInFuture
//            }
//        };
//
//        gameTimer.start(); // Start the timer
//    }
    public  void launchRocket(){
        ImageView row0_col0 =findViewById(col0[0]);
        ImageView row0_col1 = findViewById(col1[0]);
        ImageView row0_col2 = findViewById(col2[0]);
      int randCol =  getRandomCol();
        if(randCol == 0){
            row0_col0.setImageResource(R.drawable.rpghead);
        }
        if(randCol == 1){
            row0_col1.setImageResource(R.drawable.rpghead);
        }
        if(randCol == 2) {
            row0_col2.setImageResource(R.drawable.rpghead);
        }
    }

    public void moveOneStepDown(){
        for (int i = 0 ; i < NUM_OF_COLS; i++) {
            for (int j = NUM_OF_ROWS - 1; j >= 0 ; j--) {
                ImageView tempView = findViewById(this.getLeaner_ids()[i][j]);
                if(j ==  NUM_OF_ROWS - 1 && tempView.getDrawable() != null){
                    tempView.setImageDrawable(null);
                }else {
                    if(tempView.getDrawable() != null){
                        ImageView under_tempView = findViewById(this.getLeaner_ids()[i][j + 1]);
                        under_tempView.setImageDrawable(tempView.getDrawable());
                    }
                }


            }
        }
    }


    private void removeOneLive() {
        if (num_of_lives > 0) {
            // Get the index of the live image to remove
            int indexToRemove = num_of_lives - 1; // Remove the last live image
            findViewById(this.lives[indexToRemove]).setVisibility(View.INVISIBLE);
            num_of_lives--;
        }
    }
    public static int getRandomCol() {
        Random random = new Random();
        return random.nextInt(3);  // Generates a random number between 0 (inclusive) and 3 (exclusive)
    }
    public int[] getCol0() {
        return col0;
    }

    public void setCol0(int[] col0) {
        this.col0 = col0;
    }

    public int[] getCol1() {
        return col1;
    }

    public void setCol1(int[] col1) {
        this.col1 = col1;
    }

    public int[] getCol2() {
        return col2;
    }

    public void setCol2(int[] col2) {
        this.col2 = col2;
    }
    public int[][] getLeaner_ids() {
        return leaner_ids;
    }

    public void setLeaner_ids(int[][] leaner_ids) {
        this.leaner_ids = leaner_ids;
    }

}
