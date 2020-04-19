package com.codji.justkilltime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    Button btnGetStarted;
    Animation btnAnim ;
    TextView tvSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if (restorePrefData() && !OptionActivity.ISOpenIntro) {
            Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class );
            startActivity(mainActivity);
            finish();
        }

        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(this,R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem(getString(R.string.moneyIntro),R.drawable.money));
        mList.add(new ScreenItem(getString(R.string.shoopIntro),R.drawable.shoop));
        mList.add(new ScreenItem(getString(R.string.optionIntro),R.drawable.option));
        mList.add(new ScreenItem(getString(R.string.supportIntro),R.drawable.support));
        mList.add(new ScreenItem(getString(R.string.webIntro),R.drawable.my_web));
        mList.add(new ScreenItem(getString(R.string.playerIntro),R.drawable.player));
        mList.add(new ScreenItem(getString(R.string.trianglesIntro),R.drawable.triangles));
        mList.add(new ScreenItem(getString(R.string.productIntro),R.drawable.product));
        /*mList.add(new ScreenItem(getString(R.string.higescoreIntro),R.drawable.higescore));
        mList.add(new ScreenItem(getString(R.string.tapToPlayIntro),R.drawable.tap_to_play));
        mList.add(new ScreenItem(getString(R.string.scoreIntro),R.drawable.score));
        mList.add(new ScreenItem(getString(R.string.exampleIntro),R.drawable.example));
        mList.add(new ScreenItem(getString(R.string.tabsIntro),R.drawable.tabs));*/

        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        tabIndicator.setupWithViewPager(screenPager);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }
                if (position == mList.size()-1) {
                    loaddLastScreen();
                }
            }
        });

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()-1) {
                    loaddLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!OptionActivity.ISOpenIntro) {
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);
                    savePrefsData();
                }
                finish();
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });



    }

    private boolean restorePrefData() {
        SharedPreferences pref = getSharedPreferences("Variables", 0);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("ISIntroOpnend",false);
        return  isIntroActivityOpnendBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Variables", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("ISIntroOpnend",true);
        editor.commit();
    }

    private void loaddLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        btnGetStarted.setAnimation(btnAnim);
    }
}
