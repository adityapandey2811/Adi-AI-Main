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
import android.provider.AlarmClock;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class speechTest extends AppCompatActivity {

    //Variables for different functions support in the app
    private final int RECOGNIZER_RESULT = 1171;
    private String strResult = null;
    private String originalString = null;
    private CameraManager camManager = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private WebView webView = null;
    private Button openInBrowser = null;
    private int flag;
    private ImageView main_ai = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_test);

        //Speech Recognition
        ImageView microphone = findViewById(R.id.mic);
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"What's on mind?");
        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Disabling button and webView
                openInBrowser = findViewById(R.id.sendToBrowser);
                openInBrowser.setVisibility(View.GONE);
                webView = findViewById(R.id.web);
                webView.setVisibility(View.GONE);
                main_ai = findViewById(R.id.main_ai);
                main_ai.setVisibility(View.GONE);
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
    private int turnOnFunction(){
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
            return 11;
        }
        else if(strResult.contains("bluetooth") || strResult.contains("blue tooth")){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            flag = 11;
            if (mBluetoothAdapter == null) {
                Toast.makeText(getApplicationContext(), "This device does not support BlueTooth", Toast.LENGTH_LONG).show();
            }
            else if(!mBluetoothAdapter.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,10);
            }
            return 11;
        }
        return 0;
    }

    //Turn off function
    private int turnOffFunction(){
        if(strResult.contains("flashlight") || strResult.contains("light")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                strResult = null;
                try{
                    strResult = camManager.getCameraIdList()[0];
                    camManager.setTorchMode(strResult, false);
                } catch (Exception CameraAccessException){
                    CameraAccessException.printStackTrace();
                }
            }
            return 11;
        }
        else if(strResult.contains("bluetooth") || strResult.contains("blue tooth")){
            flag = 11;
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothAdapter.disable();
            return 11;
        }
        return 0;
    }
    int toast = 0;
    //Start function
    private int startFunction() {
        // Final Code
        View view = findViewById(R.id.mic);
        if(strResult.contains("android") || strResult.contains("miui")){
            Snackbar.make(view,strResult + " is OS, Can't open!",Snackbar.LENGTH_SHORT).show();
            return 12;
        }
        else if(strResult.contains("samsung") || strResult.contains("oneplus") || strResult.contains("xiaomi") || strResult.contains("nokia") || strResult.contains("qualcomm")){
            Snackbar.make(view,strResult + " is Brand, Can't open!",Snackbar.LENGTH_SHORT).show();
            return 12;
        }
        else{
            if(strResult.contains("google")){
                strResult = "chrome";
            }
            final PackageManager pm = getPackageManager();
            String checkApp = null;
            //Get a list of installed apps
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            for (ApplicationInfo packageInfo : packages) {
                if((packageInfo.packageName).contains(strResult)){
                    checkApp = packageInfo.packageName;
                    Intent unknownAppIntent = pm.getLaunchIntentForPackage(packageInfo.packageName);
                    try {
                        toast = 0;
                        startActivity( unknownAppIntent );
                    } catch (Exception ActivityNotFoundException){
                        Toast.makeText(getApplicationContext(), "Can't open " + strResult, Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
            if(checkApp == null && toast == 0){
                Snackbar.make(view,"App not found!",Snackbar.LENGTH_SHORT).show();
            }
            return 12;
        }
    }

    //Send message through text function
    private void sendText(String name, String message){
        //get contacts will be good instead of number
        int i = 0;
        if(name != null && message != null){
            try{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(name,null,message,null,null);
                i = 1;
                Toast.makeText(this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        if(i!=1){
            try{
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                sendIntent.putExtra("sms_body", "default content");
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
            } catch (Exception ex){
                Toast.makeText(this, "SMS not sent!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Send message through whatsapp function
    private void sendWhatsapp(String name, String message){
        int init = 0;
        if(name == null && message != null){
            Intent sendIntent = new Intent();
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            try {
                startActivity(sendIntent);
                init = 1;
            } catch (Exception e){
                e.printStackTrace();
            }

        }
        if(init != 1 && name != null && message != null){
            Intent i = new Intent(Intent.ACTION_VIEW);
            try {
                String url = "https://api.whatsapp.com/send?phone="+ name +"&text=" + URLEncoder.encode(message, "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                startActivity(i);
                init = 1;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        if(init != 1){
            strResult = "whatsapp";
            startFunction();
        }
    }

    //Send message through email
    private void sendEmail(String message){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
//        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        try {
            startActivity(intent);
        } catch (Exception ActivityNotFoundException) {
            Toast.makeText(getApplicationContext(),"Can't send email!", Toast.LENGTH_LONG).show();
        }
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
                View view = findViewById(R.id.mic);
                try{
                    startActivity(browserIntent);
                } catch(Exception ActivityNotFoundException){
                    Snackbar.make(view,"No browser found!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Call using call function
    private void callFunction(String numberOrName){
        //Enters number in the dialer
        if(numberOrName.substring(0,1).matches(".*\\d.*"))
            try{
                Intent phone_intent = new Intent(Intent.ACTION_CALL);
                phone_intent.setData(Uri.parse("tel:" + numberOrName));
                startActivity(phone_intent);
            } catch (Exception e){
                e.printStackTrace();
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", numberOrName, null)));
            }
        else{
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", numberOrName, null)));
        }
    }

    //Set alarm using alarm function
    private void alarmFunction(int hour, int minutes){
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        try{
            startActivity(intent);
        } catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error occured!", Toast.LENGTH_SHORT).show();
        }
    }

    //Miscellaneous function
    private void miscellaneousFunction(){
        View view = findViewById(R.id.mic);
        TextView apiResult = findViewById(R.id.results);
        if(strResult.contains("i love you") || strResult.contains("i miss you")){
            apiResult.setText("I love you too");
            Toast.makeText(getApplicationContext(),"❤💛", Toast.LENGTH_LONG).show();
            main_ai.setVisibility(View.VISIBLE);
        }
        else if(strResult.contains("i need a girlfriend") || strResult.contains("i need a girlfriend")){
            strResult = "tinder";
            Toast.makeText(getApplicationContext(),"Here you go!", Toast.LENGTH_LONG).show();
            searchFunction();
        }
        else if(strResult.contains("tilt screen")){
            strResult = "askew";
            searchFunction();
        }
        else if(strResult.contains("***")){
            Toast.makeText(getApplicationContext(),"Bad Word!😔", Toast.LENGTH_LONG).show();
            apiResult.setText("I am sorry!");
        }
        else if(strResult.contains("welcome") || strResult.contains("hello") || strResult.contains("mahalo") || strResult.contains("bonjour") || strResult.contains("sup") || strResult.contains("wassup")
        || strResult.contains("hi") || strResult.contains("nice to meet you") || strResult.contains("thank you")){
            apiResult.setText("You're a really nice person.");
            strResult = "Thank you";
            searchFunction();
        }
        else if(strResult.contains("good morning") || strResult.contains("good afternoon") || strResult.contains("good evening") || strResult.contains("good night")){
            apiResult.setText("Same to you ❤💛");
            main_ai.setVisibility(View.VISIBLE);
        }
        else if(strResult.contains("sorry") || strResult.contains("i apologize")){
            apiResult.setText("No need my friend!");
            Snackbar.make(view,"It's all good 😁",Snackbar.LENGTH_SHORT).show();
            main_ai.setVisibility(View.VISIBLE);
        }
        else{
            toast = 1;
            strResult = strResult.replace(" ","");
            startFunction();
            if(toast == 1){
                strResult = originalString;
                apiResult.setText("Sorry!, didn't understand what you wanted me to do! Here's a web search for that");
                searchFunction();
                toast = 0;
            }
        }
    }

    //Handles the speech text and calls appropriate function with some string alteration
    private void resultParse(){
        //turn function condition
        int flag = 0;
        if(strResult.indexOf("turn ") == 0 && strResult.contains("on ") && flag == 0){
            flag = turnOnFunction();
        }
        if(strResult.indexOf("turn ") == 0  && (strResult.contains("of ") || strResult.contains("off ")) && flag == 0){
            flag = turnOffFunction();
        }
        //start function condition
        if((strResult.indexOf("start ") == 0 || strResult.indexOf("open ") == 0) && flag == 0) {
            if (strResult.indexOf("start ") == 0){
                strResult = strResult.substring(6).replace(" ","");
            }
            else if (strResult.indexOf("open ") == 0){
                strResult = strResult.substring(5).replace(" ","");
            }
            startFunction();
        }
        //Send function done
        if((strResult.indexOf("send ") == 0) && flag == 0){
            strResult = strResult.substring(5);
            if(strResult.indexOf("a ") == 0){
                strResult = strResult.replace("a ", "");
            }
            else if(strResult.indexOf("an ") == 0){
                strResult = strResult.replace("an ", "");
            }
            //Solved email error
            if(strResult.indexOf("mail")==0 || strResult.indexOf("email")==0 || strResult.indexOf("e-mail")==0){
                if(strResult.indexOf("mail ") == 0)
                    strResult = strResult.substring(5);
                else if(strResult.indexOf("email ") == 0)
                    strResult = strResult.substring(6);
                else if(strResult.indexOf("e-mail ") == 0)
                    strResult = strResult.substring(7);
                if(strResult.indexOf("mail") == 0 || strResult.indexOf("email") == 0 || strResult.indexOf("e-mail") == 0){
                    sendEmail(null);
                    return;
                }
                flag = 3;
            }
            //Solved message error
            else if(strResult.indexOf("text")==0 || strResult.indexOf("message")==0 || strResult.indexOf("sms") == 0){
                if(strResult.indexOf("sms ") == 0)
                    strResult = strResult.substring(4);
                if(strResult.indexOf("text ") == 0)
                    strResult = strResult.substring(5);
                if(strResult.indexOf("message ") == 0)
                    strResult = strResult.substring(8);
                if(strResult.indexOf("sms") == 0 || strResult.indexOf("message") == 0 || strResult.indexOf("text") == 0){
                    sendText(null,null);
                    return;
                }
                flag = 1;
            }
            //Solved whatsapp error
            else if(strResult.indexOf("whatsapp")==0){
                if(strResult.indexOf("whatsapp ") == 0)
                    strResult = strResult.substring(9);
                else if(strResult.indexOf("whatsapp") == 0){
                    sendWhatsapp(null,null);
                }
                flag = 2;
            }
            if((strResult.indexOf("to")==0 || strResult.indexOf("for")==0)){
                if(strResult.indexOf("to ") == 0)
                    strResult = strResult.substring(3);
                if(strResult.indexOf("for ") == 0)
                    strResult = strResult.substring(4);
                if(strResult.indexOf("our ") == 0)
                    strResult = strResult.substring(4);
                if(strResult.indexOf("a ") == 0)
                    strResult = strResult.substring(4);
                if(strResult.indexOf("to") == 0 || strResult.indexOf("for") == 0 || strResult.indexOf("our") == 0 || strResult.indexOf("a") == 0){
                    if(flag == 1){
                        sendText(null,null);
                        return;
                    }
                    if(flag == 2){
                        sendWhatsapp(null,null);
                        return;
                    }
                }
            }
            String name = null;
            String message = null;
            String checkContact = null;
            int nameIssue = 0;
            if(!strResult.isEmpty() && strResult.substring(0,1).matches(".*\\d.*")){
                String number = strResult.substring(0,12);
                strResult = strResult.substring(12);
                number = number.replace(" ","");
                name = "+91" + number;
                nameIssue = 1;
            }
            if(strResult.contains(" ")){
                if(strResult.indexOf(" ") == 0)
                    strResult = strResult.substring(1);
                if(nameIssue == 0){
                    checkContact = strResult.substring(0,strResult.indexOf(" ") + 1);
                    strResult = strResult.substring(strResult.indexOf(" ") + 1);
                }
                if(strResult.indexOf("containing") ==0)
                    strResult = strResult.substring(10);
                else if(strResult.indexOf("saying that") == 0)
                    strResult = strResult.substring(11);
                else if(strResult.indexOf("saying") == 0)
                    strResult = strResult.substring(6);
                else if(strResult.indexOf("that is") == 0)
                    strResult = strResult.substring(7);
                else if(strResult.indexOf("with message") == 0)
                    strResult = strResult.substring(12);
                else if(strResult.indexOf("and message") == 0)
                    strResult = strResult.substring(11);
                if(strResult.indexOf(" ") == 0)
                    strResult = strResult.substring(1);
                if(strResult != null)
                    message = strResult + ".";
            }
            if(flag == 2){
                if(name!=null)
                    sendWhatsapp(name,message);
                else
                    sendWhatsapp(null,message);
            }
            else if(flag == 1){
                if(name!=null)
                    sendText(name,message);
                else
                    sendText(null,null);
            }
            else if(flag == 3){
                if(message!=null)
                    sendEmail(message);
                else
                    sendEmail(null);
            }
        }
        if((strResult.indexOf("find ") == 0 || strResult.indexOf("search ") == 0 || strResult.indexOf("what ") == 0 || strResult.indexOf("why ") == 0 || strResult.indexOf("where ") == 0 || strResult.indexOf("when ") == 0 || strResult.indexOf("which ") == 0 || strResult.indexOf("how ") == 0 || strResult.indexOf("who ") == 0) && flag == 0){
            flag = 1;
            if(strResult.contains("how are you")){
                Toast.makeText(getApplicationContext(), "Awesome as always 😉", Toast.LENGTH_SHORT).show();
            }
            else if(strResult.contains("where are you")){
                Toast.makeText(getApplicationContext(), "Right there in your heart 💕", Toast.LENGTH_SHORT).show();
            }
            else if(strResult.contains("when is your birthday")){
                Toast.makeText(getApplicationContext(), "I guess you should know twin factor 😁", Toast.LENGTH_SHORT).show();
            }
            else if(strResult.contains("who are you")){
                main_ai.setVisibility(View.VISIBLE);
                main_ai.setImageResource(R.drawable.main_ai_image);
                View view = findViewById(R.id.mic);
                Toast.makeText(getApplicationContext(), "I am ADI 😁", Toast.LENGTH_SHORT).show();
                Snackbar.make(view = findViewById(R.id.mic),"I am ADI 😁",Snackbar.LENGTH_SHORT).show();
            }
            else{
                if(strResult.indexOf("find out ") == 0)
                    strResult = strResult.substring(9);
                if(strResult.indexOf("search ") == 0)
                    strResult = strResult.substring(7);
                if(strResult.indexOf("for ") == 0)
                    strResult = strResult.substring(4);
                searchFunction();
            }
        }
        //Solved call error
        if((strResult.indexOf("call") == 0 || strResult.indexOf("phone call") == 0) && !strResult.contains("call of duty") && flag == 0){
            if(strResult.indexOf("phone ") == 0){
                strResult = strResult.substring(6);
            }
            if(strResult.indexOf("call ") == 0){
                strResult = strResult.substring(5);
            }
            if(strResult.indexOf("phone call") == 0 || strResult.indexOf("call") == 0){
                callFunction(null);
                return;
            }
            callFunction(strResult);
            flag = 1;
        }
        //Set alarm done
        if(strResult.indexOf("set") == 0 && flag == 0){
//            flag = 0;
            if(strResult.indexOf("set ") == 0){
                strResult = strResult.substring(4);
            }
            else if(strResult.indexOf("set") == 0){
                Toast.makeText(getApplicationContext(),"What should I set?", Toast.LENGTH_SHORT).show();
            }
            if(strResult.indexOf("a ") == 0 || strResult.indexOf("an ") == 0){
                if(strResult.indexOf("a ") == 0)
                    strResult = strResult.substring(2);
                else if(strResult.indexOf("an ") == 0)
                    strResult = strResult.substring(3);
                else if(strResult.indexOf("a") == 0)
                    Toast.makeText(getApplicationContext(),"What should I set?", Toast.LENGTH_SHORT).show();
                else if(strResult.indexOf("an") == 0)
                    Toast.makeText(getApplicationContext(),"What should I set?", Toast.LENGTH_SHORT).show();
            }
            if(strResult.indexOf("alarm") == 0){
                if(strResult.indexOf("alarm ") == 0)
                    strResult = strResult.substring(6);
                if(strResult.indexOf("alarm") == 0){
                    strResult = "clock";
                    startFunction();
                }
                flag = 1;
            }
            if(strResult.indexOf("for") == 0 || strResult.indexOf("to") == 0 || strResult.indexOf("at") == 0 || strResult.indexOf("on") == 0){
                if(strResult.indexOf("for ") == 0)
                    strResult = strResult.substring(4);
                if(strResult.indexOf("to ") == 0)
                    strResult = strResult.substring(3);
                if(strResult.indexOf("at ") == 0)
                    strResult = strResult.substring(3);
                if(strResult.indexOf("on ") == 0)
                    strResult = strResult.substring(3);
                if(strResult.indexOf("for") == 0 || strResult.indexOf("to") == 0 || strResult.indexOf("at") == 0 || strResult.indexOf("on") == 0){
                    strResult = "clock";
                    startFunction();
                }
            }
            int hour = 0,minute = 0,index;
            String ampm;
            if(strResult.substring(0,1).matches(".*\\d.*")){
                if(strResult.contains(":"))
                    index = strResult.indexOf(":");
                else if(strResult.contains(" "))
                    index = strResult.indexOf(" ");
                else
                    index = strResult.length();
                hour = Integer.parseInt(strResult.substring(0,index));
                if(!strResult.isEmpty() && strResult.indexOf(" ") == -1 && strResult.length() > 1){
                    minute = Integer.parseInt(strResult.substring(index + 1));
                }
                else if(!strResult.isEmpty() && strResult.length() > 1)
                    minute = Integer.parseInt(strResult.substring(index + 1, strResult.indexOf(" ")));
                alarmFunction(hour,minute);
                strResult = strResult.substring(1);
                return;
            }
            if(strResult.indexOf("a.m.") == 0){
                ampm = "am";
                //change to 24 hours format then call alarm function
            }
            else if(strResult.indexOf("p.m.") == 0){
                ampm = "pm";
                //change to 24 hours format then call alarm function
            }
        }
        if(flag == 0){
            //Contains easter eggs
            strResult = originalString;
            miscellaneousFunction();
        }
    }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK){
            try{
                ArrayList<String> match = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                TextView apiResult = findViewById(R.id.results);
                strResult = match.get(0);
                apiResult.setText(strResult);
                originalString = strResult;
                strResult = strResult.toLowerCase();
            } catch (Exception NullPointerException){
                Toast.makeText(getApplicationContext(), "Speech Recognition Error!", Toast.LENGTH_SHORT).show();
            }
            resultParse();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}