package com.example.collector;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


public class MainActivity extends Activity {

    HomeWatcher mHomeWatcher;
    SharedPreferences sp;
    int musicCounter = 0;
    public boolean sounds_flag = false;
    SharedPreferences loadToggleState;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadToggleState = getSharedPreferences("data", Context.MODE_PRIVATE);
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

        if(!(loadToggleState.contains("sound")))
        {
            sounds_flag = true;
        } else
        {
            sounds_flag = loadToggleState.getBoolean("sound", true);
        }

        sp = getSharedPreferences("data", MODE_PRIVATE);

        Button play_btn = findViewById(R.id.play_bt);
        play_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent play_intent = new Intent(MainActivity.this, Levels_class.class);
                startActivity(play_intent);
            }
        });

        Button quit_btn = findViewById(R.id.quit_bt);
        quit_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                exit();
            }
        });

        if(!(sp.contains("level")))
        {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("level",1);
            editor.commit();
        }

        Button score_btn=findViewById(R.id.score_bt);
        score_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scoreTableIntent = new Intent(MainActivity.this, Scoretable_class.class);
                scoreTableIntent.putExtra("CURRENT_INTENT",1);
                startActivity(scoreTableIntent);
            }
        });

        final ImageButton setting = findViewById(R.id.settings_btn);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertSetting = new AlertDialog.Builder(MainActivity.this);

                final Dialog alertDialog = new Dialog(MainActivity.this);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //alertDialog.setContentView(R.layout.tabs);
                alertDialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
                alertDialog.show();


                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_dialog_setting, (RelativeLayout)findViewById(R.id.layout));

                alertDialog.setContentView(view);

                final ImageButton bt_home = view.findViewById(R.id.bt_home);
                final ImageButton bt_play = view.findViewById(R.id.bt_play);
                final ImageButton bt_sound = view.findViewById(R.id.bt_sound);
                final ImageButton bt_music = view.findViewById(R.id.bt_music);
                final ImageButton bt_exit = view.findViewById(R.id.exit_bt);

                //alertSetting.setView(view);

                //final AlertDialog alertDialogSetting = alertSetting.create();
                //alertDialogSetting.setCanceledOnTouchOutside(true);

                bt_home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent_home=new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent_home);
                        alertDialog.cancel();
                    }
                });

                bt_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //alertDialogSetting.cancel();
                        alertDialog.cancel();
                    }
                });

                bt_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent_play=new Intent(MainActivity.this,Levels_class.class);
                        startActivity(intent_play);
                    }
                });

                bt_sound.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(sounds_flag)
                        {
                            bt_sound.setImageResource(R.drawable.no_music_bt);
                            sounds_flag = false;
                        }
                        else
                        {
                            bt_sound.setImageResource(R.drawable.music_bt);
                            sounds_flag = true;
                        }
                    }
                });

                bt_music.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (musicCounter > 0)
                        {
                            doBindService();
                            Intent music = new Intent();
                            music.setClass(MainActivity.this, MusicPlayer.class);
                            startService(music);
                            musicCounter = 0;
                            if (mServ != null) {
                                mServ.resumeMusic();
                            }
                            bt_music.setImageResource(R.drawable.sound_bt);
                        } else
                        {
                            musicCounter = 1;
                            if (mServ != null) {
                                mServ.pauseMusic();
                            }
                            bt_music.setImageResource(R.drawable.no_sound_bt);
                        }
                        SharedPreferences saveMusic = getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorMusic = saveMusic.edit();
                        editorMusic.putInt("music", musicCounter);
                        editorMusic.commit();
                    }
                });

                //alertDialogSetting.show();
                alertDialog.show();

                if(musicCounter == 0)
                {
                    bt_music.setImageResource(R.drawable.sound_bt);
                }
                else
                {
                    bt_music.setImageResource(R.drawable.no_sound_bt);
                }

                if(sounds_flag)
                {
                    bt_sound.setImageResource(R.drawable.music_bt);
                }
                else
                {
                    bt_sound.setImageResource(R.drawable.no_music_bt);
                }

            }
        });

        if(!(sp.contains("first game")))
        {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first game", true);
            editor.commit();
        }


        //Stops the music when pressing home btn
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
    public void onBackPressed()
    {
        exit();
    }

    public void exit()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getResources().getString(R.string.exit_msg));
        builder.setCancelable(true);

        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                MainActivity.super.onBackPressed();
            }
        });



        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        AlertDialog exit_dialog = builder.create();
        exit_dialog.show();
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

        // save sounds settings
        SharedPreferences.Editor editor = loadToggleState.edit();

        if(loadToggleState.contains("sound")) {
            editor.remove("sound");
        }

        if(loadToggleState.contains("music")) {
            editor.remove("music");
        }

        editor.putBoolean("sound", sounds_flag);
        editor.putInt("music", musicCounter);
        editor.commit();
    }

    @Override
    protected void onResume()
    {
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
                music.setClass(MainActivity.this, MusicPlayer.class);
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
    protected void onDestroy()
    {
        super.onDestroy();
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicPlayer.class);
        stopService(music);
    }


    //Music
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
                Scon, Context.BIND_AUTO_CREATE);
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
}


