package com.codji.justkilltime;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class ShoopViewPagerAdapter extends PagerAdapter {

    float playerVertices;

   Context mContext ;
   List<LinearLayout> mListScreen;
   Button colorsSet[], modesSet[];
   LinearLayout layoutScreen;
   Button modesBut, playerColorsBut;
   ViewPager viewPager;

    public ShoopViewPagerAdapter(Context mContext, List<LinearLayout> mListScreen, Button modesBut, Button playerColorsBut, ViewPager viewPager, float playerVertices) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
        this.modesBut = modesBut;
        this.playerColorsBut = playerColorsBut;
        this.viewPager = viewPager;
        this.playerVertices = playerVertices;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutScreen = mListScreen.get(position);

        if (position == 0){
            ((GLSurfaceView)layoutScreen.findViewById(R.id.glSurfacePlayerInShop)).setRenderer(new ShopOpenGLRenderer(viewPager, playerVertices));
            fillColorsSet();
        }else{
            fillModesSet();
        }

        container.addView(layoutScreen);

        return layoutScreen;
    }

    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    void fillColorsSet(){
        colorsSet = new Button[10];
        colorsSet[0] = layoutScreen.findViewById(R.id.colorWhite);
        colorsSet[1] = layoutScreen.findViewById(R.id.colorGreen);
        colorsSet[2] = layoutScreen.findViewById(R.id.colorYellow);
        colorsSet[3] = layoutScreen.findViewById(R.id.colorPeach);
        colorsSet[4] = layoutScreen.findViewById(R.id.colorBlue);
        colorsSet[5] = layoutScreen.findViewById(R.id.colorCrimson);
        colorsSet[6] = layoutScreen.findViewById(R.id.colorChocolate);
        colorsSet[7] = layoutScreen.findViewById(R.id.colorPistachio);
        colorsSet[8] = layoutScreen.findViewById(R.id.colorOrange);
        colorsSet[9] = layoutScreen.findViewById(R.id.colorTurquoise);

        for (int i = 0; i < colorsSet.length; i++){
            SharedPreferences sPref = mContext.getSharedPreferences("Variables", 0);
            String s = colorsSet[i].getText().toString();
            if (sPref.getInt("color" + i, 0) > 0) {
                colorsSet[i].setTextColor(mContext.getResources().getColor(R.color.notBuy));
                colorsSet[i].setText(s + " | " + sPref.getInt("color" + i, 0));
            }
            colorsSet[i].setOnClickListener((View.OnClickListener) mContext);
        }
    }

    void fillModesSet(){
        modesSet = new Button[6];
        modesSet[0] = layoutScreen.findViewById(R.id.deg0);
        modesSet[1] = layoutScreen.findViewById(R.id.deg90);
        modesSet[2] = layoutScreen.findViewById(R.id.deg180);
        modesSet[3] = layoutScreen.findViewById(R.id.deg270);
        modesSet[4] = layoutScreen.findViewById(R.id.deg45);
        modesSet[5] = layoutScreen.findViewById(R.id.deg315);

        for (int i = 0; i < modesSet.length; i++){
            SharedPreferences sPref = mContext.getSharedPreferences("Variables", 0);
            String s = modesSet[i].getText().toString().split("°")[0];
            if (sPref.getInt("deg" + s, 0) > 0) {
                modesSet[i].setTextColor(mContext.getResources().getColor(R.color.notBuy));
                modesSet[i].setText(s + "° | " + sPref.getInt("color" + i, 0));
            }
            modesSet[i].setOnClickListener((View.OnClickListener) mContext);
        }
    }
}
