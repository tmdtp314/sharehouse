package com.example.project3;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import soup.neumorphism.NeumorphCardView;

public class fragment_mypage extends Fragment {


    NeumorphCardView btn_start2, btn_stop2;
    TextView user_name;
    //    TextView user_tel;
    String encode;
    TextView user_room;
    EditText settingAlarm;
    String user_tel;
    String room_name;
    ArrayList<String> data2 = new ArrayList<>(3);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_mypage, container, false);

        user_name = fragment.findViewById(R.id.user_name);

        user_room = fragment.findViewById(R.id.user_room); //****매우 중요.

        if (getArguments().getStringArrayList("data").get(2).equals("a")) {
            room_name = "Room A / 101호";
        } else if (getArguments().getStringArrayList("data").get(2).equals("b")) {
            room_name = "Room B / 101호";
        }





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