package com.codji.justkilltime;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;


public class ShoopActivity extends AppCompatActivity implements View.OnClickListener {

    public static float scaleYShop;
    public static float playerColor[] = new float[4];
    public static int direction = 1;
    public static Handler handler;
    final String TAG = "MyTag";

    private int money = 0;
    Button playerColorsBut, modesBut;
    ImageButton optionBut;
    TextView moneyText;
    SharedPreferences sPref;
    GLSurfaceView glSurfaceViewMoney;
    ViewPager viewPager;

    @Override
    protected void onStart(){
        super.onStart();

        moneyText = (TextView)findViewById(R.id.money);
        money = sPref.getInt("money", 0);
        moneyText.setText(money + "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoop_activity);

        handler = new Handler(){
            public void handleMessage (android.os.Message msg){
                switch (msg.what){
                    case 0:
                        playerColorsBut.setTextSize(25.0f);
                        playerColorsBut.setTextColor(getResources().getColor(R.color.colorAccent));

                        modesBut.setTextSize(20.0f);
                        modesBut.setTextColor(getResources().getColor(R.color.colorAccent40));
                        break;
                    case 1:
                        modesBut.setTextSize(25.0f);
                        modesBut.setTextColor(getResources().getColor(R.color.colorAccent));

                        playerColorsBut.setTextSize(20.0f);
                        playerColorsBut.setTextColor(getResources().getColor(R.color.colorAccent40));
                        break;
                }
            }
        };

        sPref = getSharedPreferences("Variables", 0);
        SharedPreferences.Editor ed = sPref.edit();
        if (sPref.getInt("color0", -1) == -1){ed.putInt("color0", 0);}
        if (sPref.getInt("color1", -1) == -1){ed.putInt("color1", 2);}
        if (sPref.getInt("color2", -1) == -1){ed.putInt("color2", 3);}
        if (sPref.getInt("color3", -1) == -1){ed.putInt("color3", 4);}
        if (sPref.getInt("deg0", -1) == -1){ed.putInt("deg0", 0);}
        if (sPref.getInt("deg90", -1) == -1){ed.putInt("deg90", 2);}
        if (sPref.getInt("deg180", -1) == -1){ed.putInt("deg180", 3);}
        if (sPref.getInt("deg270", -1) == -1){ed.putInt("deg270", 4);}
        if (sPref.getInt("deg45", -1) == -1){ed.putInt("deg45", 30);}
        if (sPref.getInt("deg315", -1) == -1){ed.putInt("deg315", 40);}
        ed.commit();

        playerColor[0] = sPref.getFloat("playerRed", 1.0f);
        playerColor[1] = sPref.getFloat("playerGreen", 1.0f);
        playerColor[2] = sPref.getFloat("playerBlue", 1.0f);
        playerColor[3] = sPref.getFloat("playerAlpha", 1.0f);

        optionBut = (ImageButton)findViewById(R.id.option);
        playerColorsBut = (Button)findViewById(R.id.playerColors);
        modesBut = (Button)findViewById(R.id.modes);

        optionBut.setOnClickListener(this);
        playerColorsBut.setOnClickListener(this);
        modesBut.setOnClickListener(this);

        modesBut.setTextColor(getResources().getColor(R.color.colorAccent40));
        modesBut.setTextSize(20.0f);

        final List<LinearLayout> list = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        list.add((LinearLayout)inflater.inflate(R.layout.colors_layout,null));
        list.add((LinearLayout)inflater.inflate(R.layout.modes_layout,null));

        viewPager = (ViewPager)findViewById(R.id.shoopViewPager);
        viewPager.setAdapter(new ShoopViewPagerAdapter(this, list, modesBut, playerColorsBut, viewPager));

        glSurfaceViewMoney = (GLSurfaceView)findViewById(R.id.glSurfaceViewMoney);
        scaleYShop = 9.0f;
        glSurfaceViewMoney.setRenderer(new MoneyOpenGLRenderer());
    }

    @Override
    public void onClick(View v) {
        sPref = getSharedPreferences("Variables", 0);
        SharedPreferences.Editor ed = sPref.edit();
        Button but = (Button)v;
        int cost;
        switch (v.getId()){
            case R.id.colorWhite:
                ed.putFloat("playerRed", 1.0f);
                ed.putFloat("playerGreen", 1.0f);
                ed.putFloat("playerBlue", 1.0f);
                ed.putFloat("playerAlpha", 1.0f);
                break;
            case R.id.colorGreen:
                cost = sPref.getInt("color1", -1);
                if (cost <= money && cost != -1){
                    if (cost > 0){
                        money -= cost;
                        moneyText.setText(money + "");
                        ed.putInt("color1", 0);
                        but.setText(but.getText().toString().split(" ")[0]);
                        but.setTextColor(getResources().getColor(R.color.colorAccent));
                        ed.putInt("goods", sPref.getInt("goods", 1) + 1);
                    }
                    ed.putFloat("playerRed", 0.26f);
                    ed.putFloat("playerGreen", 0.44f);
                    ed.putFloat("playerBlue", 0.11f);
                    ed.putFloat("playerAlpha", 1.0f);
                }
                break;
            case R.id.colorYellow:
                cost = sPref.getInt("color2", -1);
                if (cost <= money && cost != -1){
                    if (cost > 0){
                        money -= cost;
                        moneyText.setText(money + "");
                        ed.putInt("color2", 0);
                        but.setText(but.getText().toString().split(" ")[0]);
                        but.setTextColor(getResources().getColor(R.color.colorAccent));
                        ed.putInt("goods", sPref.getInt("goods", 1) + 1);
                    }
                    ed.putFloat("playerRed", 0.92f);
                    ed.putFloat("playerGreen", 0.86f);
                    ed.putFloat("playerBlue", 0.2f);
                    ed.putFloat("playerAlpha", 1.0f);
                }
                break;
            case R.id.colorPeach:
                cost = sPref.getInt("color3", -1);
                if (cost <= money && cost != -1){
                    if (cost > 0){
                        money -= cost;
                        moneyText.setText(money + "");
                        ed.putInt("color3", 0);
                        but.setText(but.getText().toString().split(" ")[0]);
                        but.setTextColor(getResources().getColor(R.color.colorAccent));
                        ed.putInt("goods", sPref.getInt("goods", 1) + 1);
                    }
                    ed.putFloat("playerRed", 1.0f);
                    ed.putFloat("playerGreen", 0.8f);
                    ed.putFloat("playerBlue", 0.6f);
                    ed.putFloat("playerAlpha", 1.0f);
                }
                break;
            case R.id.deg0:
                ed.putFloat("angleMap", 0.0f);
                break;
            case R.id.deg90:
                cost = sPref.getInt("deg90", -1);
                if (cost <= money && cost != -1){
                    if (cost > 0){
                        money -= cost;
                        moneyText.setText(money + "");
                        ed.putInt("deg90", 0);
                        but.setText(but.getText().toString().split(" ")[0]);
                        but.setTextColor(getResources().getColor(R.color.colorAccent));
                        ed.putInt("goods", sPref.getInt("goods", 1) + 1);
                    }
                    ed.putFloat("angleMap", 90.0f);
                }
                break;
            case R.id.deg180:
                cost = sPref.getInt("deg180", -1);
                if (cost <= money && cost != -1){
                    if (cost > 0){
                        money -= cost;
                        moneyText.setText(money + "");
                        ed.putInt("deg180", 0);
                        but.setText(but.getText().toString().split(" ")[0]);
                        but.setTextColor(getResources().getColor(R.color.colorAccent));
                        ed.putInt("goods", sPref.getInt("goods", 1) + 1);
                    }
                    ed.putFloat("angleMap", 180.0f);
                }
                break;
            case R.id.deg270:
                cost = sPref.getInt("deg270", -1);
                if (cost <= money && cost != -1){
                    if (cost > 0){
                        money -= cost;
                        moneyText.setText(money + "");
                        ed.putInt("deg270", 0);
                        but.setText(but.getText().toString().split(" ")[0]);
                        but.setTextColor(getResources().getColor(R.color.colorAccent));
                        ed.putInt("goods", sPref.getInt("goods", 1) + 1);
                    }
                    ed.putFloat("angleMap", 270.0f);
                }
                break;
            case R.id.modes:
                modesBut.setTextSize(25.0f);
                modesBut.setTextColor(getResources().getColor(R.color.colorAccent));

                playerColorsBut.setTextSize(20.0f);
                playerColorsBut.setTextColor(getResources().getColor(R.color.colorAccent40));

                viewPager.setCurrentItem(1);

                break;
            case R.id.playerColors:
                playerColorsBut.setTextSize(25.0f);
                playerColorsBut.setTextColor(getResources().getColor(R.color.colorAccent));

                modesBut.setTextSize(20.0f);
                modesBut.setTextColor(getResources().getColor(R.color.colorAccent40));

                viewPager.setCurrentItem(0);
                break;
        }
        ed.putInt("money", money);
        ed.commit();

        playerColor[0] = sPref.getFloat("playerRed", 1.0f);
        playerColor[1] = sPref.getFloat("playerGreen", 1.0f);
        playerColor[2] = sPref.getFloat("playerBlue", 1.0f);
        playerColor[3] = sPref.getFloat("playerAlpha", 1.0f);

    }

    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.sliderin_return, R.anim.sliderout_return);
    }
}
