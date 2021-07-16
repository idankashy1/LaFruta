package com.example.collector;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.app.AlertDialog;
import android.os.PowerManager;

public class level_up_class extends Activity {

    SharedPreferences sp;
    HomeWatcher mHomeWatcher;
    int musicCounter = 0;
    int curr_lvl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_up);

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

        sp = getSharedPreferences("data", MODE_PRIVATE);
        curr_lvl = sp.getInt("level", 1);
        int played_lvl = getIntent().getIntExtra("played level", 1);

        if(played_lvl == curr_lvl)
        {
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("level");
            editor.putInt("level", played_lvl+1);
            editor.commit();
        }

        Button next_lvl_btn = findViewById(R.id.next_lvl_btn);
        next_lvl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent levels_Intent = new Intent(level_up_class.this, Levels_class.class);
                startActivity(levels_Intent);
                level_up_class.super.onBackPressed();
            }
        });


        Button to_menu_btn = findViewById(R.id.to_main_menu_btn);
        to_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuIntent= new Intent(level_up_class.this, MainActivity.class);
                startActivity(menuIntent);
                level_up_class.super.onBackPressed();
            }
        });


        Button saveBt = findViewById(R.id.saveBt);
        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref=getSharedPreferences("score_details",MODE_PRIVATE);
                SharedPreferences.Editor editor=pref.edit();
                editor.putInt("lastScore",score);
                editor.commit();


                final AlertDialog.Builder alert= new AlertDialog.Builder(level_up_class.this);
                View view=getLayoutInflater().inflate(R.layout.activity_dialog_score, null);

                final EditText name_input= (EditText)view.findViewById(R.id.name_edit);

                Button cancelBt=(Button)view.findViewById(R.id.cancle_bt);
                Button sendBt= (Button)view.findViewById(R.id.send_bt);

                alert.setView(view);

                final AlertDialog alertDialog= alert. create();
                alertDialog.setCanceledOnTouchOutside(false);

                cancelBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                sendBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String playerName=name_input.getText().toString();
                        alertDialog.dismiss();

                        SharedPreferences pref=getSharedPreferences("score_details",MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.putString("name_lastScore",playerName);
                        editor.commit();

                        Intent scoreTableIntent = new Intent(level_up_class.this, Scoretable_class.class);
                        scoreTableIntent.putExtra("CURRENT_INTENT",2);
                        startActivity(scoreTableIntent);
                    }
                });

                alertDialog.show();

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
    protected void onResume()
    {
        super.onResume();

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
                music.setClass(level_up_class.this, MusicPlayer.class);
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
