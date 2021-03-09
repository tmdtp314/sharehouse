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
    TextView tv_today, tv_avg, house_avg;
    StringRequest stringRequest, stringRequest2;
    String result;
    BarData bardata;
    String[] dayname;
    float[] total;
    float[] use;
    float[] result2;
    String roomID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_graph1, container, false);

        roomID = getArguments().getString("room");

        requestQueue = Volley.newRequestQueue(getContext()); //현재 페이지 정보 보내주는것
        house_avg = fragment.findViewById(R.id.house_avg);

        tv_avg = fragment.findViewById(R.id.tv_avg);
        //  tv_today = fragment.findViewById(R.id.tv_today);
        VS = fragment.findViewById(R.id.VS);

        String url = "http://172.30.1.49:8083/LoginServer/weekGraph";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                result = "Tue";
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("EEE");


                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));




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

                JSONArray array = null;
                try {

                    array = new JSONArray(response);

                    ArrayList<BarData> dataList = new ArrayList<>();

                    total = new float[array.length()];
                    use = new float[array.length()];
                    for (int i = 0; i < array.length(); i++) {

                        total[i] = Float.parseFloat(array.getJSONObject(i).getString("total"));
                        use[i] = Float.parseFloat(array.getJSONObject(i).getString("use"));

                    }


                    BarData data = new BarData(dayname[0], ((float)use[6] / (float)total[6]) * 100, (int)(((float)use[6] / (float)total[6])*100) + "%");
                    dataList.add(data);

                    data = new BarData(dayname[1], ((float)use[5] / (float)total[5]) * 100,(int)(((float)use[5] / (float)total[5])*100) + "%");
                    dataList.add(data);

                    data = new BarData(dayname[2], ((float)use[4] / (float)total[4]) * 100, (int)(((float)use[4] / (float)total[4])*100) + "%");
                    dataList.add(data);

                    data = new BarData(dayname[3], ((float)use[3] / (float)total[3]) * 100, (int)(((float)use[3] / (float)total[3])*100) + "%");
                    dataList.add(data);

                    data = new BarData(dayname[4], ((float)use[2] / (float)total[2]) * 100, (int)(((float)use[2] / (float)total[2])*100) + "%");
                    dataList.add(data);

                    data = new BarData(dayname[5], ((float)use[1] / (float)total[1]) * 100, (int)(((float)use[1] / (float)total[1])*100) + "%");
                    dataList.add(data);

                    data = new BarData("오늘", ((float)use[0] / (float)total[0]) * 100, (int)(((float)use[0] / (float)total[0])*100) + "%");
                    dataList.add(data);


                    mChart = (ChartProgressBar) fragment.findViewById(R.id.ChartProgressBar);

                    mChart.setDataList(dataList);
                    mChart.build();


                    //     mChart.disableBar(dataList.size() - 1);


                    //   tv_today.setText(array.getJSONObject(0).getString("use") + "wh");
                    float sum = 0;
                    float house_sum = 0;
                    for (int i = 0; i < 7; i++) {
                        sum += use[i];
                        house_sum += total[i];
                    }
                    String sum_ = String.format("%.2f", (float)sum / 7);
                    tv_avg.setText(sum_ + " kwh");
                    house_avg.setText(String.format("%.2f",(float)house_sum / 7) + " kwh");
                    float lastWeek = 0;
                    for (int i = 7; i < 14; i++) {
                        lastWeek += Float.parseFloat(array.getJSONObject(i).getString("use"));
                    }

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