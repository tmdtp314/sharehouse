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

import com.airbnb.lottie.LottieAnimationView;
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
    NeumorphTextView tv_updown, tv_premonth, tv_thismonth, premonth, thismonth, small_pre, small_this;
    TextView tv_preMonth, tv_thisMonth;
    LottieAnimationView img_updown, img_slow;
    double[] month_temp = new double[28];
    private double pre_sum;
    ImageView pin_premonth, pin_thismonth;
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
        tv_updown = fragment.findViewById(R.id.tv_updown);
        img_updown = fragment.findViewById(R.id.img_speed);
        tv_premonth = fragment.findViewById(R.id.tv_premonth);
        tv_thismonth = fragment.findViewById(R.id.tv_thismonth);
        pin_premonth = fragment.findViewById(R.id.pin_premonth);
        pin_thismonth = fragment.findViewById(R.id.pin_thismonth);
        img_slow = fragment.findViewById(R.id.img_slow);
        small_pre = fragment.findViewById(R.id.tv_small_pre);
        small_this = fragment.findViewById(R.id.tv_small_this);
        
        tv_thisMonth=fragment.findViewById(R.id.tv_preMonth);
        tv_preMonth=fragment.findViewById(R.id.tv_preMonth);

        roomID = getArguments().getString("room");

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                JSONArray array = null;

                try {
                    Random rd = new Random();

                    month_temp = new double[]{4.2,6.6,4.6,5.2,4.6,5.7,6.3,5.15,7.2,4,4.5,4.2,4.1,5,4.8,5.3,4.4,4.2,5.6,6,5.8,4.4,4.2,4,4,4.2,3.2};




                    array = new JSONArray(response);

                    int today_value_sum = (int) Double.parseDouble(array.getJSONObject(0).getString("Value"));

                    //    Toast.makeText(getContext(), "today sum" + array.getJSONObject(0).getString("Value"), Toast.LENGTH_SHORT);

                    java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());

                    SimpleDateFormat sdf = new SimpleDateFormat("dd");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                    sdf2.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                    int result = Integer.parseInt(sdf.format(timestamp)); //오늘 날짜
                    int thismonth = Integer.parseInt(sdf2.format(timestamp));
                    pre_sum = 0;
                    for (int i = 0; i < result; i++) {

                        pre_sum += month_temp[i];

                    }
                    int pre_sum2 = Math.round((float) pre_sum);

                    tv_thismonth.setText(thismonth+"월 1일 ~"+thismonth+"월 "+result+"일까지");
                 
                    tv_preMonth.setText(thismonth-1+"월 1일 ~"+(thismonth-1)+"월 "+result+"일까지");
                    


                    if (today_value_sum > pre_sum) { //오늘치가 전월을 초과해버렸을 때

                        progressBar_VS.setMax(400);
                        progressBar_VS.setProgress(today_value_sum);
                        progressBar_VS.setSecondaryProgress((float) pre_sum);

                        progressBar_VS.setSecondaryProgressColor(Color.parseColor("#AAAAAA"));
                        progressBar_VS.setProgressColor(Color.parseColor("#BA147BDB"));


                        tv_premonth.setText(pre_sum2 + "kwh 사용");
                        double today2=Math.round((today_value_sum)*10/10);
                        tv_thismonth.setText(today2 + "kwh 사용");
                        tv_thismonth.setTextColor(Color.parseColor("#3A6CB2"));
                        img_slow.setVisibility(View.INVISIBLE);
                        img_updown.setMinAndMaxProgress(0.0f, 1.0f);
                        img_updown.playAnimation();
                        img_updown.setVisibility(View.VISIBLE);

                        float VS = Float.valueOf(today_value_sum - pre_sum2) / Float.valueOf(pre_sum2);


                        tv_updown.setText(" " + String.format("%.2f", VS * 100) + "%\n 더 빨리 소모중");
                        tv_updown.setTextColor(Color.parseColor("#F34D42"));
                        pin_premonth.animate()
                                .translationX(pre_sum2 * 2)
                                .translationY(0)
                                .setDuration(1000);

                        small_pre.animate()
                                .translationX(pre_sum2 * 2)
                                .translationY(0)
                                .setDuration(1000);

                        pin_thismonth.animate()
                                .translationX(today_value_sum * 2)
                                .translationY(0)
                                .setDuration(1000);

                        small_this.animate()
                                .translationX(today_value_sum * 2)
                                .translationY(0)
                                .setDuration(1000);


                    } else { // 저번달 보다 적게 쓰고 있을 때

                        progressBar_VS.setMax(400);
                        progressBar_VS.setProgress(today_value_sum);
                        progressBar_VS.setSecondaryProgress(pre_sum2);
                        progressBar_VS.setProgressColor(Color.parseColor("#BA147BDB"));
                        progressBar_VS.setSecondaryProgressColor(Color.parseColor("#AAAAAA"));

                        img_updown.setVisibility(View.INVISIBLE);
                        img_slow.setMinAndMaxProgress(0.0f, 1.0f);
                        img_slow.playAnimation();
                        img_slow.setVisibility(View.VISIBLE);
                        tv_premonth.setText(pre_sum2 + "kwh 사용");
                        tv_thismonth.setText(today_value_sum + "kwh 사용");
                        tv_thismonth.setTextColor(Color.parseColor("#3A6CB2"));


                        float VS = Float.valueOf(pre_sum2 - today_value_sum) / Float.valueOf(pre_sum2);
                        img_updown.setImageResource(R.drawable.down);

                        tv_updown.setText(" " + String.format("%.2f", VS * 100) + "%\n 더 천천히 소모 중");
                        tv_updown.setTextColor(Color.parseColor("#A1C84F"));

                        pin_premonth.animate()
                                .translationX(pre_sum2 * 2)
                                .translationY(0)
                                .setDuration(100);

                        small_pre.animate()
                                .translationX(pre_sum2 * 2)
                                .translationY(0)
                                .setDuration(100);

                        pin_thismonth.animate()
                                .translationX(today_value_sum * 2)
                                .translationY(0)
                                .setDuration(1000);

                        small_this.animate()
                                .translationX(today_value_sum * 2)
                                .translationY(0)
                                .setDuration(1000);
                    }

                    small_this.setText(thismonth + "월");
                    small_this.setTextColor(Color.parseColor("#1562D1"));
                    small_pre.setTextColor(Color.parseColor("#AAAAAA"));
                    small_pre.setText(thismonth - 1 + "월");
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