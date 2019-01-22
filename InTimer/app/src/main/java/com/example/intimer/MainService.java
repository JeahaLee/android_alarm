package com.example.intimer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class MainService extends Service {
    private MediaPlayer player;
    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this,R.raw.alarm3);
        player.setLooping(true);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}