package com.codji.justkilltime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class PlayActivity extends AppCompatActivity implements View.OnTouchListener {

    public static GLSurfaceView glSurfaceView;
    public static float scaleX, scaleY;
    public static Handler handler;
    public static int direction = 1;

    private int score = 0;

    RelativeLayout relLayout;
    final String TAG = "MyTag";
    TextView Score;
    SharedPreferences sPref;
    Date start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);

        start = new Date();

        Score = (TextView)findViewById(R.id.score);

        handler = new Handler(){
            public void handleMessage (android.os.Message msg){
                if (msg.what == MainActivity.DEATH){
                    Intent intent = new Intent();
                    intent.putExtra("score", score);
                    intent.putExtra("time", new Date().getTime() - start.getTime());
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.alpha_for_transition_in, R.anim.alpha_for_transition_out);
                }else{
                    Score.setText("" + msg.what);
                    score = msg.what;
                    if (score > MainActivity.higescore){
                        ((TextView)findViewById(R.id.newHigescore)).setText(getResources().getString(R.string.newHigescore));
                    }
                }
            }
        };

        relLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        relLayout.setOnTouchListener(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        scaleY = ((float)size.x)/size.y;
        scaleX = ((float)size.y)/size.x;

        sPref = getSharedPreferences("Variables", 0);

        setScorePos();

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

    void setScorePos(){
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

    @Override
    public void onBackPressed(){}
}
