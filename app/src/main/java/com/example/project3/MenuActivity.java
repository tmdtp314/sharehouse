package com.example.project3;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    BottomNavigationView navi;
    fragment_mypage frag_mypage;
    fragment_control frag_control;
    fragment_elect frag_elec;

    fragment_graph1 fragment_graph1;
    fragment_graph2 fragment_graph2;

    String room;
    ArrayList<String> data = new ArrayList<>(3); // fragment1 유저정보를 띄워줄 데이터 저장 list (name, tel, room)
    ArrayList<String> data2 = new ArrayList<>(3); // fragment3 각 기기 전력량 띄워줄 데이터 저장 list (count1, count2, count3)
    // fragment2에 띄워질 방 번호(room)은 data[2].get하여 사용하자


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        navi = findViewById(R.id.bottomNavigationView);

        frag_mypage = new fragment_mypage();
        frag_control = new fragment_control();
        frag_elec = new fragment_elect();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag_mypage).commit();

        navi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag_mypage,"frag_Tag_Mypage").commit();
                        break;
                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag_control,"frag_Tag_Con").commit();
                        break;
                    case R.id.tab3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag_elec,"frag_Tag_Elec").commit();
                        break;
                }

                return true;
            }
        });
        // frage1 유저정보에 갈 data저장 (name, tel, room)
        data.add(getIntent().getStringExtra("name"));
        data.add(getIntent().getStringExtra("tel"));
        data.add(getIntent().getStringExtra("room"));
      //  room = data.get(2);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("data",data);
        frag_mypage.setArguments(bundle);
        frag_elec.setArguments(bundle);


        // frag2 기기제어에 갈 data저장 (room)
        room = data.get(2);


        Bundle bundle_control = new Bundle();
        bundle_control.putString("room",room);
        frag_control.setArguments(bundle_control);




    }

}