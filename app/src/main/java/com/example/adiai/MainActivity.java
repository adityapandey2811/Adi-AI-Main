package com.example.adiai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.button);
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
                Toast.makeText(getApplicationContext(), "Knocked out! lol 😂", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

//    public void main(String[] args) {
//        OsEssentials IAM = new OsEssentials();
//        if(IAM.getthemeMode() == 0){
//            Button btn = (Button) findViewById(R.id.button);
//            btn.setBackgroundColor(Color.WHITE);
//            btn.setTextColor(Color.BLACK);
//            ImageView backImage = (ImageView) findViewById(R.id.backImage);
//            backImage.setImageResource(R.drawable.whitebackground);
//        }
//        else{
//            RelativeLayout relS = (RelativeLayout) findViewById(R.id.relStart);
//            ImageView knockScreen = (ImageView) findViewById(R.id.backImage);
//            RelativeLayout relI = (RelativeLayout) findViewById(R.id.relInside);
//            TextView result = (TextView) findViewById(R.id.results);
//            knockScreen.setImageResource(R.drawable.blackbackground);
//            relS.setBackgroundColor(Color.BLACK);
//            relI.setBackgroundColor(Color.BLACK);
//            result.setTextColor(Color.WHITE);
//        }
//    }
}