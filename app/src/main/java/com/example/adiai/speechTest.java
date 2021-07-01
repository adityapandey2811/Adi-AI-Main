package com.example.adiai;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.Color;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
                try{
                    Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speech To Text");
                    startActivityForResult(speechIntent,RECOGNIZER_RESULT);
                }
                catch(Exception ActivityNotFoundException){
                    Toast.makeText(getApplicationContext(), "Recognizer Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private String strResult;
    private CameraManager camManager;
    private BluetoothAdapter mBluetoothAdapter;
    private WifiManager wifiManager;
    private void turnOnFunction(){
        if(strResult.contains("flashlight") || strResult.contains("light")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                strResult = null;
                try {
                    strResult = camManager.getCameraIdList()[0];
                    camManager.setTorchMode(strResult, true);   //Turn ON
                } catch (Exception CameraAccessException) {
                    CameraAccessException.printStackTrace();
                }
            }
        }
        else if(strResult.contains("bluetooth") || strResult.contains("blue tooth")){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(getApplicationContext(), "This device does not support BlueTooth", Toast.LENGTH_LONG).show();
            }
            else if(!mBluetoothAdapter.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,10);
            }
        }
//        else if(strResult.contains("wifi") || strResult.contains("wi-fi") || strResult.contains("wi fi")){
//            wifiManager = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//            wifiManager.setWifiEnabled(true);
//        }
//        Android 10 does not support wifi automation with program so no need to add it to the code
    }
    private void turnOffFunction(){
        if(strResult.contains("flashlight") || strResult.contains("light")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try{
                    strResult = camManager.getCameraIdList()[0];
                    camManager.setTorchMode(strResult, false);
                } catch (Exception CameraAccessException){
                    CameraAccessException.printStackTrace();
                }
            }
        }
        else if(strResult.contains("bluetooth") || strResult.contains("blue tooth")){
            mBluetoothAdapter.disable();
        }
//        else if(strResult.contains("wifi") || strResult.contains("wi-fi") || strResult.contains("wi fi")){
//            wifiManager.setWifiEnabled(false);
//        }
//        Android 10 does not support wifi automation with program so no need to add it to the code
    }
    private void startFunction() {
        // Final Code
//        if(strResult == "android" || strResult == "miui"){
//            Toast.makeText(getApplicationContext(), strResult + " is OS, Can't open!", Toast.LENGTH_SHORT).show();
//        }
//        else if(strResult == "samsung" || strResult == "oneplus" || strResult == "xiaomi" || strResult == "nokia" || strResult == "qualcomm"){
//            Toast.makeText(getApplicationContext(), strResult + " is Brand, Can't open!", Toast.LENGTH_SHORT).show();
//        }
//        else if(strResult == "google"){
//            strResult = "chrome";
//        }
//        else{
//            final PackageManager pm = getPackageManager();
////            get a list of installed apps
//            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//            for (ApplicationInfo packageInfo : packages) {
//                if((packageInfo.packageName).contains(strResult)){
//                    Toast.makeText(getApplicationContext(), "App Found", Toast.LENGTH_SHORT).show();
//                    Intent unknownAppIntent = pm.getLaunchIntentForPackage(packageInfo.packageName);
//                    try {
//                        startActivity( unknownAppIntent );
//                    } catch (Exception ActivityNotFoundException){
//                        Toast.makeText(getApplicationContext(), "Can't open " + strResult, Toast.LENGTH_SHORT).show();
//                    }
//                    return;
//                }
//            }
//        }
        //Final Code over
        // Helpful
//        final PackageManager pm = getPackageManager();
//        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//
//        for (ApplicationInfo packageInfo : packages) {
//            Log.d(null, "Installed package :" + packageInfo.packageName);
//            Log.d(null, "Source dir : " + packageInfo.sourceDir);
//            Log.d(null, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
//        }

    }
    private void resultParse(){
        if(strResult.contains("turn") && strResult.indexOf("turn") == 0 && strResult.contains("on")){
            turnOnFunction();
        }
        else if(strResult.contains("turn") && strResult.indexOf("turn") == 0  && (strResult.contains("of") || strResult.contains("off"))){
            turnOffFunction();
        }
        else if((strResult.contains("start") && strResult.indexOf("start") == 0) || (strResult.contains("open") && strResult.indexOf("open") == 0)) {
            if (strResult.contains("start")){
                strResult = strResult.substring(5).replace(" ","");
            }
            else if (strResult.contains("open")){
                strResult = strResult.substring(4).replace(" ","");
            }
            startFunction();
        }
    }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK){
            ArrayList<String> match = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            TextView apiResult = (TextView) findViewById(R.id.results);
            strResult = match.get(0).toString();
            apiResult.setText(strResult);
            strResult = strResult.toLowerCase();
            resultParse();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}