package com.codji.justkilltime;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class PlayActivity extends AppCompatActivity implements View.OnTouchListener {

    public static GLSurfaceView glSurfaceView;
    public static float scaleX, scaleY;
    public static Handler handler, timerHandler, endGameHandler;
    public static int direction = 1;

    private int score = 0, extraLives;
    private boolean ISSecondChance = false;

    RelativeLayout relLayout;
    final String TAG = "MyTag";
    TextView Score;
    SharedPreferences sPref;
    Date start;
    ProgressBar progressTimer;
    RelativeLayout timer;
    Animation animStart, animEnd;
    AlertDialog.Builder builder;
    LayoutInflater inflater;
    Intent intent;
    GLSurfaceView newSurfaceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);
        sPref = getSharedPreferences("Variables", 0);

        extraLives = sPref.getInt("extraLives", 0);

        start = new Date();

        Score = (TextView)findViewById(R.id.score);

        newSurfaceView = new GLSurfaceView(this);

        animStart = AnimationUtils.loadAnimation(this, R.anim.progress_timer_start);
        animEnd = AnimationUtils.loadAnimation(this, R.anim.progress_timer_end);

        endGameHandler = new Handler(){
            public void handleMessage (android.os.Message msg){
                if (extraLives > 0 && !ISSecondChance) {
                    showDialog(1);
                    glSurfaceView.onPause();
                }else{
                    endGame();
                }
            }
        };

        timerHandler = new Handler(){
            public void handleMessage (android.os.Message msg){
                if(msg.what <= 3000){
                    if(timer.getVisibility() == View.GONE){
                        timer.setVisibility(View.VISIBLE);
                        timer.startAnimation(animStart);
                    }
                    progressTimer.setProgress(msg.what);
                }else{
                    if(timer.getVisibility() == View.VISIBLE){
                        timer.setVisibility(View.GONE);
                        timer.startAnimation(animEnd);
                    }
                }
            }
        };

        handler = new Handler(){
            public void handleMessage (android.os.Message msg){
                Score.setText("" + msg.what);
                score = msg.what;
                if (score > MainActivity.higescore){
                    ((TextView)findViewById(R.id.newHigescore)).setText(getResources().getString(R.string.newHigescore));
                }
            }
        };

        timer = findViewById(R.id.timer);
        progressTimer = findViewById(R.id.progressTimer);

        relLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        relLayout.setOnTouchListener(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        scaleY = ((float)size.x)/size.y;
        scaleX = ((float)size.y)/size.x;

        setInterfacePos();

        glSurfaceView = (GLSurfaceView)findViewById(R.id.glSurface);
        glSurfaceView.setRenderer(new OpenGLRenderer(new float[]{sPref.getFloat("playerRed", 1.0f),
                sPref.getFloat("playerGreen", 1.0f),
                sPref.getFloat("playerBlue", 1.0f),
                sPref.getFloat("playerAlpha", 1.0f)}, sPref.getFloat("angleMap", 0.0f)));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                direction = -direction;
                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt("clicks", sPref.getInt("clicks", 0) + 1);
                ed.commit();
                break;
        }
        return true;
    }

    void setInterfacePos(){
        LinearLayout scoreLin = findViewById(R.id.scoreText);
        float angleMap = sPref.getFloat("angleMap", 0.0f);
        switch ((int)angleMap){
            case 0:
                scoreLin.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                break;
            case 90:
                scoreLin.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                break;
            case 180:
                scoreLin.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                break;
            case 270:
                scoreLin.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                break;
        }

    }

    protected Dialog onCreateDialog(int id){
        builder = new AlertDialog.Builder(this);
        inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.second_chance, null);
        builder.setView(v);
        v.findViewById(R.id.positiveBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGame();
            }
        });
        v.findViewById(R.id.negativeBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPref.edit().putInt("extraLives", sPref.getInt("extraLives", 0) - 1).commit();
                ISSecondChance = true;
                dismissDialog(1);

                relLayout.removeView(findViewById(R.id.glSurface));
                newSurfaceView.setId(R.id.glSurface);
                newSurfaceView.setRenderer(new OpenGLRenderer(new float[]{sPref.getFloat("playerRed", 1.0f),
                        sPref.getFloat("playerGreen", 1.0f),
                        sPref.getFloat("playerBlue", 1.0f),
                        sPref.getFloat("playerAlpha", 1.0f)}, sPref.getFloat("angleMap", 0.0f)));
                relLayout.addView(newSurfaceView, 0);
            }
        });

        return builder.create();
    }

    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        ((TextView)(dialog).findViewById(R.id.extraLivesCount)).setText(sPref.getInt("extraLives", 0) + "");
        ObjectAnimator animator = ObjectAnimator.ofInt(dialog.findViewById(R.id.timerSecondChance), "progress", 0, 5000);

        animator.setDuration(5000);
        animator.start();
    }

    void endGame(){
        intent = new Intent();
        intent.putExtra("score", score);
        intent.putExtra("time", new Date().getTime() - start.getTime());
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.alpha_for_transition_in, R.anim.alpha_for_transition_out);
    }

    @Override
    public void onBackPressed(){}
}
