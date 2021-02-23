package com.example.project3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hadiidbouk.charts.BarData;
import com.hadiidbouk.charts.ChartProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fragment_graph1 extends Fragment {
ChartProgressBar mChart;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_graph1, container, false);

        requestQueue = Volley.newRequestQueue(getContext()); //현재 페이지 정보 보내주는것
        String url = "http://172.30.1.49:8083/LoginServer/graph1Servlet";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray array = null;
                try {
                    array = new JSONArray(response);
                    String month = array.getJSONObject(0).getString("month");


                    ArrayList<BarData> dataList = new ArrayList<>();

                    BarData data = new BarData("1월",Float.parseFloat(array.getJSONObject(0).getString("mon_per")) , "23342원");
                    dataList.add(data);

                    data = new BarData("2월", Float.parseFloat(array.getJSONObject(1).getString("mon_per")), "11111원");
                    dataList.add(data);

                    data = new BarData("3월", Float.parseFloat(array.getJSONObject(2).getString("mon_per")), "22222원");
                    dataList.add(data);

                    data = new BarData("4월", Float.parseFloat(array.getJSONObject(3).getString("mon_per")), "33333원");
                    dataList.add(data);

                    data = new BarData("5월", Float.parseFloat(array.getJSONObject(4).getString("mon_per")), "44444원");
                    dataList.add(data);



                    mChart = (ChartProgressBar)fragment.findViewById(R.id.ChartProgressBar);

                    mChart.setDataList(dataList);
                    mChart.build();

                    mChart.disableBar(dataList.size() - 1);




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

            data.put("room", "a");

            return data;
        }
    };

        requestQueue.add(stringRequest);
        return fragment;
    }
}