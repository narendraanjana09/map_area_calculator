package com.nsa.mapsspeech.ExtraClasses;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.nsa.mapsspeech.R;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;


public class BalloonText {

    public static Balloon.Builder getBalloon(String message, Context context) {
       return new Balloon.Builder(context)
                .setArrowSize(10)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.5f)
                .setArrowColor(ContextCompat.getColor(context, R.color.teal_700))
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(65)
                .setTextSize(15f)
                .setCornerRadius(10f)
                .setPaddingLeft(5)
                .setPaddingRight(5)
                .setAlpha(0.9f)
                .setText(message)
               .setDismissWhenClicked(true)
                .setTextColor(ContextCompat.getColor(context, R.color.black))
                .setTextIsHtml(true)
                .setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                .setBalloonAnimation(BalloonAnimation.FADE);

    }

}
