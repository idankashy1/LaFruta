package com.example.collector;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayer {

    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX=2;

    private static SoundPool soundPool;
    private static  int hitSound;
    private static int overSound;
    private static  int worm;
    private static  int win;


    public SoundPlayer(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            audioAttributes=new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();


            soundPool=new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(SOUND_POOL_MAX).build();

        }
        else {
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC,0);

        }






        hitSound=soundPool.load(context,R.raw.fruit,1);
        overSound=soundPool.load(context,R.raw.final_game,1);
        worm=soundPool.load(context,R.raw.eror,1);
        win=soundPool.load(context,R.raw.win,1);
    }

    public void playHitSound(){
        soundPool.play(hitSound,1.0f,1.0f,1,0,1.0f) ;

    }

    public void playoverSound(){
        soundPool.play(overSound,1.0f,1.0f,1,0,1.0f) ;

    }

    public void playwormSound(){
        soundPool.play(worm,1.0f,1.0f,1,0,1.0f) ;

    }

    public void playWinSound(){
        soundPool.play(win,1.0f,1.0f,1,0,1.0f) ;

    }


}
