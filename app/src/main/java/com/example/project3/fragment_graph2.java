package com.example.project3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.sql.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.github.mikephil.charting.utils.ColorTemplate.LIBERTY_COLORS;

public class fragment_graph2 extends Fragment {
    private String roomID;
    ValueLineChart mCubicValueLineChart;
    RadarChart radarChart;
    StringRequest stringRequest;
    RequestQueue requestQueue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //  roomID=getArguments().getString("room_graph2");
        View fragment = inflater.inflate(R.layout.fragment_graph2, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        String url = "http://172.30.1.49:8083/LoginServer/graph2Servlet";
        mCubicValueLineChart = (ValueLineChart) fragment.findViewById(R.id.cubiclinechart);
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


                    mCubicValueLineChart.addSeries(series);
                    mCubicValueLineChart.startAnimation();


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
        requestQueue.add(stringRequest);


        return fragment;
    }


}