package com.codji.justkilltime;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public class CustomDialogFragment extends DialogFragment implements View.OnClickListener {

    int extraLives;

    LayoutInflater inflater;


    CustomDialogFragment(int extraLives){
        this.extraLives = extraLives;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.second_chance, null);

        v.findViewById(R.id.positiveBut).setOnClickListener(this);
        v.findViewById(R.id.negativeBut).setOnClickListener(this);

        ((TextView)v.findViewById(R.id.extraLivesCount)).setText(extraLives + "");
        ObjectAnimator timerAnimator = ObjectAnimator.ofInt(v.findViewById(R.id.timerSecondChance), "progress", 0, 5000);

        timerAnimator.setDuration(5000);
        timerAnimator.start();

        builder.setView(v);
        builder.setCancelable(false);

        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialogTheme;

        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.positiveBut: PlayActivity.endGameHandler.sendEmptyMessage(PlayActivity.END_GAME_ID); break;
            case R.id.negativeBut: PlayActivity.endGameHandler.sendEmptyMessage(PlayActivity.SECOND_CHANCE_ID); break;
        }
    }
}
