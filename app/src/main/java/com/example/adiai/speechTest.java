package com.example.adiai;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class speechTest extends AppCompatActivity {
    private final int RECOGNIZER_RESULT = 1171;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_test);
        ImageView microphone = (ImageView) findViewById(R.id.mic);
        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator micTouch = (Vibrator)   getSystemService(Context.VIBRATOR_SERVICE);
                micTouch.vibrate(20);
                micTouch.vibrate(40);
                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speech To Text");
                startActivityForResult(speechIntent,RECOGNIZER_RESULT);
            }
        });
    }
    public String strResult;
//    private void resultParse(){
//        strResult = strResult.toLowerCase();
//        RelativeLayout relS = (RelativeLayout) findViewById(R.id.relStart);
//        ImageView knockScreen = (ImageView) findViewById(R.id.backImage);
//        RelativeLayout relI = (RelativeLayout) findViewById(R.id.relInside);
//        TextView result = (TextView) findViewById(R.id.results);
//        if(strResult.contains("dark")){
//            knockScreen.setImageResource(R.drawable.blackbackground);
//            relS.setBackgroundColor(Color.BLACK);
//            relI.setBackgroundColor(Color.BLACK);
//            result.setTextColor(Color.WHITE);
//        }
//    }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK){
            ArrayList<String> match = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            TextView apiResult = (TextView) findViewById(R.id.results);
            strResult = match.get(0).toString();
            apiResult.setText(strResult);
//            resultParse();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}