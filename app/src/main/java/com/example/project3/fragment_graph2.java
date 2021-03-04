package com.example.project3;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mckrpk.animatedprogressbar.AnimatedProgressBar;


import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONException;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import soup.neumorphism.NeumorphTextView;

import static java.lang.System.currentTimeMillis;

public class fragment_graph2 extends Fragment {
    private String roomID;
    NeumorphTextView tv_updown,tv_premonth,tv_thismonth,premonth,thismonth;
    ImageView img_updown;
    int[] month_temp = new int[28];
    private int pre_sum;

    TextRoundCornerProgressBar progressBar_VS;
    ValueLineChart mCubicValueLineChart;
    private LineChart mChart;
    StringRequest stringRequest;
    RequestQueue requestQueue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_graph2, container, false);
        requestQueue = Volley.newRequestQueue(getContext());

        String url = "http://172.30.1.49:8083/LoginServer/newgraph2Con";



        progressBar_VS = fragment.findViewById(R.id.progress_bar_VS);
        tv_updown=fragment.findViewById(R.id.tv_updown);
        img_updown=fragment.findViewById(R.id.img_updown);
        tv_premonth=fragment.findViewById(R.id.tv_premonth);
        tv_thismonth=fragment.findViewById(R.id.tv_thismonth);





        roomID = getArguments().getString("room");
        Toast.makeText(getContext(),"roomID 들어옴"+roomID,Toast.LENGTH_SHORT).show();
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                JSONArray array = null;

                try {
                    Random rd = new Random();

                    for (int i = 0; i < month_temp.length; i++) {
                        month_temp[i] = (int) rd.nextInt(50);
                    }


                    array = new JSONArray(response);

                    int today_value_sum=Integer.parseInt(array.getJSONObject(0).getString("Value"));

                    Toast.makeText(getContext(),"today sum"+array.getJSONObject(0).getString("Value"),Toast.LENGTH_SHORT);

                    java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());

                    SimpleDateFormat sdf = new SimpleDateFormat( "dd");
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                    int result=Integer.parseInt(sdf.format(timestamp));

                    pre_sum=0;
                    for(int i=0;i<result;i++){

                        pre_sum+=month_temp[i];
                    }

                    Toast.makeText(getContext(),month_temp[0]+"pre_sum",Toast.LENGTH_SHORT).show();

                    if(today_value_sum>pre_sum){ //오늘치가 전월을 초과해버렸을 때
                        progressBar_VS.setMax(100);
                        progressBar_VS.setProgress(today_value_sum);
                        progressBar_VS.setSecondaryProgress(pre_sum);

                        progressBar_VS.setSecondaryProgressColor(Color.parseColor("#65CCB9")); //지난 달것 투명처리
                        progressBar_VS.setProgressColor(Color.parseColor("#FBBC1C"));
                        progressBar_VS.setProgressText(today_value_sum+"wh");


                        tv_premonth.setText(pre_sum+"wh");
                        tv_thismonth.setText(today_value_sum+"wh");
                        tv_thismonth.setTextColor(Color.parseColor("#F34D42"));


                        int VS=((today_value_sum-pre_sum)/pre_sum)*100;
                        img_updown.setImageResource(R.drawable.up);
                        tv_updown.setText(VS+"% 더 쓰는 중");
                        tv_updown.setTextColor(Color.parseColor("#F34D42"));



                    } else{ // 저번달 보다 적게 쓰고 있을 때

                        progressBar_VS.setProgress(today_value_sum);
                        progressBar_VS.setSecondaryProgress(pre_sum);
                        progressBar_VS.setProgressColor(Color.parseColor("#A4CE6F"));
                        progressBar_VS.setSecondaryProgressColor(Color.parseColor("#D2E2BD"));
                        progressBar_VS.setMax(100);
                        progressBar_VS.setProgressText(today_value_sum+"wh");

                        tv_premonth.setText(pre_sum+"wh");
                        tv_thismonth.setText(today_value_sum+"wh");
                        tv_thismonth.setTextColor(Color.parseColor("#A4CE6F"));


                        float VS=-1*(today_value_sum-pre_sum/pre_sum)*100;
                        img_updown.setImageResource(R.drawable.down);
                        tv_updown.setText(VS+"% 덜 쓰는 중");
                        tv_updown.setTextColor(Color.parseColor("#A1C84F"));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
////
////                    ValueLineSeries series = new ValueLineSeries();
////                    series.setColor(0xFF56B7F1);
////
////                    for (int k = 0; k < 24; k++) {
////
////                        for (int i = 0; i < array.length(); i++) {
////                            String[] time_ = new String[array.length()];
////                            time_[i] = array.getJSONObject(i).getString("value_time");
////                            if (k == Integer.parseInt(time_[i])) {
////                                series.addPoint(new ValueLinePoint(time_[i] + "시", Integer.parseInt(array.getJSONObject(i).getString("value_num"))));
////                            } else {
////                                series.addPoint(new ValueLinePoint(k + "시", 0));
////                            }
//
//                        }
//                    }
//
////
////                    mCubicValueLineChart.addSeries(series);
////                    mCubicValueLineChart.startAnimation();
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

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
           requestQueue.add(stringRequest);

        return fragment;
    }

}