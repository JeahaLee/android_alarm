package com.example.intimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

public class TimerList extends Activity {

    private static final int ADD_TIMER = 101;
    private static final int RESET_TIMER = 201;
    private static final int DELETE_TIMER = 301;

    // 매니저 객체 불러오기
    private Manager mgr = Manager.getManager();

    // 어레이어댑터 변수 지정
    //private ArrayAdapter<TimerTimer> adapter;
    private BaseAdapter adapter;


    // 리스트뷰 변수 지정
    private ListView list;

    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_list);
        
        mgr.load(this,"timer.dat");

        // 레이아웃에서의 리스트뷰 설정
        list = (ListView)findViewById(R.id.list);

        // 초기화
        init();

        // 버튼 생성
        Button del = (Button)findViewById(R.id.del);
        Button back = (Button)findViewById(R.id.back);

        // 플로팅 버튼 생성
        fab = (FloatingActionButton)findViewById(R.id.fab);
        // 플로팅 버튼 클릭 시
        fab.setOnClickListener(new View.OnClickListener() {

            // 화면 넘기기
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(TimerList.this,Setting.class);
                startActivity(i);
            }
        });


        // 취소 버튼 클릭 시 초기화
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

        // 삭제 버튼 클릭 시
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 불리언 값으로 배열 생성
                SparseBooleanArray booleans = list.getCheckedItemPositions();

                // 리스트에서 뒤에서부터 true이면 삭제
                for (int i = mgr.getList().size() -1 ; i >=0 ; i-- ){
                    if(booleans.get(i) == true){
                        mgr.delete(i);
                    }
                }

                // 초기화
                init();
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        init();
        // Toast.makeText(this, "onResume()", Toast.LENGTH_SHORT).show();
    }






    // 초기화 메서드 생성
    public void init(){

        // 초이스모드 인비저블
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // 어댑터 생성
//        adapter = new ArrayAdapter<TimerTimer>(this,
//                android.R.layout.simple_list_item_2,
//                mgr.getList());

        adapter = new BaseAdapter(){
            @Override
            public int getCount() {
                return mgr.getList().size();
            }
            @Override
            public Object getItem(int position) {
                return mgr.getList().get(position);
            }
            @Override
            public long getItemId(int position) {
                return position;
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v=View.inflate(TimerList.this,R.layout.timer_item,null);
                TimerTimer tt=(TimerTimer)getItem(position);
                TextView tv1=(TextView)v.findViewById(R.id.tv1);
                tv1.setText(tt.getTitle());
                TextView tv2=(TextView)v.findViewById(R.id.tv2);
                tv2.setText(tt.getHour()+"° "+tt.getMinute()+"' "+tt.getSecond()+"\"");
                return v;
            }
        };


        //리스트에 어댑터 적용
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(TimerList.this,TimerStart.class);
                i.putExtra("position",position);
                //Toast.makeText(TimerList.this, "position : "+position, Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });

        // 삭제, 취소 버튼 인비저블
        LinearLayout ll = (LinearLayout)findViewById(R.id.buttonLayout);
        ll.setVisibility(View.GONE);

        // 플로팅버튼 비저블
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
    }





    // 옵션메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // 레이아웃의 메뉴 따 옴
        getMenuInflater().inflate(R.menu.menu_main,menu);
        /*menu.add(0,RESET_TIMER,0,"편집")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0,DELETE_TIMER,0,"삭제")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);*/
        return true;
    }

    // 옵션메뉴 아이템 선택 시
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 삭제 버튼 선택 시
        if(item.getItemId() == R.id.delete){

            // 리스트 초이스 모드
            list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            // 아이템 클릭 비활성화
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });



            // 어댑터 생성
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_multiple_choice,
                    mgr.getList());

            //리스트에 어댑터 적용
            list.setAdapter(adapter);

            // 삭제, 취소 버튼 비저블
            LinearLayout ll = (LinearLayout)findViewById(R.id.buttonLayout);
            ll.setVisibility(View.VISIBLE);

            // 플로팅 버튼 인비저블
            fab = (FloatingActionButton)findViewById(R.id.fab);
            fab.setVisibility(View.GONE);

        // 수정 버튼 클릭 시
        } else if(item.getItemId() == R.id.update){
            init();
            fab.setVisibility(View.GONE);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent();
                    i.setClass(TimerList.this, Setting.class);
                    i.putExtra("position",position);
                    startActivity(i);
                }
            });
        } else {
            init();
        }
        return super.onOptionsItemSelected(item);
    }


}
