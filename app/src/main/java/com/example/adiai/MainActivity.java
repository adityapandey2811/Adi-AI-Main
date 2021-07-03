package com.example.adiai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.button);
        btn.setBackgroundColor(Color.WHITE);
        btn.setTextColor(Color.BLACK);
        //Knock Intent (App Start)
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),speechTest.class);
                startActivity(intent);
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