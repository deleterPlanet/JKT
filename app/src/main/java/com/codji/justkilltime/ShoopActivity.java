package com.codji.justkilltime;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
        if (sPref.getInt("color0", -1) == -1){ed.putInt("color0", 0);} // White
        if (sPref.getInt("color1", -1) == -1){ed.putInt("color1", 2);} // Green
        if (sPref.getInt("color2", -1) == -1){ed.putInt("color2", 3);} // Yellow
        if (sPref.getInt("color3", -1) == -1){ed.putInt("color3", 4);} // Peach
        if (sPref.getInt("color4", -1) == -1){ed.putInt("color4", 5);} // Blue
        if (sPref.getInt("color5", -1) == -1){ed.putInt("color5", 6);} // Crimson
        if (sPref.getInt("color6", -1) == -1){ed.putInt("color6", 7);} // Chocolate
        if (sPref.getInt("color7", -1) == -1){ed.putInt("color7", 8);} // Pistachio
        if (sPref.getInt("color8", -1) == -1){ed.putInt("color8", 9);} // Orange
        if (sPref.getInt("color9", -1) == -1){ed.putInt("color9", 10);} // Turquoise
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

        playerColorsBut = (Button)findViewById(R.id.playerColors);
        modesBut = (Button)findViewById(R.id.modes);

        playerColorsBut.setOnClickListener(this);
        modesBut.setOnClickListener(this);

        modesBut.setTextColor(getResources().getColor(R.color.colorAccent40));
        modesBut.setTextSize(20.0f);

        final List<LinearLayout> list = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        list.add((LinearLayout)inflater.inflate(R.layout.colors_layout,null));
        list.add((LinearLayout)inflater.inflate(R.layout.modes_layout,null));

        viewPager = (ViewPager)findViewById(R.id.shoopViewPager);
        viewPager.setAdapter(new ShoopViewPagerAdapter(this, list, modesBut, playerColorsBut, viewPager, sPref.getFloat("playerVertices", 30.0f)));

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
                    ed.putFloat("playerRed", 0.46f);
                    ed.putFloat("playerGreen", 0.68f);
                    ed.putFloat("playerBlue", 0.04f);
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
                    ed.putFloat("playerRed", 0.85f);
                    ed.putFloat("playerGreen", 0.87f);
                    ed.putFloat("playerBlue", 0.17f);
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
            case R.id.colorBlue:
                cost = sPref.getInt("color4", -1);
                if (cost <= money && cost != -1){
                    if (cost > 0){
                        money -= cost;
                        moneyText.setText(money + "");
                        ed.putInt("color4", 0);
                        but.setText(but.getText().toString().split(" ")[0]);
                        but.setTextColor(getResources().getColor(R.color.colorAccent));
                        ed.putInt("goods", sPref.getInt("goods", 1) + 1);
                    }
                    ed.putFloat("playerRed", 0.13f);
                    ed.putFloat("playerGreen", 0.61f);
                    ed.putFloat("playerBlue", 0.76f);
                    ed.putFloat("playerAlpha", 1.0f);
                }
                break;
            case R.id.colorCrimson:
                cost = sPref.getInt("color5", -1);
                if (cost <= money && cost != -1){
                    if (cost > 0){
                        money -= cost;
                        moneyText.setText(money + "");
                        ed.putInt("color5", 0);
                        but.setText(but.getText().toString().split(" ")[0]);
                        but.setTextColor(getResources().getColor(R.color.colorAccent));
                        ed.putInt("goods", sPref.getInt("goods", 1) + 1);
                    }
                    ed.putFloat("playerRed", 0.86f);
                    ed.putFloat("playerGreen", 0.08f);
                    ed.putFloat("playerBlue", 0.24f);
                    ed.putFloat("playerAlpha", 1.0f);
                }
                break;
            case R.id.colorChocolate:
                cost = sPref.getInt("color6", -1);
                if (cost <= money && cost != -1){
                    if (cost > 0){
                        money -= cost;
                        moneyText.setText(money + "");
                        ed.putInt("color6", 0);
                        but.setText(but.getText().toString().split(" ")[0]);
                        but.setTextColor(getResources().getColor(R.color.colorAccent));
                        ed.putInt("goods", sPref.getInt("goods", 1) + 1);
                    }
                    ed.putFloat("playerRed", 0.55f);
                    ed.putFloat("playerGreen", 0.36f);
                    ed.putFloat("playerBlue", 0.26f);
                    ed.putFloat("playerAlpha", 1.0f);
                }
                break;
            case R.id.colorPistachio:
                cost = sPref.getInt("color7", -1);
                if (cost <= money && cost != -1){
                    if (cost > 0){
                        money -= cost;
                        moneyText.setText(money + "");
                        ed.putInt("color7", 0);
                        but.setText(but.getText().toString().split(" ")[0]);
                        but.setTextColor(getResources().getColor(R.color.colorAccent));
                        ed.putInt("goods", sPref.getInt("goods", 1) + 1);
                    }
                    ed.putFloat("playerRed", 0.84f);
                    ed.putFloat("playerGreen", 0.96f);
                    ed.putFloat("playerBlue", 0.56f);
                    ed.putFloat("playerAlpha", 1.0f);
                }
                break;
            case R.id.colorOrange:
                cost = sPref.getInt("color8", -1);
                if (cost <= money && cost != -1){
                    if (cost > 0){
                        money -= cost;
                        moneyText.setText(money + "");
                        ed.putInt("color8", 0);
                        but.setText(but.getText().toString().split(" ")[0]);
                        but.setTextColor(getResources().getColor(R.color.colorAccent));
                        ed.putInt("goods", sPref.getInt("goods", 1) + 1);
                    }
                    ed.putFloat("playerRed", 0.92f);
                    ed.putFloat("playerGreen", 0.55f);
                    ed.putFloat("playerBlue", 0.21f);
                    ed.putFloat("playerAlpha", 1.0f);
                }
                break;
            case R.id.colorTurquoise:
                cost = sPref.getInt("color9", -1);
                if (cost <= money && cost != -1){
                    if (cost > 0){
                        money -= cost;
                        moneyText.setText(money + "");
                        ed.putInt("color9", 0);
                        but.setText(but.getText().toString().split(" ")[0]);
                        but.setTextColor(getResources().getColor(R.color.colorAccent));
                        ed.putInt("goods", sPref.getInt("goods", 1) + 1);
                    }
                    ed.putFloat("playerRed", 0.19f);
                    ed.putFloat("playerGreen", 0.84f);
                    ed.putFloat("playerBlue", 0.78f);
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
