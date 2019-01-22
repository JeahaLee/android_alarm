package com.example.intimer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class MainStart extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        TimerTask mTask = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent();
                i.setClass(MainStart.this, TimerList.class);
                startActivity(i);
                finish();
            }
        };
        Timer mTimer = new Timer();
        mTimer.schedule(mTask, 2000);
    }
}
