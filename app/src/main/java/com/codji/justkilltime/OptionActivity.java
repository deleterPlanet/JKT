package com.codji.justkilltime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class OptionActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean ISOpenIntro = false;

    LinearLayout aboutGame, web, support;
    Intent intent;
    SharedPreferences sPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        sPref = getSharedPreferences("Variables", 0);

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
}
