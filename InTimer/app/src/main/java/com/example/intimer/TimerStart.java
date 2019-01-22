package com.example.intimer;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.TimerTask;

public class TimerStart extends Activity {

    // 매니저 불러오기
    private Manager mgr = Manager.getManager();

    // 변수 선언
    LottieAnimationView lottie;
    private TextView output;
    private TimerTask mTask;
    private Timer mTimer;
    private int position;
    private int value;
    private int inputTime=0;
    private Button alarm_stop;



    Button start;
    Button pause;
    Button stop;
    NotificationManager mNoti;
    Builder mBuilder;


    Handler h = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                output.setText((String)msg.obj);
            }else if(msg.what==2){
                alarm_stop.setEnabled(true);
                start.setEnabled(false);
                stop.setEnabled(false);
                pause.setEnabled(false);
                start.setVisibility(View.GONE);
                pause.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                alarm_stop.setVisibility(View.VISIBLE);
            }
        }
    };

    class MyTask extends TimerTask{
        @Override
        public void run() {
            int hour;
            int min;
            int sec;
            if(value == 0){
                //lottie.cancelAnimation();//영상 정지
                hour = value / 3600;
                min = (value % 3600) / 60;
                sec = (value % 3600) % 60;
                h.sendMessage(h.obtainMessage(1, "" + ((hour < 10) ? "0" + hour : hour) + " : "
                        + ((min < 10) ? "0" + min : min) + " : "
                        + ((sec < 10) ? "0" + sec : sec)));
                mBuilder.setContentText("" + ((hour < 10) ? "0" + hour : hour) + " : " + ((min < 10) ? "0" + min : min) + " : " + ((sec < 10) ? "0" + sec : sec));
                mNoti.notify(0, mBuilder.build());
                mTimer.cancel();//타이머 정지

                h.sendMessage(h.obtainMessage(2));
                //h.sendMessage(h.obtainMessage(3));
                //MediaPlayer player= MediaPlayer.create(MainActivity.this,R.raw.alarm3);
                //player.start();
                Intent i = new Intent();
                i.setClass(TimerStart.this,MainService.class);
                startService(i);

                //  finish();
            }



            //Log.e("log","value="+value);//에러 잡을때
            //String tmp = String.valueOf(value);
            //Toast.makeText(getApplicationContext(),tmp,Toast.LENGTH_SHORT).show();
            //output.setText(String.valueOf(value));
            //Intent intent = new Intent(MainActivity.this, Result.class);
            //startActivity(intent);
            else {
                hour = value / 3600;
                min = (value % 3600) / 60;
                sec = (value % 3600) % 60;
                h.sendMessage(h.obtainMessage(1, "" + ((hour < 10) ? "0" + hour : hour) + " : "
                        + ((min < 10) ? "0" + min : min) + " : "
                        + ((sec < 10) ? "0" + sec : sec)));
                mBuilder.setContentText("" + ((hour < 10) ? "0" + hour : hour) + " : " + ((min < 10) ? "0" + min : min) + " : " + ((sec < 10) ? "0" + sec : sec));
                mNoti.notify(0, mBuilder.build());
                value--;

            }

        }
    };

    public class AlarmReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            throw new UnsupportedOperationException("");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // 액션바 없애기는 매니패스트 파일에서

        // 인텐트 불러오기
        Intent i = getIntent();
        position = i.getIntExtra("position",-1);

        // value 값 지정
        value = mgr.get(position).getValue();

        //Log.e("log","value = "+ value);


        int hh,mm,ss;
        hh = value /3600;
        mm = (value%3600)/60;
        ss = (value%3600)%60;

        mNoti = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        mBuilder = new Builder(TimerStart.this)
                .setSmallIcon(R.drawable.notiicon)
                .setContentTitle("TIMETIME")
                .setContentText(""+((hh<10)? "0" + hh:hh) + " : " +((mm<10)? "0" +mm:mm) + " : " + ((ss<10)? "0" +ss:ss));
        lottie = (LottieAnimationView)findViewById(R.id.lottie);
        mTimer = new Timer();

        start = (Button)findViewById(R.id.start);
        pause = (Button)findViewById(R.id.pause);// 일시 정지
        stop = (Button)findViewById(R.id.stop);

        //처음에는 pause, stop 버튼 누를 수 없음
        pause.setEnabled(false);
        stop.setEnabled(false);
        //pause.setTextColor(Color.BLACK);

        alarm_stop=(Button)findViewById(R.id.alarm_stop);
        alarm_stop.setEnabled(false);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoti.notify(0,mBuilder.build());
                start.setEnabled(false);
                //start.setTextColor(Color.CYAN);
                pause.setEnabled(true);
                stop.setEnabled(true);
                //pause.setTextColor(Color.BLACK);
                mTimer.schedule(new MyTask(),0,1000);
                if(start.getText().toString().equals("시작")){
                    inputTime = value;
                    lottie.setVisibility(View.VISIBLE);
                    lottie.playAnimation();
                    lottie.setSpeed(1f);//1초에 한바퀴
                    lottie.loop(true);
                }
                else{
                    lottie.resumeAnimation();
                }

            }
        });

        alarm_stop.setOnClickListener(new View.OnClickListener() {//알람 정지 버튼을 눌렀을때
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(TimerStart.this,MainService.class);
                stopService(i);
                start.setText("시작");
                start.setEnabled(true);
                pause.setEnabled(false);
                //start.setTextColor(Color.BLACK);
                //pause.setTextColor(Color.BLACK);//다시 초기 상태로 돌려줌
                //stop.setTextColor(Color.BLACK);//다시 초기 상태로 돌려줌
                stop.setEnabled(false);

                alarm_stop.setVisibility(View.INVISIBLE);
                lottie.cancelAnimation();
                lottie.setVisibility(View.GONE);
                start.setVisibility(View.VISIBLE);
                pause.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);
                value = inputTime;
                int hour;
                int min;
                int sec;
                hour = value /3600;
                min = (value%3600)/60;
                sec = (value%3600)%60;
                h.sendMessage(h.obtainMessage(1,""+ ((hour<10)? "0"+hour:hour)  +" : "
                        +((min<10)? "0"+min:min)  + " : "
                        +((sec<10)? "0"+sec:sec)));

                mTimer = new Timer();
                lottie = (LottieAnimationView)findViewById(R.id.lottie);
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mTimer.purge();
                mTimer.cancel();
                mTimer = new Timer();
                start.setEnabled(true);
                //start.setTextColor(Color.BLACK);
                stop.setEnabled(true);
                //stop.setTextColor(Color.BLACK);
                pause.setEnabled(false);
                //pause.setTextColor(Color.CYAN);
                start.setText("재시작");
                lottie.pauseAnimation();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setText("시작");
                //start.setTextColor(Color.BLACK);
                //pause.setTextColor(Color.BLACK);
                mTimer.cancel();
                mTimer = new Timer();
                start.setEnabled(true);
                pause.setEnabled(false);
                stop.setEnabled(false);
                lottie.cancelAnimation();
                lottie.setVisibility(View.GONE);

                value = inputTime;
                int hour;
                int min;
                int sec;
                hour = value /3600;
                min = (value%3600)/60;
                sec = (value%3600)%60;
                h.sendMessage(h.obtainMessage(1,""+ ((hour<10)? "0"+hour:hour)  +" : "
                        +((min<10)? "0"+min:min)  + " : "
                        +((sec<10)? "0"+sec:sec)));

                mTimer = new Timer();

                lottie.setVisibility(View.VISIBLE);

            }
        });

        output = (TextView)findViewById(R.id.output);

    }



    @Override
    protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }
}
