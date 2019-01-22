package com.example.intimer;

public class TimerTimer {
    private String title;
    private int hour;
    private int minute;
    private int second;
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
        setValue((getHour())*3600 + (getMinute())*60 + getSecond());
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
        setValue((getHour())*3600 + (getMinute())*60 + getSecond());
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
        setValue((getHour())*3600 + (getMinute())*60 + getSecond());
    }

    public TimerTimer(String title, int hour, int minute, int second){
        setTitle(title);
        setHour(hour);
        setMinute(minute);
        setSecond(second);
        setValue((getHour())*3600 + (getMinute())*60 + getSecond());
    }

    public String toString(){
        return getTitle();
    }



}
