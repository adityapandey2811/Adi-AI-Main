package com.example.adiai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private int CALL_PERMISSION_CODE = 1;
    private int SMS_PERMISSION_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Knock Button Program
        Button btn = findViewById(R.id.button);
        btn.setBackgroundColor(Color.WHITE);
        btn.setTextColor(Color.BLACK);
        //Knock Intent (App Start)
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED ){
                    Intent intent = new Intent(getApplicationContext(),speechTest.class);
                    startActivity(intent);
                }
                else{
                    Snackbar.make(v,"Allow all permissions!",Snackbar.LENGTH_SHORT).show();
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
                }
            }
        });
        //Easter egg
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator micTouch = (Vibrator)   getSystemService(Context.VIBRATOR_SERVICE);
                micTouch.vibrate(100);
                micTouch.vibrate(10);
                micTouch.vibrate(100);
                Toast.makeText(getApplicationContext(), "Knocked out! lol 😂", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

}