package com.example.project3;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tooltip.TooltipDrawable;
import com.mckrpk.animatedprogressbar.AnimatedProgressBar;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphFloatingActionButton;
import soup.neumorphism.NeumorphTextView;


public class fragment_graph3 extends Fragment {
    private String roomID;
    StringRequest stringRequest2;
    ImageView cardview1, cardview3, cardview2, cardview4;
    NeumorphTextView yesterday_right_house, yesterday_right_myroom, yesterday_left_myroom, yesterday_left_house;
    TextView show_room_over, show_room_under, show_house_over, show_house_under, Move, Move2;
    RequestQueue requestQueue;
    TextView tv_myroom_today, tv_house_today, tv_myroom_today_imogi, tv_house_today_imogi;
    AnimatedProgressBar progressBar_room, progressBar_house;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_graph3, container, false);
        requestQueue = Volley.newRequestQueue(getContext());

        roomID = getArguments().getString("room");
        Toast.makeText(getContext(), "roomID 들어옴" + roomID, Toast.LENGTH_SHORT).show();
        progressBar_house = fragment.findViewById(R.id.progressbar_house);
        progressBar_room = fragment.findViewById(R.id.progressbar_room);
        tv_house_today = fragment.findViewById(R.id.tv_house_today_text);
        tv_house_today_imogi = fragment.findViewById(R.id.tv_house_today_imogi);
        tv_myroom_today = fragment.findViewById(R.id.tv_myroom_today_text);
        tv_myroom_today_imogi = fragment.findViewById(R.id.tv_myroom_today_imogi);

        yesterday_left_myroom = fragment.findViewById(R.id.tv_yesterday_left_myroom);
        yesterday_left_house = fragment.findViewById(R.id.tv_yesterday_left_house);
        yesterday_right_house = fragment.findViewById(R.id.tv_yesterday_right_house);
        yesterday_right_myroom = fragment.findViewById(R.id.tv_yesterday_right_myroom);


        cardview1 = fragment.findViewById(R.id.cardview1);
        cardview2 = fragment.findViewById(R.id.cardview2);
        cardview3 = fragment.findViewById(R.id.cardview3);
        cardview4 = fragment.findViewById(R.id.cardview4);

        cardview1.setVisibility(View.INVISIBLE);
        cardview2.setVisibility(View.INVISIBLE);
        cardview3.setVisibility(View.INVISIBLE);
        cardview4.setVisibility(View.INVISIBLE);

        Move = fragment.findViewById(R.id.move);
        Move2 = fragment.findViewById(R.id.move2);


        int[] image = new int[2];
        cardview1.getLocationOnScreen(image);
        Toast.makeText(getContext(), image[0] + "," + image[1], Toast.LENGTH_LONG).show();


        String url = "http://172.30.1.49:8083/LoginServer/graph2_MyRoom";
        stringRequest2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                JSONArray array = null;

                try {

                    array = new JSONArray(response);
                    int myroom_today = Integer.parseInt(array.getJSONObject(0).getString("Value"));
                    int house_today = Integer.parseInt(array.getJSONObject(0).getString("Value2"));
                    int myroom_yesterday = Integer.parseInt(array.getJSONObject(0).getString("Value3"));
                    int house_yesterday = Integer.parseInt(array.getJSONObject(0).getString("Value4"));


                    int myroom_final = (int) (((double) myroom_today / (double) myroom_yesterday) * 100);
                    int house_final = (int) (((double) house_today / (double) house_yesterday) * 100);

                    Toast.makeText(getContext(), String.valueOf(house_today) + "house_today", Toast.LENGTH_SHORT).show();


                    if (myroom_today > myroom_yesterday) { // 내 방 과다사용
                        progressBar_room.setMax(100); //어제
                        cardview1.setVisibility(View.VISIBLE);
//                        show_room_over.setText(" "+myroom_yesterday+"wh");
                        progressBar_room.setProgress(myroom_today);
                        progressBar_room.setProgressColor(Color.parseColor("#E8976A"));

                        progressBar_room.setProgressTipColor(Color.parseColor("#EF6315"));
                        tv_myroom_today.setText("어제 보다 더 사용");
                        tv_myroom_today.setTextColor(Color.parseColor("#CF6809"));
                        tv_myroom_today_imogi.setText("☹");
                        yesterday_right_myroom.setVisibility(View.INVISIBLE);
                        yesterday_left_myroom.setText("+"+String.valueOf(myroom_yesterday) + "kwh");

                        Move.setText(String.valueOf(myroom_yesterday - myroom_today));
                        Move.animate()
                                .translationX((float) (myroom_today * 5.8))
                                .translationY(0)
                                .setDuration(1500);


                    } else if (myroom_today < myroom_yesterday) { //내방 덜사용
                        progressBar_room.setMax(100); //어제
                        cardview2.setVisibility(View.VISIBLE);
                        // show_room_under.setText(array.getJSONObject(0).getString("Value3")+"wh");
                        progressBar_room.setProgress(myroom_final);
                        progressBar_room.setProgressColor(Color.parseColor("#9CEEC6")); //보라
                        tv_myroom_today.setText("어제보다 덜 사용");
                        tv_myroom_today.setTextColor(Color.parseColor("#0EA52F"));
                        tv_myroom_today_imogi.setText("😀");

                        yesterday_left_myroom.setVisibility(View.INVISIBLE);
                        yesterday_right_myroom.setText(String.valueOf(myroom_yesterday) + " kwh");
                        Move.setText("-"+String.valueOf(myroom_yesterday - myroom_today)+" kwh");
                        Move.setTextColor(Color.parseColor("#0EA52F"));
                        Move.animate()
                                .translationX((float) (myroom_final * 5.9))
                                .translationY(0)
                                .setDuration(1500);

                    }
                    if (house_today > house_yesterday) { //과다사용
                        progressBar_house.setMax(100); //어제
                        cardview3.setVisibility(View.VISIBLE);
                        //  show_house_over.setText(array.getJSONObject(0).getString("Value4")+"wh");
                        progressBar_house.setProgress(house_today);
                        progressBar_house.setProgressColor(Color.parseColor("#E8976A"));
                        progressBar_house.setProgressTipColor(Color.parseColor("#EF6315"));

                        //오늘 집 전체
                        tv_house_today.setText("어제 보다 더 사용");
                        tv_house_today.setTextColor(Color.parseColor("#CF6809"));
                        tv_house_today_imogi.setText("☹");

                        yesterday_right_house.setVisibility(View.INVISIBLE);
                        yesterday_left_house.setText(String.valueOf(house_yesterday) + " kwh");


                        Move2.setText("+ " + String.valueOf(house_today - house_yesterday) + "kwh");
                        Move2.animate()
                                .translationX((float) (house_today * 5.8))
                                .translationY(0)
                                .setDuration(1500);


                    } else if (house_today < house_yesterday) {
                        progressBar_house.setMax(100); //어제//오늘 절약 집전체
                        cardview4.setVisibility(View.VISIBLE);
                        //   show_house_under.setText(array.getJSONObject(0).getString("Value4")+"wh");
                        progressBar_house.setProgress(house_final);
                        progressBar_house.setProgressColor(Color.parseColor("#9CEEC6"));

                        //오늘 집 전체
                        tv_house_today.setText("어제보다 덜 사용");
                        tv_house_today.setTextColor(Color.parseColor("#0EA52F"));
                        tv_house_today_imogi.setText("😀");
                        yesterday_left_house.setVisibility(View.INVISIBLE);
                        yesterday_right_house.setText(String.valueOf(house_yesterday) + " kwh");
                        Move2.setText("-"+String.valueOf(house_yesterday - house_today) + "kwh");
                        Move2.setTextColor(Color.parseColor("#0EA52F"));
                        Move2.animate()
                                .translationX((float)(house_final * 5.8))
                                .translationY(0)
                                .setDuration(1500);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();


                data.put("roomId", roomID); //roomID
                return data;
            }
        };
        requestQueue.add(stringRequest2);

        return fragment;
    }

}