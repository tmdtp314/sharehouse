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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.github.mikephil.charting.utils.ColorTemplate.LIBERTY_COLORS;

public class fragment_graph2 extends Fragment {
    private String roomID;
    BarChart barChart;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    String[] dayname;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //  roomID=getArguments().getString("room_graph2");
        View fragment = inflater.inflate(R.layout.fragment_graph2, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        barChart = fragment.findViewById(R.id.barChart);
        String url = "http://172.30.1.49:8083/LoginServer/weekGraph";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray array = null;
                try {

                    array = new JSONArray(response);
                    barChart.setDrawBarShadow(false);
                    barChart.setDrawValueAboveBar(true);
                    // barChart.setMaxVisibleValueCount();
                    barChart.setPinchZoom(false);
                    barChart.setDrawGridBackground(true);
//                    String before2 = array.getJSONObject(0).getString("use");

                    ArrayList<BarEntry> barEntryArrayList;


                    Toast.makeText(getContext(), "graph2" + array.getJSONObject(0).getString("use"), Toast.LENGTH_SHORT).show();
                    barEntryArrayList = new ArrayList<>();
                    barEntryArrayList.add(new BarEntry(1, Float.parseFloat(array.getJSONObject(0).getString("use"))));
                    barEntryArrayList.add(new BarEntry(2, Float.parseFloat(array.getJSONObject(1).getString("use"))));
                    barEntryArrayList.add(new BarEntry(3, Float.parseFloat(array.getJSONObject(2).getString("use"))));
                    barEntryArrayList.add(new BarEntry(4, Float.parseFloat(array.getJSONObject(3).getString("use"))));
                    barEntryArrayList.add(new BarEntry(5, Float.parseFloat(array.getJSONObject(4).getString("use"))));
                    barEntryArrayList.add(new BarEntry(6, Float.parseFloat(array.getJSONObject(5).getString("use"))));
                    barEntryArrayList.add(new BarEntry(7, 27f));


                    BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Data set1");
                    barDataSet.setColor(Color.parseColor("#2D7DF6"));
                    BarData data = new BarData(barDataSet);
                    data.setBarWidth(0.8f);
                    barChart.setData(data);

                    String result = "";
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE");
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                    result = sdf.format(timestamp);

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

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new MyXAxisValueFormatter(dayname));
                    xAxis.setPosition(XAxis.XAxisPosition.TOP);
                    xAxis.setAxisMinimum(1);
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setGranularity(1);
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

                roomID = "a";
                data.put("id", roomID); //roomID
                return data;
            }
        };
        requestQueue.add(stringRequest);


        return fragment;
    }

    public class MyXAxisValueFormatter extends IndexAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }
}