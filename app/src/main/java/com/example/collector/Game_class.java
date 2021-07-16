package com.example.collector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PowerManager;

public class Game_class extends Activity {
    private FrameLayout gameFrame;
    private LinearLayout startLayout;
    private int frameHeight,frameWidth;

    HomeWatcher mHomeWatcher;

    //Images
    private ImageView stickBg,basket, worm, redApple;

    private int basketSize;

    //Sounds
    private  SoundPlayer sound;
    private boolean sound_flag = true;

    //Position
    private float basketX,basketY;
    private float wormX, wormY;
    private float redAppleX, redAppleY;

    //Score
    private TextView scoreLable;
    private int score;
    private final int current_level_req_score = 100;

    //Timer
    private Timer timer;
    private Handler handler = new Handler();

    //Countdown Timer
    private static final long startTime = 90000; // 2 minutes = 120000 miliseconds
    private TextView countDownText;
    private CountDownTimer countDownTimer;
    private long timeLeft;


    //Status
    private boolean start_flg = false;
    private boolean action_flg = false;

    Button start_btn;
    int musicCounter = 0;
    SharedPreferences sp;
    SharedPreferences loadToggleState;
    int instruction_counter = 0;
    LinearLayout instructions;
    LinearLayout instructions2;
    boolean first_game = true;
    ImageButton setting;
    private boolean levelsflag = false;
    private boolean not_from_settings = true;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

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

        gameFrame = findViewById(R.id.gameFrame);
        startLayout = findViewById(R.id.start_layout);
        stickBg= findViewById(R.id.stickBg);
        basket = findViewById(R.id.box);
        worm = findViewById(R.id.worm);
        redApple = findViewById(R.id.RedApple);
        scoreLable = findViewById(R.id.scoreLable);
        countDownText = findViewById(R.id.timerText);
        TextView req_score_tv = findViewById(R.id.req_score_tv);
        TextView req_score_up_tv = findViewById(R.id.req_score_up_tv);
        start_btn = findViewById(R.id.start_btn);

        String required_score = getResources().getString(R.string.required_score) + current_level_req_score;

        req_score_tv.setText(required_score);
        req_score_up_tv.setText(required_score);

        sound = new SoundPlayer(this);

        timeLeft = startTime;

        instructions = findViewById(R.id.instructions_layout);
        instructions2 = findViewById(R.id.instructions_layout2);

        sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        first_game = sp.getBoolean("first game", true);


        setting = findViewById(R.id.settings_btn_game);

        gameFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(instruction_counter < 2)
                {
                    if(instruction_counter == 0)
                    {
                        instructions.setVisibility(View.GONE);
                        instruction_counter = 1;
                        instructions2.setVisibility(View.VISIBLE);
                        ImageView catchFruit = findViewById(R.id.catch_Fruit);
                        Animation animCathFruit= AnimationUtils.loadAnimation(Game_class.this,R.anim.anim_yuval);
                        catchFruit.startAnimation(animCathFruit);
                    }else //if(instruction_counter == 1)
                    {
                        instructions2.setVisibility(View.GONE);
                        instruction_counter++;
                        start_flg = true;
                        first_game = false;

                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run()
                            {
                                if(start_flg)
                                {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            changePos();

                                        }
                                    });
                                }

                            }
                        }, 0, 20);

                        startTimer();
                    }
                }

                return false;
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

    //throw ftuits and worms
    public void changePos()
    {
        // Apple
        redAppleY += 12;

        float redAppleCenterX = redAppleX + redApple.getWidth() / 2;
        float redAppleCenterY = redAppleY + redApple.getHeight() / 2;

        if (hitCheck (redAppleCenterX, redAppleCenterY)) //if thouch the basket
        {
            if(sound_flag)
            {
                sound.playHitSound();
            }

            redAppleY = frameHeight + 100;
            score += 10;
        }

        if (redAppleY > frameHeight)
        {
            redAppleY = -100;
            redAppleX = (float) Math.floor(Math.random() * (frameWidth - redApple.getWidth()));
        }
        redApple.setX(redAppleX);
        redApple.setY(redAppleY);

        //worm
        wormY +=15;

        float wormCenterX= wormX + worm.getWidth() /2;
        float wormCenterY= wormY + worm.getHeight() /2;

        if(hitCheck(wormCenterX,wormCenterY))
        {
            if(sound_flag)
            {
                sound.playwormSound();
            }


            //worm made the screen smaller:
            wormY= frameHeight +100;
            score -=10;
            frameWidth = frameWidth * 90 /100;
            changeFrameWidth(frameWidth);

            //stop the game when catch worm:
            if (frameWidth < basketSize)
            {
                timer.cancel();
                timer=null;
                pauseTimer();

                if (score < current_level_req_score)
                {
                    lose();
                }
                else // if score >= current_level_req_score
                {
                    win();
                }


            }
        }

        if(wormY> frameHeight)
        {
            wormY=-100;
            wormX= (float) Math.floor(Math.random() * (frameWidth - worm.getWidth()));
        }
        worm.setX(wormX);
        worm.setY(wormY);

        // Move basket
        if(action_flg)
        {
            basketX += 14;
        } else
        {
            // Releasing
            basketX -= 14;
        }

        // Check basket position
        if (basketX < 0)
            basketX = 0;

        if (frameWidth - basketSize < basketX)
            basketX = frameWidth - basketSize;

        basket.setX(basketX);

        String score_txt;
        score_txt = getResources().getString(R.string.score) + score;
        scoreLable.setText(score_txt);
    }

    public void win()
    {
        final int CURRENT_LEVEL = 1;

        Intent scoreIntent_win = new Intent(Game_class.this, level_up_class.class);
        scoreIntent_win.putExtra("SCORE",score);
        scoreIntent_win.putExtra("played level", CURRENT_LEVEL);
        startActivity(scoreIntent_win);
        if(sound_flag)
        {
            sound.playWinSound();
        }

        Game_class.super.onBackPressed();
    }

    public void lose()
    {
        Intent scoreIntent_lose = new Intent(Game_class.this, CurrentScore_class.class);
        scoreIntent_lose.putExtra("SCORE",score);
        startActivity(scoreIntent_lose);
        if(sound_flag)
        {
            sound.playoverSound();
        }
        Game_class.super.onBackPressed();
    }

    public boolean hitCheck(float x, float y)
    {
        return basketX <= x && x <= basketX + basketSize && basketY <= y && y <= frameHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (start_flg)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                action_flg = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP)
            {
                action_flg = false;
            }
        }
        return true;

    }



    public void startGame (View view)
    {
        start_flg = true;
        startLayout.setVisibility(View.GONE);
        setting.setVisibility(View.VISIBLE);

        if(frameHeight == 0)
        {
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();

            basketSize = basket.getHeight();
            basketX = basket.getX();
            basketY = basket.getY();
        }

        basket.setX(0.0f);
        worm.setY(3000.0f);
        redApple.setY(3000.0f);

        wormY = worm.getY();
        redAppleY = redApple.getY();

        stickBg.setVisibility(View.VISIBLE);
        basket.setVisibility(View.VISIBLE);
        worm.setVisibility(View.VISIBLE);
        redApple.setVisibility(View.VISIBLE);

        //position timer
        if(!(first_game))
        {
            instruction_counter = 3;
            score = 0;
            scoreLable.setText(getResources().getString(R.string.score_st));

            Runnable delayGame = new Runnable() {
                @Override
                public void run() {
                    //downCounter clock
                    if(start_flg=true)
                        startTimer();

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run()
                        {
                            if(start_flg)
                            {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        changePos();

                                    }
                                });
                            }

                        }
                    }, 0, 20);

                }
            };
            Handler handler = new Handler();
            handler.postDelayed(delayGame, 500);
        }

        if(first_game)
        {
            //הוראות למשחק
            instructions.setVisibility(View.VISIBLE);
            ImageView tap=findViewById(R.id.tap);
            AnimationDrawable animationDrawable=(AnimationDrawable)tap.getDrawable();
            animationDrawable.start();
        }



    }




    private void startTimer()
    {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;

                if (((int)(timeLeft/1000)%60 == 0) && (timeLeft / 1000 / 60 == 0))
                {
                    pauseTimer();
                    timer.cancel();
                    timer=null;

                    if (score < current_level_req_score)
                    {
                        lose();
                    }
                    else // if score >= current_level_req_score
                    {
                        win();
                    }
                }
                updateTimerText();
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    private void pauseTimer(){
        countDownTimer.cancel();
    }

    private void updateTimerText(){ //to show the textClock
        int minutes=(int)timeLeft / 1000 / 60;
        int seconds=(int)(timeLeft/1000)%60;
        String timeLeftText = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        countDownText.setText(timeLeftText);
    }


    public void changeFrameWidth(int frameWidth)
    {
        ViewGroup.LayoutParams params= gameFrame.getLayoutParams();
        params.width=frameWidth;
        gameFrame.setLayoutParams(params);
    }

    @Override
    public void onBackPressed()
    {
        if(timer != null)
        {
            timer.cancel();
            timer = null;
            pauseTimer();
        }

        not_from_settings = true;
        exit();
    }

    public void exit()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Game_class.this);
        builder.setMessage(getResources().getString(R.string.exit_in_game_msg));
        builder.setCancelable(false);

        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(levelsflag)
                {
                    Intent intent_play = new Intent(Game_class.this,Levels_class.class);
                    startActivity(intent_play);
                }
                else
                {
                    Intent menuIntent = new Intent(Game_class.this, MainActivity.class);
                    startActivity(menuIntent);
                }


                Game_class.super.onBackPressed();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(not_from_settings)
                {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run()
                        {
                            if(start_flg)
                            {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        changePos();

                                    }
                                });
                            }

                        }
                    }, 0, 20);

                    startTimer();
                }

                dialog.cancel();
            }
        });

        AlertDialog exit_dialog = builder.create();
        exit_dialog.show();
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
                music.setClass(Game_class.this, MusicPlayer.class);
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

        if(loadToggleState.contains("sound"))
        {
            if(!(loadToggleState.getBoolean("sound", true)))
            {
                sound_flag = false;
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

        sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(sp.contains("first game"))
        {
            editor.remove("first game");
        }
        editor.putBoolean("first game", first_game);
        editor.commit();

        // save sounds settings
        SharedPreferences.Editor editor2 = loadToggleState.edit();

        if(loadToggleState.contains("sound")) {
            editor2.remove("sound");
        }

        if(loadToggleState.contains("music")) {
            editor2.remove("music");
        }

        editor2.putBoolean("sound", sound_flag);
        editor2.putInt("music", musicCounter);
        editor2.commit();
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



    public void setting (View view)
    {
        timer.cancel();
        timer = null;
        pauseTimer();

        final AlertDialog.Builder alertSetting = new AlertDialog.Builder(Game_class.this);
        View sview = getLayoutInflater().inflate(R.layout.activity_dialog_setting, null);

        final ImageButton bt_home=(ImageButton)sview.findViewById(R.id.bt_home);
        final ImageButton bt_play=(ImageButton)sview.findViewById(R.id.bt_play);
        final ImageButton bt_sound=(ImageButton)sview.findViewById(R.id.bt_sound);
        final ImageButton bt_music=(ImageButton)sview.findViewById(R.id.bt_music);
        final ImageButton bt_exit=(ImageButton)sview.findViewById(R.id.exit_bt);

        alertSetting.setView(sview);

        final AlertDialog alertDialogSetting = alertSetting.create();
        alertDialogSetting.setCanceledOnTouchOutside(false);

        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run()
                    {
                        if(start_flg)
                        {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    changePos();

                                }
                            });
                        }

                    }
                }, 0, 20);

                startTimer();
                alertDialogSetting.dismiss();
            }
        });

        bt_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                not_from_settings = false;
                levelsflag = false;
                exit();
            }
        });

        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                not_from_settings = false;
                levelsflag = true;
                exit();
            }
        });

        bt_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound_flag)
                {
                    bt_sound.setImageResource(R.drawable.no_music_bt);
                    sound_flag = false;
                }
                else
                {
                    bt_sound.setImageResource(R.drawable.music_bt);
                    sound_flag = true;
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
                    music.setClass(Game_class.this, MusicPlayer.class);
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

        alertDialogSetting.show();

        if(musicCounter == 0)
        {
            bt_music.setImageResource(R.drawable.sound_bt);
        }
        else
        {
            bt_music.setImageResource(R.drawable.no_sound_bt);
        }

        if(sound_flag)
        {
            bt_sound.setImageResource(R.drawable.music_bt);
        }
        else
        {
            bt_sound.setImageResource(R.drawable.no_music_bt);
        }

    }

}





