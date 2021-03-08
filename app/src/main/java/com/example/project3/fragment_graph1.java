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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hadiidbouk.charts.BarData;
import com.hadiidbouk.charts.ChartProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


import soup.neumorphism.NeumorphTextView;

import static android.graphics.Color.parseColor;

public class fragment_graph1 extends Fragment {
    ChartProgressBar mChart, mChart2, mChart3, mChart4, mChart5, mChart6;
    NeumorphTextView VS;
    RequestQueue requestQueue;
    TextView tv_today, tv_avg ,house_avg;
    StringRequest stringRequest,stringRequest2;
    String result;
    BarData bardata;
    String[] dayname;
    float[] total;

    String roomID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_graph1, container, false);

        roomID = getArguments().getString("room");
        Toast.makeText(getContext(), roomID, Toast.LENGTH_SHORT).show();
        requestQueue = Volley.newRequestQueue(getContext()); //현재 페이지 정보 보내주는것
house_avg=fragment.findViewById(R.id.house_avg);

        tv_avg = fragment.findViewById(R.id.tv_avg);
        //  tv_today = fragment.findViewById(R.id.tv_today);
        VS = fragment.findViewById(R.id.VS);

        String url2 = "http://172.30.1.49:8083/LoginServer/weeTotal";
        stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray array =null;
                try {

                    array = new JSONArray(response);

                    ArrayList<BarData> dataList = new ArrayList<>();

                    total = new float[array.length()];
                    for(int i=0;i<array.length();i++){

                    total[i]=Float.parseFloat(array.getJSONObject(i).getString("use"));
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
            //StringRequest 내의 메소드 오버로딩!


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                //서버에 전송하고 싶은 데이터를 key값, value값으로 저장하여 return
                Map<String, String> data = new HashMap<>();


                data.put("id", roomID); //roomID

                return data;
            }
        };
        requestQueue.add(stringRequest2);
        String url = "http://172.30.1.49:8083/LoginServer/weekGraph";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                result = "Tue";
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("EEE");



                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
              //  result = sdf.format(timestamp);
                result="Sat";


                switch (result) {
                    case "Mon":
                        dayname = new String[]{"화", "수", "목", "금", "토", "일", "오늘"};
                        break;
                    case "Tue":
                        dayname = new String[]{"수", "목", "금", "토", "일", "월", "오늘"};
                        break;
                    case "Wed":
                        dayname = new String[]{"목", "금", "토", "일", "월", "화", "오늘"};
                        break;
                    case "Thu":
                        dayname = new String[]{"금", "토", "일", "월", "화", "수", "오늘"};
                        break;
                    case "Fri":
                        dayname = new String[]{"토", "일", "월", "화", "수", "목", "오늘"};
                        break;
                    case "Sat":
                        dayname = new String[]{"일", "월", "화", "수", "목", "금", "오늘"};
                        break;
                    case "Sun":
                        dayname = new String[]{"월", "화", "수", "목", "금", "토", "오늘"};
                        break;
                }

                JSONArray array =null;
                try {

                    array = new JSONArray(response);

                    ArrayList<BarData> dataList = new ArrayList<>();


                    BarData data = new BarData(dayname[0], (Float.parseFloat(array.getJSONObject(6).getString("use"))/total[0])*100, array.getJSONObject(6).getString("use") + "wh");
                    dataList.add(data);

                    data = new BarData(dayname[1], (Float.parseFloat(array.getJSONObject(5).getString("use"))/total[1])*100, array.getJSONObject(5).getString("use") + "wh");
                    dataList.add(data);

                    data = new BarData(dayname[2], (Float.parseFloat(array.getJSONObject(4).getString("use"))/total[2])*100, array.getJSONObject(4).getString("use") + "wh");
                    dataList.add(data);

                    data = new BarData(dayname[3], (Float.parseFloat(array.getJSONObject(3).getString("use"))/total[3])*100, array.getJSONObject(3).getString("use") + "wh");
                    dataList.add(data);

                    data = new BarData(dayname[4], (Float.parseFloat(array.getJSONObject(2).getString("use"))/total[4])*100, array.getJSONObject(2).getString("use") + "wh");
                    dataList.add(data);

                    data = new BarData(dayname[5], (Float.parseFloat(array.getJSONObject(1).getString("use"))/total[5])*100, array.getJSONObject(1).getString("use") + "wh");
                    dataList.add(data);

                    data = new BarData("오늘", (Float.parseFloat(array.getJSONObject(0).getString("use"))/total[6])*100, array.getJSONObject(0).getString("use") + "wh");
                    dataList.add(data);


                    mChart = (ChartProgressBar) fragment.findViewById(R.id.ChartProgressBar);

                    mChart.setDataList(dataList);
                    mChart.build();


                    //     mChart.disableBar(dataList.size() - 1);


                    //   tv_today.setText(array.getJSONObject(0).getString("use") + "wh");
                    float sum = 0;
                    float house_sum=0;
                    for (int i = 0; i < 7; i++) {
                        sum += Float.parseFloat(array.getJSONObject(i).getString("use"));
                        house_sum+=total[i];
                    }
                    String sum_ = String.format("%.2f", sum / 7);
                    tv_avg.setText(sum_ + " kwh");
house_avg.setText(String.format("%.2f",house_sum/7)+" kwh");
                    float lastWeek = 0;
                    for (int i = 7; i < 14; i++) {
                        lastWeek += Float.parseFloat(array.getJSONObject(i).getString("use"));
                    }
                    Toast.makeText(getContext(), String.valueOf(lastWeek), Toast.LENGTH_SHORT).show();
                    float result = ((sum - lastWeek) / sum);
                    if (result > 0)
                        VS.setText(String.format("%.2f", result * 100) + "% 🔺");
                    else
                        VS.setTag(String.format("%.2f", result * 100) + "% 🔻");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


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


                data.put("id", roomID); //roomID

                return data;
            }
        };
        requestQueue.add(stringRequest);
        return fragment;
    }
}