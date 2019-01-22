package com.example.intimer;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Manager {

    // Timer타입의 arraylist 생성(리스트 이름 : tl)
    private ArrayList<TimerTimer> tl = new ArrayList<>();

    // 매니저 생성자
    private Manager(){
        tl.add(new TimerTimer("삶은 계란(반숙)",0,7,0));
        tl.add(new TimerTimer("모의고사",1,40,0));
        tl.add((new TimerTimer("마감",1,23,45)));
    }

    // 매니저 객체 생성(객체 이름 : manager)
    private static Manager manager = new Manager();

    // 매니저 호출
    public static Manager getManager(){
        return manager;
    }

    // 리스트에 추가하는 메서드
    public void add(TimerTimer t) {
        tl.add(t);
    }

    // 리스트에서 삭제하는 메서드
    public void delete(int index){
        tl.remove(index);
    }

    // 해당 인덱스의 타이머 호출
    public TimerTimer get(int index){
        return tl.get(index);
    }

    // 해당 인덱스에 해당 타이머를 설정(덮어씌우기)
    public void set(int index,TimerTimer t){
        tl.set(index,t);
    }

    // 리스트 호출 메서드
    public ArrayList<TimerTimer> getList(){
        return tl;
    }



    // 파일 로드
    public void load(Context c, String name){

        // 오브젝트 인풋 스트림 생성
        ObjectInputStream ois = null;
        try {
            FileInputStream fis = c.openFileInput(name); // 파일 인풋 스트림 생성
            ois = new ObjectInputStream(fis); // 오브젝트 인풋 스트림에 fis 파라메터 생성
            tl = (ArrayList<TimerTimer>) ois.readObject(); // 리스트 파일 읽어오기
        }catch (FileNotFoundException e){
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }finally {
            try {
                if(ois != null) ois.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    public void save(Context c, String name){
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = c.openFileOutput(name, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(tl);
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(oos != null) oos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }






}
