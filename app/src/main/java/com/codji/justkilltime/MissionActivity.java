package com.codji.justkilltime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;


public class MissionActivity extends AppCompatActivity implements View.OnClickListener{

    public static int position = 0;
    public static int missionId[] = new int[]{0, 1, 2, 3, 4, 5};

    int NowMission[] = new int[]{missionId.length, missionId.length, missionId.length};
    final String TAG = "MyTag";
    int randMis, randN, progress;
    String missions[] = new String[]{};
    TextView mission0, mission1, mission2;
    ImageView imgs[];
    SharedPreferences sPref;
    SharedPreferences.Editor ed;
    ProgressBar progressBar;
    ProgressBar progressMissions[];
    TextView progressValues[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        sPref = getSharedPreferences("Variables", 0);
        ed = sPref.edit();
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

        ed.putInt("position", position);
        ed.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
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
}
