package com.example.collector;

import android.app.Activity;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PowerManager;


public class CurrentScore_class extends Activity {

    HomeWatcher mHomeWatcher;
    SharedPreferences sp;
    int musicCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentscore);

        SharedPreferences loadToggleState = getSharedPreferences("data", Context.MODE_PRIVATE);
        if(loadToggleState.contains("music"))
        {
            musicCounter = loadToggleState.getInt("music", 0); //0 is the default value
        }

        if (musicCounter == 0) {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicPlayer.class);
            startService(music);
        }

        TextView theScore = findViewById(R.id.theScore_st);

       final int score = getIntent().getIntExtra("SCORE", 0);
        theScore.setText(String.valueOf(score));

        Button againBt = findViewById(R.id.againBt);
        againBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent= new Intent(CurrentScore_class.this, Game_class.class);
                startActivity(gameIntent);
            }
        });


        Button to_menu_btn = findViewById(R.id.to_main_menu_btn);
        to_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuIntent= new Intent(CurrentScore_class.this, MainActivity.class);
                startActivity(menuIntent);
            }
        });


        //Start HomeWatcher
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();

    }

    //Bind/Unbind music service
    private boolean mIsBound = false;
    private MusicPlayer mServ;
    private ServiceConnection Scon = new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicPlayer.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicPlayer.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }

        SharedPreferences loadToggleState = getSharedPreferences("data", Context.MODE_PRIVATE);
        if(loadToggleState.contains("music"))
        {
            musicCounter = loadToggleState.getInt("music", 0); //0 is the default value
        }

        if(musicCounter == 0)
        {
            if (mServ != null)
            {
                mServ.resumeMusic();
            } else
            {
                doBindService();
                Intent music = new Intent();
                music.setClass(CurrentScore_class.this, MusicPlayer.class);
                startService(music);
                if (mServ != null)
                {
                    mServ.resumeMusic();
                }
            }
        }
        else
        {
            if (mServ != null)
            {
                mServ.pauseMusic();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Detect idle screen
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //UNBIND music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicPlayer.class);
        stopService(music);

    }

}