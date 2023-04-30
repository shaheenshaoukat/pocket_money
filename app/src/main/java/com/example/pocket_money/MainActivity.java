package com.example.pocket_money;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo();

       //splash screen code
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,login.class);
                startActivity(intent);
                finish();

            }
        }, 5000);

        logos();
    }


    public void logo() {
        YoYo.with(Techniques.ZoomInLeft)
                .duration(2000)
                .repeat(0)
                .playOn(findViewById(R.id.piggy));


    }
    public void logos() {
        YoYo.with(Techniques.ZoomInLeft)
                .duration(2000)
                .repeat(0)
                .playOn(findViewById(R.id.savemoneytext));


    }
}