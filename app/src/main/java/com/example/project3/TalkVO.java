package com.example.project3;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TalkVO {
    private int imgID;
    private String name;
    private String msg;
    private String date;
    private String time;

    public TalkVO() {

    }


    public TalkVO(int imgID, String name, String msg, String time, String date) {
        this.imgID = imgID;
        this.name = name;
        this.msg = msg;
        this.time = changeTime();
        this.date = changeDate();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TalkVO{" +
                "imgID=" + imgID +
                ", name='" + name + '\'' +
                ", msg='" + msg + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public static String changeTime() {
        String result = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        result = sdf.format(timestamp);

        return result;
    }

    public static String changeDate() {
        String result = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("――――――yyyy 년 MM월 dd일 EE요일――――――――");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        result = sdf.format(timestamp);

        return result;
    }
}


