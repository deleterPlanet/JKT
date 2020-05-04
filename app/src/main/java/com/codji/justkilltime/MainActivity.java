package com.codji.justkilltime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    public static final int  REQ_COD_PLAY_ACTIVITY = 1,
                            REQ_COD_OPTION_ACTIVITY = 2;
    public static int higescore = 0;

    private int money = 0, score;
    private long time = 0;


    int missionId[] = new int[]{0, 1, 2, 3, 4, 5},
            NowMission[] = new int[]{missionId.length, missionId.length, missionId.length};
    RelativeLayout relLayout;
    ImageButton missionBut, shoopBut, statisticsBut, optionBut;
    TextView higescoreText, moneyText, tapToPlay;
    GLSurfaceView glSurfaceViewMoney;
    SharedPreferences sPref;
    Intent intent;
    SharedPreferences.Editor ed;

    @Override
    protected void onStart(){
        super.onStart();
        higescoreText = (TextView)findViewById(R.id.higescore);
        moneyText = (TextView)findViewById(R.id.money);

        money = sPref.getInt("money", 0);
        higescore = sPref.getInt("higescore", 0);

        higescoreText.setText(higescore + "");
        moneyText.setText(money + "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                startActivityForResult(intent, REQ_COD_PLAY_ACTIVITY);
                overridePendingTransition(R.anim.alpha_for_transition_in, R.anim.alpha_for_transition_out);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_COD_OPTION_ACTIVITY: setLanguage(); setContent(); break;
            case REQ_COD_PLAY_ACTIVITY:
                if (data == null){return;}
                ed = sPref.edit();

                time += data.getLongExtra("time", 0);

                score = data.getIntExtra("score", 0);
                money += score;
                moneyText.setText(money + "");

                checkMission(score, data.getLongExtra("time", 0));

                if (score > higescore){
                    higescore = score;
                    higescoreText.setText(score + "");
                    ed.putInt("higescore", score);
                }

                save();
                break;
        }
    }

    void checkMission(int score, long time){
        if (sPref.getInt("missionId0", -1) != -1){
            for (int i = 0; i < missionId.length; i++){
                missionId[i] = sPref.getInt("missionId" + i, -1);
            }
        }
        if (sPref.getInt("NowMission0", -1) != -1){
            for (int i = 0; i < NowMission.length; i++){
                NowMission[i] = sPref.getInt("NowMission" + i, -1);
            }
        }
        MissionActivity.position = sPref.getInt("position", 0);

        for (int i = 0; i < 3;i++){
            int mission = sPref.getInt("mission" + i, -1);
            int missionComplete = sPref.getInt("missionComplete", 0);
            boolean ISComplete = false;
            if (mission == -1){mission = addMission(i);};
            switch (mission){
                case 0:
                    ed.putInt("missionProgress" + i, sPref.getInt("missionProgress" + i, 0) + score);
                    if (sPref.getInt("missionProgress" + i, 0) + score >= sPref.getInt("N0", 0)){
                        ed.putInt("missionComplete", missionComplete + 1);
                        ed.putString("missionText" + i, "");
                        ISComplete = true;
                    }
                    break;
                case 1:
                    if (score >= sPref.getInt("N1", 0)){
                        ed.putInt("missionComplete", missionComplete + 1);
                        ed.putString("missionText" + i, "");
                        ISComplete = true;
                    }
                    break;
                case 2:
                    if (Math.floor(time/1000) >= sPref.getInt("N2", 0)){
                        ed.putInt("missionComplete", missionComplete + 1);
                        ed.putString("missionText" + i, "");
                        ISComplete = true;
                    }
                    break;
                case 3:
                    ed.putInt("missionProgress" + i, sPref.getInt("missionProgress" + i, 0) + (int)Math.floor(time/1000));
                    if (sPref.getInt("missionProgress" + i, 0) + (int)Math.floor(time/1000) >= sPref.getInt("N3", 0)){
                        ed.putInt("missionComplete", missionComplete + 1);
                        ed.putString("missionText" + i, "");
                        ISComplete = true;
                    }
                    break;
                case 4:
                    ed.putInt("missionProgress" + i, sPref.getInt("missionProgress" + i, 0) + 1);
                    if (sPref.getInt("missionProgress" + i, 0) + 1 >= sPref.getInt("N4", 0)){
                        ed.putInt("missionComplete", missionComplete + 1);
                        ed.putString("missionText" + i, "");
                        ISComplete = true;
                    }
                    break;
                case 5:
                    if (score > higescore){
                        ed.putInt("missionComplete", missionComplete + 1);
                        ed.putString("missionText" + i, "");
                        ISComplete = true;
                    }
                    break;
            }
            ed.commit();
            if (ISComplete){
                ed.putInt("NowMission" + i, missionId.length);
                if ((missionComplete+1)%3 == 0 && missionComplete != sPref.getInt("missionComplete", 0)){
                    ed.putInt("extraLives", sPref.getInt("extraLives", 0) + 1);
                }
            }
        }
    }

    int addMission(int pos){
        SharedPreferences.Editor ed = sPref.edit();
        String missions[] = getResources().getStringArray(R.array.missions);

        if(MissionActivity.position == 0){MissionActivity.spanNewRandArray(ed);}

        int randMis = MissionActivity.missionId[MissionActivity.position];
        while (randMis == NowMission[0] || randMis == NowMission[1] || randMis == NowMission[2]){
            MissionActivity.position = (MissionActivity.position == missionId.length-1)? 0 : MissionActivity.position + 1;
            if(MissionActivity.position == 0){MissionActivity.spanNewRandArray(ed);}
            randMis = missionId[MissionActivity.position];
        }

        int randN = sPref.getInt("N" + randMis, 0) + ((randMis == 0 || randMis == 3)? (int)Math.round((Math.random())) + 1 : 1);
        if (randMis < 5){
            String s[] = missions[randMis].split("/?");
            if (randN % 10 == 1 && (randN - randN % 10) / 10 % 10 != 1) {
                s[1] = s[1].replace("секунд", "секунду");
                s[1] = s[1].replace("очков", "очко");
            }
            if ((randN % 10 == 2 || randN % 10 == 3 || randN % 10 == 4) && (randN - randN % 10) / 10 % 10 != 1) {
                s[1] = s[1].replace("секунд", "секунды");
                s[1] = s[1].replace("очков", "очка");
                s[1] = s[1].replace("раз", "раза");
            }
            if(randN == 1){
                s[1] = s[1].replace("points", "point");
            }
            ed.putString("missionText" + pos, s[0] + randN + s[1]);
            ed.putInt("N" + randMis, randN);
        }else{
            ed.putString("missionText" + pos, missions[randMis]);
            ed.putString("missionText" + pos, missions[randMis]);
            ed.putInt("missionProgress" + pos, 0);
            ed.putInt("N" + randMis, 1);
        }
        NowMission[pos] = randMis;
        ed.putInt("NowMission" + pos, randMis);

        ed.putInt("mission" + pos, randMis);
        ed.commit();

        MissionActivity.position = (MissionActivity.position == MissionActivity.missionId.length-1)? 0 : MissionActivity.position + 1;
        return randMis;
    }

    void save(){
        ed.putInt("money", money);

        ed.putInt("play", sPref.getInt("play", 0) + 1);
        ed.putInt("points", sPref.getInt("points", 0) + score);
        ed.putLong("time", time);

        ed.commit();
    }

    void setLanguage(){
        if(!sPref.getBoolean("ISSetLocale", false)){return;}
        Locale locale = new Locale(sPref.getString("language", "en"));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
    }

    void setContent(){
        sPref = getSharedPreferences("Variables", 0);
        setLanguage();

        setContentView(R.layout.activity_main);
        time = sPref.getLong("time", 0);

        tapToPlay = (TextView)findViewById(R.id.tapToPlay);
        tapToPlay.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_tap_to_play));

        //обработка кнопок
        View.OnClickListener ocl_but = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.shoop:
                        intent = new Intent(MainActivity.this, ShoopActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.sliderin, R.anim.sliderout);
                        break;
                    case R.id.option:
                        intent = new Intent(MainActivity.this, OptionActivity.class);
                        startActivityForResult(intent, REQ_COD_OPTION_ACTIVITY);
                        overridePendingTransition(R.anim.slider_down, R.anim.null_anim);
                        break;
                    case R.id.mission:
                        intent = new Intent(MainActivity.this, MissionActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.sliderin_left, R.anim.sliderout_left);
                        break;
                    case R.id.statistics:
                        intent = new Intent(MainActivity.this, StatisticsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.sliderin_right, R.anim.sliderout_right);
                }
            }
        };

        missionBut = (ImageButton)findViewById(R.id.mission);
        shoopBut = (ImageButton)findViewById(R.id.shoop);
        statisticsBut = (ImageButton)findViewById(R.id.statistics);
        optionBut = (ImageButton)findViewById(R.id.option);

        missionBut.setOnClickListener(ocl_but);
        shoopBut.setOnClickListener(ocl_but);
        statisticsBut.setOnClickListener(ocl_but);
        optionBut.setOnClickListener(ocl_but);

        relLayout = (RelativeLayout)findViewById(R.id.mainRelativeLayout);
        relLayout.setOnTouchListener(this);

        glSurfaceViewMoney = (GLSurfaceView)findViewById(R.id.glSurfaceViewMoney);
        glSurfaceViewMoney.setRenderer(new MoneyOpenGLRenderer());
    }
}
