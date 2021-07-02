package com.example.adiai;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class speechTest extends AppCompatActivity {

    //Variables for different functions support in the app
    private final int RECOGNIZER_RESULT = 1171;
    private String strResult;
    private CameraManager camManager;
    private BluetoothAdapter mBluetoothAdapter;
    private WebView webView;
    private Button openInBrowser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_test);

        //Speech Recognition
        ImageView microphone = (ImageView) findViewById(R.id.mic);
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"What's on mind?");
        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Disabling button and webView
                openInBrowser = (Button) findViewById(R.id.sendToBrowser);
                openInBrowser.setVisibility(View.GONE);
                webView = (WebView) findViewById(R.id.web);
                webView.setVisibility(View.GONE);
                //Vibration
                Vibrator micTouch = (Vibrator)   getSystemService(Context.VIBRATOR_SERVICE);
                micTouch.vibrate(20);
                micTouch.vibrate(40);

                try{
                    startActivityForResult(speechIntent,RECOGNIZER_RESULT);
                }
                catch(Exception ActivityNotFoundException){
                    Toast.makeText(getApplicationContext(), "Recognizer Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Turn on function
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
//            private WifiManager wifiManager;
//            wifiManager = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//            wifiManager.setWifiEnabled(true);
//        }
//        Android 10 does not support wifi automation with program so no need to add it to the code
    }

    //Turn off function
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
//            private WifiManager wifiManager;
//            wifiManager.setWifiEnabled(false);
//        }
//        Android 10 does not support wifi automation with program so no need to add it to the code
    }

    //Start function
    private void startFunction() {
        // Final Code
        if(strResult.contains("android") || strResult.contains("miui")){
            Toast.makeText(getApplicationContext(), strResult + " is OS, Can't open!", Toast.LENGTH_SHORT).show();
        }
        else if(strResult.contains("samsung") || strResult.contains("oneplus") || strResult.contains("xiaomi") || strResult.contains("nokia") || strResult.contains("qualcomm")){
            Toast.makeText(getApplicationContext(), strResult + " is Brand, Can't open!", Toast.LENGTH_SHORT).show();
        }
        else{
            if(strResult.contains("google")){
                strResult = "chrome";
            }
            final PackageManager pm = getPackageManager();
            //Get a list of installed apps
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            for (ApplicationInfo packageInfo : packages) {
                if((packageInfo.packageName).contains(strResult)){
                    Intent unknownAppIntent = pm.getLaunchIntentForPackage(packageInfo.packageName);
                    try {
                        startActivity( unknownAppIntent );
                    } catch (Exception ActivityNotFoundException){
                        Toast.makeText(getApplicationContext(), "Can't open " + strResult, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }
        }
    }

    //Send message through whatsapp function
    private void sendWhatsapp(){

    }

    //Send message through text function
    private void sendText(){

    }

    //Search in app function
    private void searchFunction(){
        openInBrowser.setVisibility(View.VISIBLE);

        //WebView inside the app
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl("http://www.google.com/search?q=" + strResult);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
        openInBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Disabling button and webView
                webView.setVisibility(View.GONE);
                openInBrowser.setVisibility(View.GONE);

                //Intent to Browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q=" + strResult));
                try{
                    startActivity(browserIntent);
                } catch(Exception ActivityNotFoundException){
                    Toast.makeText(getApplicationContext(), "No browser found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Handles the speech text and calls appropriate function with some string alteration
    private void resultParse(){
        if(strResult.indexOf("turn ") == 0 && strResult.contains("on ")){
            turnOnFunction();
        }
        else if(strResult.indexOf("turn ") == 0  && (strResult.contains("of ") || strResult.contains("off "))){
            turnOffFunction();
        }
        else if(strResult.indexOf("start ") == 0 || strResult.indexOf("open ") == 0) {
            if (strResult.indexOf("start ") == 0){
                strResult = strResult.substring(6).replace(" ","");
            }
            else if (strResult.indexOf("open ") == 0){
                strResult = strResult.substring(5).replace(" ","");
            }
            startFunction();
        }
        //Work in progress send function
        else if((strResult.indexOf("send ") == 0)){
            int flag = 0;
            strResult = strResult.substring(5);
            if(strResult.indexOf("a ") == 0){
                strResult = strResult.replace("a ", "");
            }
            if((strResult.indexOf("text ")==0) || (strResult.indexOf("message ")==0)){
                if(strResult.indexOf("text ") == 0)
                    strResult.substring(5);
                if(strResult.indexOf("message ") == 0)
                    strResult.substring(8);
                if(strResult.indexOf("text ") == 0)
                    strResult.substring(5);
                flag = 1;
            }
            if(strResult.indexOf("whatsapp ")==0){
                strResult.substring(9);
                if(strResult.indexOf("message ") == 0)
                    strResult.substring(8);
                if(strResult.indexOf("text ") == 0)
                    strResult.substring(5);
            }
            if((strResult.indexOf("to ")==0) || (strResult.indexOf("for ")==0)){
                if(strResult.indexOf("to ") == 0)
                    strResult.substring(3);
                if(strResult.indexOf("for ") == 0)
                    strResult.substring(4);
            }
            if(flag == 0){
                sendWhatsapp();
            }
            else if(flag == 1){
                sendText();
            }
        }
        else if(strResult.indexOf("what ") == 0 || strResult.indexOf("why ") == 0 || strResult.indexOf("where ") == 0 || strResult.indexOf("when ") == 0 || strResult.indexOf("which ") == 0 || strResult.indexOf("how ") == 0){
            if(strResult.contains("how are you")){
                Toast.makeText(getApplicationContext(), "Awesome as always 😉", Toast.LENGTH_SHORT).show();
            }
            else if(strResult.contains("where are you")){
                Toast.makeText(getApplicationContext(), "Right there in your heart 💕", Toast.LENGTH_SHORT).show();
            }
            else if(strResult.contains("when is your birthday")){
                Toast.makeText(getApplicationContext(), "I guess you should know twinfactor 😁", Toast.LENGTH_SHORT).show();
            }
            else{
                searchFunction();//direct search without string alteration
            }
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