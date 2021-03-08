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
    ImageView Move,Move2,Move_today,Move2_today;
    NeumorphTextView tv_move_today;
    RequestQueue requestQueue;
    AnimatedProgressBar progressBar_room_yesterday, progressBar_house_yesterday,progressbar_room_today,progressbar_house_today;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_graph3, container, false);
        requestQueue = Volley.newRequestQueue(getContext());

        roomID = getArguments().getString("room");

        progressBar_house_yesterday = fragment.findViewById(R.id.progressbar_house_yesterday);
        progressBar_room_yesterday = fragment.findViewById(R.id.progressbar_room_yesterday);
        progressbar_house_today=fragment.findViewById(R.id.progressbar_house_today);
        progressbar_room_today=fragment.findViewById(R.id.progressbar_room_today);


        Move = fragment.findViewById(R.id.move);
        Move2 = fragment.findViewById(R.id.move2);
        Move_today=fragment.findViewById(R.id.move_today);
        Move2_today=fragment.findViewById(R.id.move2_today);

        tv_move_today=fragment.findViewById(R.id.tv_move_today);


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
                    progressBar_room_yesterday.setMax(45); //어제
                    progressBar_room_yesterday.setProgress(myroom_yesterday);
                    progressBar_room_yesterday.setProgressColor(Color.parseColor("#A1A1A1"));
                    progressBar_room_yesterday.setProgressTipColor(Color.parseColor("#797979"));

                    progressBar_house_yesterday.setMax(45); //어제
                    progressBar_house_yesterday.setProgress(house_yesterday);
                    progressBar_house_yesterday.setProgressColor(Color.parseColor("#A1A1A1"));
                    progressBar_house_yesterday.setProgressTipColor(Color.parseColor("#797979"));

                    progressbar_room_today.setMax(45); //어제
                    progressbar_room_today.setProgress(myroom_today);
                    progressbar_room_today.setProgressColor(Color.parseColor("#1C7FDA"));
                    progressbar_room_today.setProgressTipColor(Color.parseColor("#10518D"));


                    progressbar_house_today.setMax(45); //어제
                    progressbar_house_today.setProgress(house_today);
                    progressbar_house_today.setProgressColor(Color.parseColor("#61E09E"));
                    progressbar_house_today.setProgressTipColor(Color.parseColor("#30B670"));




                    Move2.animate() //house 어제
                            .translationX((float)(house_yesterday*14))
                            .translationY(0)
                            .setDuration(1800);


                    Move.animate() //room 어제
                            .translationX((float) (myroom_yesterday*14))
                            .translationY(0)
                            .setDuration(1800);

                    Move_today.animate() //room 오늘
                            .translationX((float) (myroom_today*14))
                            .translationY(0)
                            .setDuration(1800);

                    Move2_today.animate() //house 오늘
                            .translationX((float) (house_today*14))
                            .translationY(0)
                            .setDuration(1800);

                    tv_move_today.animate()
                            .translationX((float) (myroom_today*14))
                            .translationY(0)
                            .setDuration(1800);






                    if (myroom_today > myroom_yesterday) {//내방 과다사용

tv_move_today.setVisibility(View.INVISIBLE);

                    } else if (myroom_today < myroom_yesterday) { //내방 덜사용
                        tv_move_today.setTextColor(Color.parseColor("#34D01B"));
                        tv_move_today.setText("절감중 😀");
         }
                    if (house_today > house_yesterday) { //하우스 과다사용

                    } else if (house_today < house_yesterday) {//하우스 절약



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