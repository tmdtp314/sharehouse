package com.example.project3;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mckrpk.animatedprogressbar.AnimatedProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


public class fragment_graph3 extends Fragment {
 private String roomID;
    StringRequest stringRequest2;
    RequestQueue requestQueue;
    TextView tv_myroom_today, tv_myroom_yesterday, tv_house_today, tv_house_yesterday;
    AnimatedProgressBar progressBar_room, progressBar_house;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_graph3, container, false);
        requestQueue = Volley.newRequestQueue(getContext());

        roomID = getArguments().getString("room");
        Toast.makeText(getContext(),"roomID 들어옴"+roomID,Toast.LENGTH_SHORT).show();
        progressBar_house = fragment.findViewById(R.id.progressbar_house);
        progressBar_room = fragment.findViewById(R.id.progressbar_room);
        tv_house_today=fragment.findViewById(R.id.tv_house_today);
        tv_house_yesterday=fragment.findViewById(R.id.tv_house_yesterday);
        tv_myroom_today=fragment.findViewById(R.id.tv_myroom_today);
        tv_myroom_yesterday=fragment.findViewById(R.id.tv_myroom_yesterday);

        String url="http://172.30.1.49:8083/LoginServer/graph2_MyRoom";
        stringRequest2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                JSONArray array = null;

                try {

                    array = new JSONArray(response);
                    int myroom_today=Integer.parseInt(array.getJSONObject(0).getString("Value"));
                    int house_today=Integer.parseInt(array.getJSONObject(0).getString("Value2"));
                    int myroom_yesterday=Integer.parseInt(array.getJSONObject(0).getString("Value3"));
                    int house_yesterday=Integer.parseInt(array.getJSONObject(0).getString("Value4"));

                    int myroom_final=0;
                    int house_final=0;


                    if(myroom_yesterday!=0&&house_yesterday!=0) {
                        myroom_final = (myroom_today / myroom_yesterday) * 100;
                        house_final = (house_today / house_yesterday) * 100;

                        progressBar_room.setMax(100); //어제
                        progressBar_house.setMax(100); //어제

                        progressBar_house.setProgressColor(Color.parseColor("#2C6DCD")); //파랑
                        progressBar_room.setProgress(myroom_final); //오늘 내방
                        tv_myroom_today.setText(myroom_today + "wh");
                        tv_myroom_today.setTextColor(Color.parseColor("#A14CBF"));
                        tv_myroom_yesterday.setText(myroom_yesterday + "wh");
                        progressBar_room.setProgressColor(Color.parseColor("#A14CBF")); //보라
                        progressBar_house.setProgress(house_final); //오늘 집 전체
                        tv_house_today.setText(house_today + "wh");
                        tv_house_today.setTextColor(Color.parseColor("#2C6DCD"));
                        tv_house_yesterday.setText(house_yesterday + "wh");
                    }
                    else{
                        progressBar_room.setMax(100); //어제
                        progressBar_house.setMax(100); //어제
                        progressBar_room.setProgress(myroom_today);
                        progressBar_house.setProgress(house_today);
                        tv_house_today.setText(house_today + "wh");
                        progressBar_room.setProgressColor(Color.parseColor("#A14CBF"));
                        progressBar_house.setProgressColor(Color.parseColor("#2C6DCD"));
                        tv_house_today.setTextColor(Color.parseColor("#2C6DCD"));
                        tv_house_yesterday.setText(house_yesterday + "wh");
                        tv_myroom_today.setText(myroom_today + "wh");
                        tv_myroom_today.setTextColor(Color.parseColor("#A14CBF"));
                        tv_myroom_yesterday.setText(myroom_yesterday + "wh");

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