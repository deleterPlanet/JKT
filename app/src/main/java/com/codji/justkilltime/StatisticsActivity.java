package com.codji.justkilltime;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.TimeZone;


public class StatisticsActivity extends AppCompatActivity{

    SharedPreferences sPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        sPref = getSharedPreferences("Variables", 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        ((TextView)findViewById(R.id.playText)).setText(sPref.getInt("play", 0) + "");
        ((TextView)findViewById(R.id.pointsText)).setText(sPref.getInt("points", 0) + "");
        ((TextView)findViewById(R.id.timeText)).setText(dateFormat.format(sPref.getLong("time", 0)) + "");
        ((TextView)findViewById(R.id.goodsText)).setText(sPref.getInt("goods", 1) + "");
        ((TextView)findViewById(R.id.missionCompliteText)).setText(sPref.getInt("missionComplete", 0 ) + "");
        ((TextView)findViewById(R.id.clickText)).setText(sPref.getInt("clicks", 0) + "");
        ((TextView)findViewById(R.id.extraLivesText)).setText(sPref.getInt("extraLivesSpend", 0) + "");
    }

    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.sliderin_left, R.anim.sliderout_left);
    }
}
