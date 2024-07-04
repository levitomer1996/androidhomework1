package com.example.homework1.utilies;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SoundPlayer {

    private Context context;
    private Executor executor;
    //private MediaPlayer mediaPlayer;

    public SoundPlayer(Context context) {
        this.context = context.getApplicationContext();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void playSound(int res) {
        executor.execute(() -> {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, res);
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(1.0f, 1.0f);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.stop();
                mp.release();
                mp = null;
            });
        });
    }

//    public void playSound(int res) {
//        playSound(res, 1.0f);
//    }
//
//    public void playSound(int res, float volume) {
//        if (mediaPlayer != null) {
//            stopSound();
//        }
//        if (mediaPlayer == null) {
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    mediaPlayer = MediaPlayer.create(context, res);
//                    mediaPlayer.setLooping(false);
//                    mediaPlayer.setVolume(volume, volume);
//                    mediaPlayer.start();
//                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
//                            mp.stop();
//                            mp.release();
//                            mp = null;
//                        }
//                    });
//                }
//            });
//        }
//    }
//
//    public void stopSound() {
//        if (mediaPlayer != null) {
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    mediaPlayer = null;
//                }
//            });
//        }
//    }


}