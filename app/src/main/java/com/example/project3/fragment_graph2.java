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
import android.widget.ProgressBar;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fragment_graph2 extends Fragment {
    private String roomID;
AnimatedProgressBar progressBar2;
    TextRoundCornerProgressBar progressBar;
    ValueLineChart mCubicValueLineChart;
    private LineChart mChart;
    StringRequest stringRequest;
    RequestQueue requestQueue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_graph2, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        String url = "http://172.30.1.49:8083/LoginServer/graph2Servlet";



        progressBar = fragment.findViewById(R.id.progress_bar_test);
        progressBar.setProgress(160);
        progressBar.setMax(200);
        progressBar.setSecondaryProgress(125);

progressBar2=fragment.findViewById(R.id.progressbar2);
        progressBar2.setMax(100);
        progressBar2.setProgress(75);

//        progressBar2.setTrackColor(Color.GRAY);
//        progressBar2.setProgressColor(Color.GREEN);
//        progressBar2.setProgressTipEnabled(true);
//        progressBar2.setProgressTipColor(Color.RED);
//        progressBar2.setAnimDuration(1200);
//        progressBar2.setProgressStyle(AnimatedProgressBar.ProgressStyle.WAVE);
//        progressBar2.setLineWidth(dpToPx(5, this).toInt());

        //  mCubicValueLineChart = (ValueLineChart) fragment.findViewById(R.id.cubiclinechart);
//mChart=fragment.findViewById(R.id.char1);
//                    mChart.animateX(1000);
//                    ArrayList<Entry> yVals1 = new ArrayList<>();
//                    for(int i=0;i<24;i++){
//                        float val = (float)(Math.random()*60)+150;
//                        yVals1.add(new Entry(i,val));
//                    }
//                    ArrayList<Entry> yVals2 = new ArrayList<>();
//                    for(int i=0;i<24;i++){
//                        float val = (float)(Math.random()*60)+100;
//                        yVals2.add(new Entry(i,val));
//                    }
//                    ArrayList<Entry> yVals3 = new ArrayList<>();
//                    for(int i=0;i<24;i++){
//                        float val = (float)(Math.random()*60)+50;
//                        yVals3.add(new Entry(i,val));
//                    }
//
//
//                    LineDataSet set1,set2,set3;
//                    set1=new LineDataSet(yVals1,"data set1");
//                    set1.setColor(Color.RED);
//
//                    set1.setCubicIntensity(0.8f);
//                    set1.setDrawFilled(true);
//                    set1.setCircleRadius(10f);
//                    set1.setHighLightColor(Color.parseColor("yellow"));
//                    set1.setDrawCircles(false);
//                    set1.setLineWidth(3f);
//                    set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//                    //to enable the cubic density : if 1 then it will be sharp curve
//                    set1.setCubicIntensity(0.2f);
//                    set1.setFillAlpha(80);
//                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.gradiant_blue);
//                    set1.setFillDrawable(drawable);
//
//
//                    set2=new LineDataSet(yVals2,"data set2");
//                    set2.setCubicIntensity(0.8f);
//                    set2.setDrawFilled(true);
//                    set2.setCircleRadius(10f);
//                    set2.setHighLightColor(Color.parseColor("yellow"));
//                    set2.setDrawCircles(false);
//                    set2.setLineWidth(3f);
//                    set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//                    //to enable the cubic density : if 1 then it will be sharp curve
//                    set2.setCubicIntensity(0.2f);
//                    set2.setFillAlpha(80);
//                    Drawable drawable2 = ContextCompat.getDrawable(getContext(), R.drawable.gradiant_purple);
//                    set1.setFillDrawable(drawable);
//                    set3 = new LineDataSet(yVals3,"Data Set 3");
//
//                    LineData data = new LineData(set2,set3);
//                    mChart.setData(data);
//
//

//            mChart = fragment.findViewById(R.id.char1);
        roomID = getArguments().getString("room");
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray array = null;
                try {
                    array = new JSONArray(response);


                    ValueLineSeries series = new ValueLineSeries();
                    series.setColor(0xFF56B7F1);

                    for (int k = 0; k < 24; k++) {

                        for (int i = 0; i < array.length(); i++) {
                            String[] time_ = new String[array.length()];
                            time_[i] = array.getJSONObject(i).getString("value_time");
                            if (k == Integer.parseInt(time_[i])) {
                                series.addPoint(new ValueLinePoint(time_[i] + "시", Integer.parseInt(array.getJSONObject(i).getString("value_num"))));
                            } else {
                                series.addPoint(new ValueLinePoint(k + "시", 0));
                            }

                        }
                    }

//
//                    mCubicValueLineChart.addSeries(series);
//                    mCubicValueLineChart.startAnimation();


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
        //    requestQueue.add(stringRequest);


        return fragment;
    }

}