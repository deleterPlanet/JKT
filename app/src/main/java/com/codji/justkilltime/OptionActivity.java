package com.codji.justkilltime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Locale;


public class OptionActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean ISOpenIntro = false;

    LinearLayout aboutGame, web, support;
    Intent intent;
    SharedPreferences sPref;
    CompoundButton.OnCheckedChangeListener onChecked;
    SharedPreferences.Editor ed;
    SwitchCompat languageSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sPref = getSharedPreferences("Variables", 0);
        ed = sPref.edit();
        setLanguage();

        setContentView(R.layout.activity_option);

        onChecked = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ed.putString("language", "en");
                }else{
                    ed.putString("language", "ru");
                }
                ed.commit();
                finish();
            }
        };

        languageSwitch = findViewById(R.id.languageSwitch);
        if (sPref.getString("language", "en").equals("en")){languageSwitch.setChecked(true);}
        languageSwitch.setOnCheckedChangeListener(onChecked);

        aboutGame = (LinearLayout)findViewById(R.id.aboutGameLayout);
        web = (LinearLayout)findViewById(R.id.web);
        support = (LinearLayout)findViewById(R.id.support);
        aboutGame.setOnClickListener(this);
        web.setOnClickListener(this);
        support.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aboutGameLayout:
                ISOpenIntro = true;
                intent = new Intent(OptionActivity.this, IntroActivity.class);
                startActivity(intent);
                break;
            case R.id.web:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://deleterplanet.github.io/main.html")));
                break;
            case R.id.support:
                intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"Yakushevi4.a@gmail.com"});
                intent.setType("message/rfc822");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.null_anim, R.anim.sliderout);
    }

    void setLanguage(){
        Locale locale = new Locale(sPref.getString("language", "en"));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
    }
}
