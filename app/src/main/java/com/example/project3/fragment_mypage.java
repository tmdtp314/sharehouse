package com.example.project3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import soup.neumorphism.NeumorphCardView;

public class fragment_mypage extends Fragment {


    StringRequest stringRequest;
    RequestQueue requestQueue;
    NeumorphCardView btn_start2, btn_stop2;
    TextView user_name, realtime_fee;
    //    TextView user_tel;
    String encode;
    TextView user_room;
    String roomID;
    EditText settingAlarm;
    String user_tel;
    String room_name;
    ArrayList<String> data2 = new ArrayList<>(3);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_mypage, container, false);
        roomID = getArguments().getStringArrayList("data").get(2);
        user_name = fragment.findViewById(R.id.user_name);
        realtime_fee = fragment.findViewById(R.id.realtime_fee);
        user_room = fragment.findViewById(R.id.user_room); //****매우 중요.

        if (getArguments().getStringArrayList("data").get(2).equals("a")) {
            room_name = "Room A / 101호";
        } else if (getArguments().getStringArrayList("data").get(2).equals("b")) {
            room_name = "Room B / 101호";
        }

        requestQueue = Volley.newRequestQueue(getContext()); //현재 페이지 정보 보내주는것
        String url = "http://172.30.1.49:8083/LoginServer/realtimeFeeServ";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                double fee_=Double.parseDouble(response);
               String fee= String.format("%.1f", fee_);
               realtime_fee.setText(fee+" 원");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            //StringRequest 내의 메소드 오버로딩!


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                //서버에 전송하고 싶은 데이터를 key값, value값으로 저장하여 return
                Map<String, String> data = new HashMap<>();

                data.put("roomId", roomID);

                return data;
            }
        };//여기까지가 생성자 호출

requestQueue.add(stringRequest);
        user_name.setText(getArguments().getStringArrayList("data").get(0) + "님 환영합니다");
        user_room.setText(room_name);

        user_tel = getArguments().getStringArrayList("data").get(1);

        settingAlarm = fragment.findViewById(R.id.settingAlarm);
        btn_start2 = fragment.findViewById(R.id.btn_start2);
        btn_stop2 = fragment.findViewById(R.id.btn_stop2);

        btn_start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Service 시작", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MyService.class);
                String alarm = String.valueOf(settingAlarm);
                intent.putExtra("alarmValue", alarm);
                getActivity().startService(intent);

            }
        });

        btn_stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Service 끝", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MyService.class);
                getActivity().stopService(intent);

            }
        });


//        data2 = getArguments().getStringArrayList("data");
//        user_name.setText(data2.get(0)+"");
//
//        user_room.setText(data2.get(2)+"");

        return fragment;


    }
}