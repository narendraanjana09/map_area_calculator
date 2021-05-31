package com.nsa.mapsspeech.ExtraClasses;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;



import java.util.List;

public class StartSpeechRecognition {
    private Activity activity;
    private static final int SPEECH_REQUEST=10;

    public StartSpeechRecognition(Activity activity) {
        this.activity = activity;
        PackageManager packageManager=activity.getPackageManager();
        List<ResolveInfo> listofinformation=packageManager.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0
        );

    }
    public void listenToUser() {
        Intent voiceIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something!");
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
        activity.startActivityForResult(voiceIntent,SPEECH_REQUEST);


    }


}
