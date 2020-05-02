package com.codji.justkilltime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;


public class MissionActivity extends AppCompatActivity implements View.OnClickListener{

    public static int position = 0;
    public static int missionId[] = new int[]{0, 1, 2, 3, 4, 5};

    int NowMission[] = new int[]{missionId.length, missionId.length, missionId.length};
    final String TAG = "MyTag";
    int randMis, randN, progress, extraLivesCount, defCount = 1;
    String missions[] = new String[]{};
    TextView mission0, mission1, mission2;
    ImageView imgs[];
    SharedPreferences sPref;
    SharedPreferences.Editor ed;
    ProgressBar progressBar;
    TextView extraLives;
    ProgressBar progressMissions[];
    TextView progressValues[];
    ImageButton rewards[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sPref = getSharedPreferences("Variables", 0);
        ed = sPref.edit();
        setLanguage();

        setContentView(R.layout.activity_mission);

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

        extraLivesCount = sPref.getInt("extraLives", 0);

        extraLives = findViewById(R.id.missionExtraLivesCount);
        extraLives.setText(extraLivesCount + "");

        progressBar = findViewById(R.id.progress);
        progress = sPref.getInt("missionComplete", 0);
        progressBar.setProgress(progress%3);

        progressMissions = new ProgressBar[3];
        progressMissions[0] = findViewById(R.id.missionProgress0);
        progressMissions[1] = findViewById(R.id.missionProgress1);
        progressMissions[2] = findViewById(R.id.missionProgress2);

        imgs = new ImageView[3];
        imgs[0] = findViewById(R.id.missionImg0);
        imgs[1] = findViewById(R.id.missionImg1);
        imgs[2] = findViewById(R.id.missionImg2);

        progressValues = new TextView[3];
        progressValues[0] = findViewById(R.id.valueProgress0);
        progressValues[1] = findViewById(R.id.valueProgress1);
        progressValues[2] = findViewById(R.id.valueProgress2);

        missions = getResources().getStringArray(R.array.missions);

        mission0 = findViewById(R.id.mission0);
        mission1 = findViewById(R.id.mission1);
        mission2 = findViewById(R.id.mission2);

        position = sPref.getInt("position", 0);

        setMission(mission0, 0);
        setMission(mission1, 1);
        setMission(mission2, 2);

        setReward();

        ed.putInt("position", position);
        ed.commit();
    }

    @Override
    public void onClick(View v) {
        int count;
        switch (v.getId()){
            case R.id.playerVertices6:
                count = sPref.getInt("playerVertices0", defCount);
                if (count <= extraLivesCount){
                    if (count != 0){
                        extraLivesCount -= count;
                        extraLives.setText(extraLivesCount + "");
                        ed.putInt("playerVertices0", 0);
                    }
                    ed.putFloat("playerVertices", 6);

                    int a = sPref.getInt("nowPlayerVertices", -1);
                    if (a != -1){
                        rewards[a].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_black95));
                        if (a == 0){
                            ed.remove("nowPlayerVertices");
                            ed.putFloat("playerVertices", 30);
                            break;
                        }
                    }
                    rewards[0].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_green));
                    ed.putInt("nowPlayerVertices", 0);
                }
                break;
            case R.id.playerVertices8:
                count = sPref.getInt("playerVertices1", defCount);
                if (count <= extraLivesCount){
                    if (count != 0){
                        extraLivesCount -= count;
                        extraLives.setText(extraLivesCount + "");
                        ed.putInt("playerVertices1", 0);
                    }
                    ed.putFloat("playerVertices", 8);

                    int a = sPref.getInt("nowPlayerVertices", -1);
                    if (a != -1){
                        rewards[a].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_black95));
                        if (a == 1){
                            ed.remove("nowPlayerVertices");
                            ed.putFloat("playerVertices", 30);
                            break;
                        }
                    }
                    rewards[1].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_green));
                    ed.putInt("nowPlayerVertices", 1);
                }
                break;
            case R.id.playerVertices12:
                count = sPref.getInt("playerVertices2", defCount);
                if (count <= extraLivesCount){
                    if (count != 0){
                        extraLivesCount -= count;
                        extraLives.setText(extraLivesCount + "");
                        ed.putInt("playerVertices2", 0);
                    }
                    ed.putFloat("playerVertices", 12);

                    int a = sPref.getInt("nowPlayerVertices", -1);
                    if (a != -1){
                        rewards[a].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_black95));
                        if (a == 2){
                            ed.remove("nowPlayerVertices");
                            ed.putFloat("playerVertices", 30);
                            break;
                        }
                    }
                    rewards[2].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_green));
                    ed.putInt("nowPlayerVertices", 2);
                }
                break;

            case R.id.trianglesColor0:
                count = sPref.getInt("trianglesColor0", defCount);
                if (count <= extraLivesCount){
                    if (count != 0){
                        extraLivesCount -= count;
                        extraLives.setText(extraLivesCount + "");
                        ed.putInt("trianglesColor0", 0);
                    }
                    ed.putFloat("trianglesColorRed", 0.59f);
                    ed.putFloat("trianglesColorGreen", 0.72f);
                    ed.putFloat("trianglesColorBlue", 0.85f);
                    ed.putFloat("trianglesColorAlpha", 1.0f);

                    int a = sPref.getInt("nowTrianglesColor", -1);
                    if (a != -1){
                        rewards[a + 3].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_black95));
                        if (a == 0){
                            ed.remove("nowTrianglesColor");
                            ed.putFloat("trianglesColorRed", 0.6f);
                            ed.putFloat("trianglesColorGreen", 0.6f);
                            ed.putFloat("trianglesColorBlue", 0.6f);
                            ed.putFloat("trianglesColorAlpha", 1.0f);
                            break;
                        }
                    }
                    rewards[3].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_green));
                    ed.putInt("nowTrianglesColor", 0);
                }
                break;
            case R.id.trianglesColor1:
                count = sPref.getInt("trianglesColor1", defCount);
                if (count <= extraLivesCount){
                    if (count != 0){
                        extraLivesCount -= count;
                        extraLives.setText(extraLivesCount + "");
                        ed.putInt("trianglesColor1", 0);
                    }
                    ed.putFloat("trianglesColorRed", 0.95f);
                    ed.putFloat("trianglesColorGreen", 0.94f);
                    ed.putFloat("trianglesColorBlue", 0.85f);
                    ed.putFloat("trianglesColorAlpha", 1.0f);

                    int a = sPref.getInt("nowTrianglesColor", -1);
                    if (a != -1){
                        rewards[a + 3].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_black95));
                        if (a == 1){
                            ed.remove("nowTrianglesColor");
                            ed.putFloat("trianglesColorRed", 0.6f);
                            ed.putFloat("trianglesColorGreen", 0.6f);
                            ed.putFloat("trianglesColorBlue", 0.6f);
                            ed.putFloat("trianglesColorAlpha", 1.0f);
                            break;
                        }
                    }
                    rewards[4].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_green));
                    ed.putInt("nowTrianglesColor", 1);
                }
                break;
            case R.id.trianglesColor2:
                count = sPref.getInt("trianglesColor2", defCount);
                if (count <= extraLivesCount){
                    if (count != 0){
                        extraLivesCount -= count;
                        extraLives.setText(extraLivesCount + "");
                        ed.putInt("trianglesColor2", 0);
                    }
                    ed.putFloat("trianglesColorRed", 0.91f);
                    ed.putFloat("trianglesColorGreen", 0.81f);
                    ed.putFloat("trianglesColorBlue", 0.53f);
                    ed.putFloat("trianglesColorAlpha", 1.0f);

                    int a = sPref.getInt("nowTrianglesColor", -1);
                    if (a != -1){
                        rewards[a + 3].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_black95));
                        if (a == 2){
                            ed.remove("nowTrianglesColor");
                            ed.putFloat("trianglesColorRed", 0.6f);
                            ed.putFloat("trianglesColorGreen", 0.6f);
                            ed.putFloat("trianglesColorBlue", 0.6f);
                            ed.putFloat("trianglesColorAlpha", 1.0f);
                            break;
                        }
                    }
                    rewards[5].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_green));
                    ed.putInt("nowTrianglesColor", 2);
                }
                break;
            case R.id.trianglesColor3:
                count = sPref.getInt("trianglesColor3", defCount);
                if (count <= extraLivesCount){
                    if (count != 0){
                        extraLivesCount -= count;
                        extraLives.setText(extraLivesCount + "");
                        ed.putInt("trianglesColor3", 0);
                    }
                    ed.putFloat("trianglesColorRed", 0.14f);
                    ed.putFloat("trianglesColorGreen", 0.37f);
                    ed.putFloat("trianglesColorBlue", 0.51f);
                    ed.putFloat("trianglesColorAlpha", 1.0f);

                    int a = sPref.getInt("nowTrianglesColor", -1);
                    if (a != -1){
                        rewards[a + 3].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_black95));
                        if (a == 3){
                            ed.remove("nowTrianglesColor");
                            ed.putFloat("trianglesColorRed", 0.6f);
                            ed.putFloat("trianglesColorGreen", 0.6f);
                            ed.putFloat("trianglesColorBlue", 0.6f);
                            ed.putFloat("trianglesColorAlpha", 1.0f);
                            break;
                        }
                    }
                    rewards[6].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_green));
                    ed.putInt("nowTrianglesColor", 3);
                }
                break;
            case R.id.trianglesColor4:
                count = sPref.getInt("trianglesColor4", defCount);
                if (count <= extraLivesCount){
                    if (count != 0){
                        extraLivesCount -= count;
                        extraLives.setText(extraLivesCount + "");
                        ed.putInt("trianglesColor4", 0);
                    }
                    ed.putFloat("trianglesColorRed", 0.7f);
                    ed.putFloat("trianglesColorGreen", 0.48f);
                    ed.putFloat("trianglesColorBlue", 0.39f);
                    ed.putFloat("trianglesColorAlpha", 1.0f);

                    int a = sPref.getInt("nowTrianglesColor", -1);
                    if (a != -1){
                        rewards[a + 3].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_black95));
                        if (a == 4){
                            ed.remove("nowTrianglesColor");
                            ed.putFloat("trianglesColorRed", 0.6f);
                            ed.putFloat("trianglesColorGreen", 0.6f);
                            ed.putFloat("trianglesColorBlue", 0.6f);
                            ed.putFloat("trianglesColorAlpha", 1.0f);
                            break;
                        }
                    }
                    rewards[7].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_green));
                    ed.putInt("nowTrianglesColor", 4);
                }
                break;
            case R.id.trianglesColor5:
                count = sPref.getInt("trianglesColor5", defCount);
                if (count <= extraLivesCount){
                    if (count != 0){
                        extraLivesCount -= count;
                        extraLives.setText(extraLivesCount + "");
                        ed.putInt("trianglesColor5", 0);
                    }
                    ed.putFloat("trianglesColorRed", 0.23f);
                    ed.putFloat("trianglesColorGreen", 0.9f);
                    ed.putFloat("trianglesColorBlue", 0.79f);
                    ed.putFloat("trianglesColorAlpha", 1.0f);

                    int a = sPref.getInt("nowTrianglesColor", -1);
                    if (a != -1){
                        rewards[a + 3].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_black95));
                        if (a == 5){
                            ed.remove("nowTrianglesColor");
                            ed.putFloat("trianglesColorRed", 0.6f);
                            ed.putFloat("trianglesColorGreen", 0.6f);
                            ed.putFloat("trianglesColorBlue", 0.6f);
                            ed.putFloat("trianglesColorAlpha", 1.0f);
                            break;
                        }
                    }
                    rewards[8].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_green));
                    ed.putInt("nowTrianglesColor", 5);
                }
                break;
        }
        ed.putInt("extraLives", extraLivesCount);
        ed.commit();
    }

    void setMission(TextView missionText, int pos){
        String m = sPref.getString("missionText" + pos, "");
        if (m != ""){
            missionText.setText(m);
            setImgs(pos);
            setMissionProgress(pos);
            return;
        }

        if(position == 0){
            spanNewRandArray(ed);
            for (int i = 0; i < missionId.length;i++) {
                ed.putInt("missionId" + i, missionId[i]);
            }
            ed.commit();
        }
        Log.d("MyTag", missionId[0]+" "+missionId[1]+" "+missionId[2]+" "+missionId[3]+" "+missionId[4]+" "+missionId[5]+" ");

        randMis = missionId[position];
        while (randMis == NowMission[0] || randMis == NowMission[1] || randMis == NowMission[2]){
            Log.d("MyTag", randMis+"=="+NowMission[0]+"||"+randMis+"=="+NowMission[1]+"||"+randMis+"=="+NowMission[2]+" : "+position);
            position = (position == missionId.length-1)? 0 : position + 1;
            if(position == 0){spanNewRandArray(ed);}
            randMis = missionId[position];
        }
        randN = sPref.getInt("N" + randMis, 0) + ((randMis == 0 || randMis == 3)? (int)Math.round((Math.random())) + 1 : 1);
        if (randMis < 5){
            String s[] = missions[randMis].split("n");
            if(randN%10 == 1 && (randN-randN%10)/10%10 != 1){
                s[1] = s[1].replace("секунд", "секунду");
                s[1] = s[1].replace("очков", "очко");
            }
            if((randN%10 == 2 || randN%10 == 3 || randN%10 == 4) && (randN-randN%10)/10%10 != 1){
                s[1] = s[1].replace("секунд", "секунды");
                s[1] = s[1].replace("очков", "очка");
                s[1] = s[1].replace("раз", "раза");
            }
            missionText.setText(s[0] + randN + s[1]);
            ed.putString("missionText" + pos, s[0] + randN + s[1]);
            ed.putInt("missionProgress" + pos, 0);
            ed.putInt("N" + randMis, randN);
        }else{
            missionText.setText(missions[randMis]);
            ed.putString("missionText" + pos, missions[randMis]);
            ed.putInt("missionProgress" + pos, 0);
            ed.putInt("N" + randMis, 1);
        }

        NowMission[pos] = randMis;
        ed.putInt("NowMission" + pos, randMis);

        ed.putInt("mission" + pos, randMis);
        ed.commit();
        setImgs(pos);
        setMissionProgress(pos);
        position = (position == missionId.length-1)? 0 : position + 1;
    }

    public static void spanNewRandArray(SharedPreferences.Editor ed){
        Random rnd = new Random();
        for(int i = 0; i < missionId.length; i++) {
            int index = rnd.nextInt(i + 1);
            int a = missionId[index];
            missionId[index] = missionId[i];
            missionId[i] = a;
        }
    }

    void setImgs(int pos){
        int resource = 0;
        if (sPref.getInt("mission" + pos, 0) == 0 || sPref.getInt("mission" + pos, 0) == 1){resource = R.drawable.triangle;}
        if (sPref.getInt("mission" + pos, 0) == 2 || sPref.getInt("mission" + pos, 0) == 3){resource = R.drawable.clook;}
        if (sPref.getInt("mission" + pos, 0) == 4){resource = R.drawable.play;}
        if (sPref.getInt("mission" + pos, 0) == 5){resource = R.drawable.medal;}
        imgs[pos].setImageResource(resource);
    }

    void setMissionProgress(int pos){
        int setProgress = sPref.getInt("missionProgress" + pos, 0);
        progressMissions[pos].setProgress(setProgress);
        progressMissions[pos].setMax(sPref.getInt("N" + sPref.getInt("mission" + pos, 0), 0));

        progressValues[pos].setText("" + setProgress);
        progressValues[pos].setPadding(0, 0, 120 - ((setProgress + "").length() - 1)*15, 0);
    }

    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.sliderin_right, R.anim.sliderout_right);
    }

    void setReward(){
        rewards = new ImageButton[9];
        rewards[0] = findViewById(R.id.playerVertices6);
        rewards[1] = findViewById(R.id.playerVertices8);
        rewards[2] = findViewById(R.id.playerVertices12);
        rewards[3] = findViewById(R.id.trianglesColor0);
        rewards[4] = findViewById(R.id.trianglesColor1);
        rewards[5] = findViewById(R.id.trianglesColor2);
        rewards[6] = findViewById(R.id.trianglesColor3);
        rewards[7] = findViewById(R.id.trianglesColor4);
        rewards[8] = findViewById(R.id.trianglesColor5);

        for (int i = 0; i < rewards.length; i++){
            String s = (i < 3)? "playerVertices" + i : "trianglesColor" + (i - 3);

            if (sPref.getInt(s, defCount) == defCount){
                rewards[i].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_red));
            }
            rewards[i].setOnClickListener(this);
        }

        if (sPref.getInt("nowPlayerVertices", -1) != -1){
            rewards[sPref.getInt("nowPlayerVertices", -1)].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_green));
        }

        if (sPref.getInt("nowTrianglesColor", -1) != -1){
            rewards[sPref.getInt("nowTrianglesColor", -1) + 3].setBackground(getResources().getDrawable(R.drawable.style_btn_stroke_green));
        }
    }

    void setLanguage(){
        Locale locale = new Locale(sPref.getString("language", "en"));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
    }
}
