package com.example.intimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Setting extends Activity {

    // 매니저 불러오기
    private Manager mgr = Manager.getManager();

    // 에디트 텍스트 변수 선언
    private EditText titleEd;
    private EditText hourEd;
    private EditText minuteEd;
    private EditText secondEd;

    // 버튼 변수 선언
    private Button save;
    private Button cancel;

    // position 선언
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 인텐트 불러오기
        Intent i = getIntent();

        // position값 지정
        position = i.getIntExtra("position",-1);
        //Toast.makeText(this, "position : "+position, Toast.LENGTH_SHORT).show();

        // 변수 지정
        titleEd = (EditText)findViewById(R.id.title);
        hourEd = (EditText)findViewById(R.id.hour);
        minuteEd = (EditText)findViewById(R.id.minute);
        secondEd = (EditText)findViewById(R.id.second);

        // 저장 버튼 지정
        save = (Button)findViewById(R.id.save);
        // 저장 버튼 클릭 시
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 텍스트를 변수로 지정
                String title = titleEd.getText().toString();
                int hour;
                int minute;
                int second;
                int flag=0;

                // null값인 경우 0 대입
                if(hourEd.getText().toString().isEmpty()){
                    hour = 0;
                    flag++;
                } else{
                    hour = Integer.parseInt(hourEd.getText().toString());
                }
                // null값인 경우 0 대입
                if(minuteEd.getText().toString().isEmpty()){
                    flag++;
                    minute = 0;
                } else{
                    minute = Integer.parseInt(minuteEd.getText().toString());
                }
                // null값인 경우 0 대입
                if(secondEd.getText().toString().isEmpty()){
                    second = 0;
                    flag++;
                } else{
                    second = Integer.parseInt(secondEd.getText().toString());
                }
                
                
                // flag 값이 3인 경우에는 저장 안되게 설정
                if(flag == 3){
                    Toast.makeText(Setting.this, "타이머를 설정해 주세요.", Toast.LENGTH_SHORT).show();
                }


                // title이 null 이면 저장 안되게 설정
                if(titleEd.getText().toString().isEmpty()){
                    Toast.makeText(Setting.this, "제목을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }


                // flag 값이 3이 아닌 경우 & title이 null이 아닌 경우에는 저장
                if(flag != 3 && titleEd.getText().toString().isEmpty() == false){

                    // 타이머 생성
                    TimerTimer t = new TimerTimer(title,hour,minute,second);

                    // 포지션 값이 -1 초과면, 리스트에 해당 포지션 자리에 업데이트
                    if(position > -1  ){
                        mgr.set(position,t);

                        // 그렇지 않다면 리스트에 추가
                    }else{
                        mgr.add(t);
                    }
                    mgr.save(Setting.this, "timer.dat");
                    finish();
                }

                
             




            }
        });

        // 취소 버튼 지정
        cancel = (Button)findViewById(R.id.cancel);
        // 취소 버튼 클릭 시 List 화면으로 이동
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(Setting.this,TimerList.class);
                startActivity(i);
            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();
        if(position > -1){
            TimerTimer t = mgr.get(position);

            titleEd.setText(t.getTitle());
            hourEd.setText(String.valueOf(t.getHour()));
            minuteEd.setText(String.valueOf(t.getMinute()));
            secondEd.setText(String.valueOf(t.getSecond()));

            save.setText("변경");
        }

    }
}
