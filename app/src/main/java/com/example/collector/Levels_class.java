package com.example.collector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PowerManager;

public class Levels_class extends AppCompatActivity
{
    HomeWatcher mHomeWatcher;

    SharedPreferences sp;
    int curr_lvl;

    Button level_1_btn;
    Button level_2_btn;
    Button level_3_btn;
    Button level_4_btn;
    Button level_5_btn;
    Button level_6_btn;
    Button level_7_btn;
    Button level_8_btn;
    Button level_9_btn;
    Button level_10_btn;

    Animation scale_anim;
    int musicCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

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

        sp = getSharedPreferences("data", MODE_PRIVATE);
        curr_lvl = sp.getInt("level", 1);


        scale_anim = AnimationUtils.loadAnimation(Levels_class.this, R.anim.scale);

        level_1_btn = findViewById(R.id.level_1_btn);
        level_1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Levels_class.this, Game_class.class);
                startActivity(intent);
                onBackPressed();
            }
        });

        level_2_btn = findViewById(R.id.level_2_btn);
        level_2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Levels_class.this, Level_2_Activity.class);
                startActivity(intent);
                onBackPressed();
            }
        });


        level_3_btn = findViewById(R.id.level_3_btn);
        level_3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Levels_class.this, Level_3_Activity.class);
                startActivity(intent);
                onBackPressed();
            }
        });


        level_4_btn = findViewById(R.id.level_4_btn);
        level_4_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Levels_class.this, Level_4_Activity.class);
                startActivity(intent);
                onBackPressed();
            }
        });


        level_5_btn = findViewById(R.id.level_5_btn);
        level_5_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Levels_class.this, Level_5_Activity.class);
                startActivity(intent);
                onBackPressed();
            }
        });


        level_6_btn = findViewById(R.id.level_6_btn);
        level_6_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Levels_class.this, Level_6_Activity.class);
                startActivity(intent);
                onBackPressed();
            }
        });


        level_7_btn = findViewById(R.id.level_7_btn);
        level_7_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Levels_class.this, Level_7_Activity.class);
                startActivity(intent);
                onBackPressed();
            }
        });


        level_8_btn = findViewById(R.id.level_8_btn);
        level_8_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Levels_class.this, Level_8_Activity.class);
                startActivity(intent);
                onBackPressed();
            }
        });


        level_9_btn = findViewById(R.id.level_9_btn);
        level_9_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Levels_class.this, Level_9_Activity.class);
                startActivity(intent);
                onBackPressed();
            }
        });


        level_10_btn = findViewById(R.id.level_10_btn);
        level_10_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Levels_class.this, Level_10_Activity.class);
                startActivity(intent);
                onBackPressed();
            }
        });

        level_1_btn.startAnimation(scale_anim);




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


    @Override
    protected void onResume()
    {
        super.onResume();
        // לקבל ולעדכן את השלב הנוכחי

        switch (curr_lvl)
        {
            case 2:
                level_1_btn.clearAnimation();
                level_1_btn.setVisibility(View.VISIBLE);

                level_2_btn.setVisibility(View.VISIBLE);
                level_2_btn.startAnimation(scale_anim);
                break;

            case 3:
                level_1_btn.clearAnimation();
                level_1_btn.setVisibility(View.VISIBLE);
                level_2_btn.clearAnimation();
                level_2_btn.setVisibility(View.VISIBLE);

                level_3_btn.setVisibility(View.VISIBLE);
                level_3_btn.startAnimation(scale_anim);
                break;

            case 4:
                level_1_btn.clearAnimation();
                level_1_btn.setVisibility(View.VISIBLE);
                level_2_btn.clearAnimation();
                level_2_btn.setVisibility(View.VISIBLE);
                level_3_btn.clearAnimation();
                level_3_btn.setVisibility(View.VISIBLE);

                level_4_btn.setVisibility(View.VISIBLE);
                level_4_btn.startAnimation(scale_anim);
                break;

            case 5:
                level_1_btn.clearAnimation();
                level_1_btn.setVisibility(View.VISIBLE);
                level_2_btn.clearAnimation();
                level_2_btn.setVisibility(View.VISIBLE);
                level_3_btn.clearAnimation();
                level_3_btn.setVisibility(View.VISIBLE);
                level_4_btn.clearAnimation();
                level_4_btn.setVisibility(View.VISIBLE);

                level_5_btn.setVisibility(View.VISIBLE);
                level_5_btn.startAnimation(scale_anim);
                break;

            case 6:
                level_1_btn.clearAnimation();
                level_1_btn.setVisibility(View.VISIBLE);
                level_2_btn.clearAnimation();
                level_2_btn.setVisibility(View.VISIBLE);
                level_3_btn.clearAnimation();
                level_3_btn.setVisibility(View.VISIBLE);
                level_4_btn.clearAnimation();
                level_4_btn.setVisibility(View.VISIBLE);
                level_5_btn.clearAnimation();
                level_5_btn.setVisibility(View.VISIBLE);

                level_6_btn.setVisibility(View.VISIBLE);
                level_6_btn.startAnimation(scale_anim);
                break;

            case 7:
                level_1_btn.clearAnimation();
                level_1_btn.setVisibility(View.VISIBLE);
                level_2_btn.clearAnimation();
                level_2_btn.setVisibility(View.VISIBLE);
                level_3_btn.clearAnimation();
                level_3_btn.setVisibility(View.VISIBLE);
                level_4_btn.clearAnimation();
                level_4_btn.setVisibility(View.VISIBLE);
                level_5_btn.clearAnimation();
                level_5_btn.setVisibility(View.VISIBLE);
                level_6_btn.clearAnimation();
                level_6_btn.setVisibility(View.VISIBLE);

                level_7_btn.setVisibility(View.VISIBLE);
                level_7_btn.startAnimation(scale_anim);
                break;

            case 8:
                level_1_btn.clearAnimation();
                level_1_btn.setVisibility(View.VISIBLE);
                level_2_btn.clearAnimation();
                level_2_btn.setVisibility(View.VISIBLE);
                level_3_btn.clearAnimation();
                level_3_btn.setVisibility(View.VISIBLE);
                level_4_btn.clearAnimation();
                level_4_btn.setVisibility(View.VISIBLE);
                level_5_btn.clearAnimation();
                level_5_btn.setVisibility(View.VISIBLE);
                level_6_btn.clearAnimation();
                level_6_btn.setVisibility(View.VISIBLE);
                level_7_btn.clearAnimation();
                level_7_btn.setVisibility(View.VISIBLE);

                level_8_btn.setVisibility(View.VISIBLE);
                level_8_btn.startAnimation(scale_anim);
                break;

            case 9:
                level_1_btn.clearAnimation();
                level_1_btn.setVisibility(View.VISIBLE);
                level_2_btn.clearAnimation();
                level_2_btn.setVisibility(View.VISIBLE);
                level_3_btn.clearAnimation();
                level_3_btn.setVisibility(View.VISIBLE);
                level_4_btn.clearAnimation();
                level_4_btn.setVisibility(View.VISIBLE);
                level_5_btn.clearAnimation();
                level_5_btn.setVisibility(View.VISIBLE);
                level_6_btn.clearAnimation();
                level_6_btn.setVisibility(View.VISIBLE);
                level_7_btn.clearAnimation();
                level_7_btn.setVisibility(View.VISIBLE);
                level_8_btn.clearAnimation();
                level_8_btn.setVisibility(View.VISIBLE);

                level_9_btn.setVisibility(View.VISIBLE);
                level_9_btn.startAnimation(scale_anim);
                break;

            case 10:
                level_1_btn.clearAnimation();
                level_1_btn.setVisibility(View.VISIBLE);
                level_2_btn.clearAnimation();
                level_2_btn.setVisibility(View.VISIBLE);
                level_3_btn.clearAnimation();
                level_3_btn.setVisibility(View.VISIBLE);
                level_4_btn.clearAnimation();
                level_4_btn.setVisibility(View.VISIBLE);
                level_5_btn.clearAnimation();
                level_5_btn.setVisibility(View.VISIBLE);
                level_6_btn.clearAnimation();
                level_6_btn.setVisibility(View.VISIBLE);
                level_7_btn.clearAnimation();
                level_7_btn.setVisibility(View.VISIBLE);
                level_8_btn.clearAnimation();
                level_8_btn.setVisibility(View.VISIBLE);
                level_9_btn.clearAnimation();
                level_9_btn.setVisibility(View.VISIBLE);

                level_10_btn.setVisibility(View.VISIBLE);
                level_10_btn.startAnimation(scale_anim);
                break;
        }

        if (mServ != null)
        {
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
                music.setClass(Levels_class.this, MusicPlayer.class);
                startService(music);
                if (mServ != null)
                {
                    mServ.resumeMusic();
                }
            }
        }
        else {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }


    //Bind/Unbind music service
    private boolean mIsBound = false;
    private MusicPlayer mServ;
    private ServiceConnection Scon =new ServiceConnection()
    {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicPlayer.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name)
        {
            mServ = null;
        }
    };

    void doBindService()
    {
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
    protected void onPause()
    {
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
    protected void onDestroy()
    {
        super.onDestroy();

        //UNBIND music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicPlayer.class);
        stopService(music);

    }
}
