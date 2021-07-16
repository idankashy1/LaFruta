package com.example.collector;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

public class Scoretable_class extends Activity {

    Intent back_intent;

    @Override
    public void onBackPressed()
    {
        startActivity(back_intent);
        super.onBackPressed();
    }

    HomeWatcher mHomeWatcher;
    int lastScore, best1, best2, best3, best4, best5,best6, best7, best8, best9, best10, temp;
    String name_lastScore, name_best1, name_best2,name_best3,name_best4,name_best5, name_best6, name_best7,name_best8,name_best9,name_best10, name_temp;
    int previousIntent;
    TextView nameST,current1,current2,winner1,winner2,winner3,winner4,winner5,winner6,winner7,winner8,winner9,winner10;

    int musicCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoretable_activity);

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

        SharedPreferences pref=getSharedPreferences("score_details",MODE_PRIVATE);
        lastScore=pref.getInt("lastScore",0);
        name_lastScore=pref.getString("name_lastScore"," ");
        best1=pref.getInt("best1",0);
        name_best1=pref.getString("name_best1"," ");
        best2=pref.getInt("best2",0);
        name_best2=pref.getString("name_best2"," ");
        best3=pref.getInt("best3",0);
        name_best3=pref.getString("name_best3"," ");
        best4=pref.getInt("best4",0);
        name_best4=pref.getString("name_best4"," ");
        best5=pref.getInt("best5",0);
        name_best5=pref.getString("name_best5"," ");
        best6=pref.getInt("best6",0);
        name_best6=pref.getString("name_best6"," ");
        best7=pref.getInt("best7",0);
        name_best7=pref.getString("name_best7"," ");
        best8=pref.getInt("best8",0);
        name_best8=pref.getString("name_best8"," ");
        best9=pref.getInt("best9",0);
        name_best9=pref.getString("name_best9"," ");
        best10=pref.getInt("best10",0);
        name_best10=pref.getString("name_best10"," ");

        nameST = findViewById(R.id.name_st);
        current1 = findViewById(R.id.currentScore_st1);
        current2 = findViewById(R.id.currentScore_st2);

        winner1 = findViewById(R.id.winner1);
        winner2 = findViewById(R.id.winner2);
        winner3 = findViewById(R.id.winner3);
        winner4 = findViewById(R.id.winner4);
        winner5 = findViewById(R.id.winner5);
        winner6 = findViewById(R.id.winner6);
        winner7 = findViewById(R.id.winner7);
        winner8 = findViewById(R.id.winner8);
        winner9 = findViewById(R.id.winner9);
        winner10 = findViewById(R.id.winner10);



        previousIntent=getIntent().getIntExtra("CURRENT_INTENT",0);

        nameST.setText(name_lastScore + "");

        if(previousIntent==1){
            nameST.setVisibility(View.INVISIBLE);
            current1.setVisibility(View.INVISIBLE);
            current2.setVisibility(View.INVISIBLE);
        }


        if(previousIntent==2) {
            current2.setText(String.valueOf(lastScore));


            if (lastScore < best10)
            {
                Toast.makeText(Scoretable_class.this, R.string.sorry_st, Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (lastScore > best10) {
                    best10 = lastScore;
                    name_best10 = name_lastScore;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("best10", best10);
                    editor.putString("name_best10", name_best10);
                    editor.commit();
                }
                if (lastScore > best9) {
                    temp = best9;
                    name_temp = name_best9;
                    best9 = lastScore;
                    name_best9 = name_lastScore;
                    best10= temp;
                    name_best10 = name_temp;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("best9", best9);
                    editor.putInt("best10", best10);
                    editor.putString("name_best9", name_best9);
                    editor.putString("name_best10", name_best10);
                    editor.commit();
                }
                if (lastScore > best8) {
                    temp = best8;
                    name_temp = name_best8;
                    best8 = lastScore;
                    name_best8 = name_lastScore;
                    best9 = temp;
                    name_best9 = name_temp;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("best8", best8);
                    editor.putInt("best9", best9);
                    editor.putString("name_best8", name_best8);
                    editor.putString("name_best9", name_best9);
                    editor.commit();
                }
                if (lastScore > best7) {
                    temp = best7;
                    name_temp = name_best7;
                    best7 = lastScore;
                    name_best7 = name_lastScore;
                    best8 = temp;
                    name_best8 = name_temp;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("best7", best7);
                    editor.putInt("best8", best8);
                    editor.putString("name_best7", name_best7);
                    editor.putString("name_best8", name_best8);
                    editor.commit();
                }
                if (lastScore > best6) {
                    temp = best6;
                    name_temp = name_best6;
                    best6 = lastScore;
                    name_best6 = name_lastScore;
                    best7 = temp;
                    name_best7 = name_temp;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("best6", best6);
                    editor.putInt("best7", best7);
                    editor.putString("name_best6", name_best6);
                    editor.putString("name_best7", name_best7);
                    editor.commit();
                }
                if (lastScore > best5) {
                    temp = best5;
                    name_temp = name_best5;
                    best5 = lastScore;
                    name_best5 = name_lastScore;
                    best6 = temp;
                    name_best6 = name_temp;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("best5", best5);
                    editor.putInt("best6", best6);
                    editor.putString("name_best5", name_best5);
                    editor.putString("name_best6", name_best6);
                    editor.commit();
                }
                if (lastScore > best4) {
                    temp = best4;
                    name_temp = name_best4;
                    best4 = lastScore;
                    name_best4 = name_lastScore;
                    best5 = temp;
                    name_best5 = name_temp;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("best4", best4);
                    editor.putInt("best5", best5);
                    editor.putString("name_best4", name_best4);
                    editor.putString("name_best5", name_best5);
                    editor.commit();
                }
                if (lastScore > best3) {
                    temp = best3;
                    name_temp = name_best3;
                    best3 = lastScore;
                    name_best3 = name_lastScore;
                    best4 = temp;
                    name_best4 = name_temp;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("best3", best3);
                    editor.putInt("best4", best4);
                    editor.putString("name_best3", name_best3);
                    editor.putString("name_best4", name_best4);
                    editor.commit();
                }
                if (lastScore > best2) {
                    temp = best2;
                    name_temp = name_best2;
                    best2 = lastScore;
                    name_best2 = name_lastScore;
                    best3 = temp;
                    name_best3 = name_temp;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("best2", best2);
                    editor.putInt("best3", best3);
                    editor.putString("name_best2", name_best2);
                    editor.putString("name_best3", name_best3);
                    editor.commit();
                }
                if (lastScore > best1) {
                    temp = best1;
                    name_temp = name_best1;
                    best1 = lastScore;
                    name_best1 = name_lastScore;
                    best2 = temp;
                    name_best2 = name_temp;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("best1", best1);
                    editor.putInt("best2", best2);
                    editor.putString("name_best1", name_best1);
                    editor.putString("name_best2", name_best2);
                    editor.commit();
                }

            }
        }

        if(best1==0)
            winner1.setText(" ");
        else
            winner1.setText(name_best1+ ": " + best1 + "");
        if(best2==0)
            winner2.setText(" ");
        else
            winner2.setText(name_best2+ ": " + best2 + "");
        if(best3==0)
            winner3.setText(" ");
        else
            winner3.setText(name_best3+ ": " + best3 + "");
        if(best4==0)
            winner4.setText(" ");
        else
            winner4.setText(name_best4+ ": " + best4 + "");
        if(best5==0)
            winner5.setText(" ");
        else
            winner5.setText(name_best5+ ": " + best5 + "");
        if(best6==0)
            winner6.setText(" ");
        else
            winner6.setText(name_best6+ ": " + best6 + "");
        if(best7==0)
            winner7.setText(" ");
        else
            winner7.setText(name_best7+ ": " + best7 + "");
        if(best8==0)
            winner8.setText(" ");
        else
            winner8.setText(name_best8+ ": " + best8 + "");
        if(best9==0)
            winner9.setText(" ");
        else
            winner9.setText(name_best9+ ": " + best9 + "");
        if(best10==0)
            winner10.setText(" ");
        else
            winner10.setText(name_best10+ ": " + best10 + "");




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

        back_intent = new Intent(Scoretable_class.this, MainActivity.class);

        ImageButton home_bt = findViewById(R.id.bt_home);
        home_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(back_intent);
            }
        });
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
                music.setClass(Scoretable_class.this, MusicPlayer.class);
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



