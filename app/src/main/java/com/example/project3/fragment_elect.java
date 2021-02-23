package com.example.project3;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import at.grabner.circleprogress.CircleProgressView;


public class fragment_elect extends Fragment {

    private FragmentPagerAdapter fragmentPagerAdapter;
    View frag_grap1_2;
    ViewPager viewPager;
    TabLayout tabLayout;
    Fragment frame_2;
    //CircleProgressView circle;
    PieChart pieChart;

    RequestQueue requestQueue;
    StringRequest stringRequest;
    Button btn_new;

    ArrayList<String> data2 = new ArrayList<>();

    public static fragment_elect newInstance() {
        fragment_elect fragment_elect = new fragment_elect();


        return fragment_elect;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View fragment = inflater.inflate(R.layout.fragment_elect, container, false);

        frag_grap1_2 = fragment.findViewById(R.id.frag_graph2);
        viewPager = (ViewPager) (fragment.findViewById(R.id.Viewpager));
        tabLayout = fragment.findViewById(R.id.tab_layout);

        viewPager.setOffscreenPageLimit(2);

        MyPagerAdapter adapter = new MyPagerAdapter((getActivity().getSupportFragmentManager()), 2);


        // ViewPage 바꿔줄 그래프 Fragment ( Fragment_Grap1_1, Fragment_Grap1_2)
        adapter.addItem(new fragment_graph1());
        adapter.addItem(new fragment_graph2());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        pieChart = fragment.findViewById(R.id.pieChart);


        requestQueue = Volley.newRequestQueue(getContext()); //현재 페이지 정보 보내주는것
        String url = "http://172.30.1.49:8083/LoginServer/usePercentage";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //응답 받아오는것
                //jin성공
                try {
                    //여기서 roomID bundle값 받아와서 a면 0, b면 1 c면 2 이런거 해줄예정.
                    JSONArray array = new JSONArray(response);
                    String user1 = array.getJSONObject(0).getString("value");
                    String user2 = array.getJSONObject(1).getString("value");
                    String user3 = array.getJSONObject(2).getString("value");

                    pieChart.setUsePercentValues(true);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setExtraOffsets(5, 5, 5, 0);
                    pieChart.setDrawHoleEnabled(true);

                    pieChart.setTransparentCircleRadius(22f);


                    ArrayList<PieEntry> yValues = new ArrayList<>();
                    yValues.add(new PieEntry(Float.parseFloat(user1), "roomA"));
                    yValues.add(new PieEntry(Float.parseFloat(user2), "roomB"));
                    yValues.add(new PieEntry(Float.parseFloat(user3), "roomC"));

//                    Description description=new Description();
//                    description.setText("실시간 전력 사용량 : "+user1);
//                    description.setTextSize(17);
//
//                    description.setTextColor(Color.parseColor("#2D7DF6"));
//                    pieChart.setDescription(description);
//pieChart.highlightValue(1,0,false);

                    pieChart.setHoleRadius(65);

                    pieChart.animateY(1000, Easing.EaseInCubic);

                    PieDataSet dataSet = new PieDataSet(yValues, "   '방 별 사용현황'");
                    dataSet.setSliceSpace(1f);
                    dataSet.setSelectionShift(5f);

                    dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                    PieData data = new PieData((dataSet));
                    data.setValueTextSize(14f);
                    data.setValueTextColor(Color.parseColor("black"));

                    data.setValueFormatter(new PercentFormatter(pieChart));
                    pieChart.setUsePercentValues(true);

                    Legend l = pieChart.getLegend();
                    l.setTextSize(16f);
                    l.setForm(Legend.LegendForm.CIRCLE);
                    pieChart.setCenterText(user1 + "kwh");
                    pieChart.setCenterTextSize(19);
                    pieChart.setCenterTextColor(Color.parseColor("#713993"));


                    pieChart.setHoleColor(Color.parseColor("#F8F9FE"));
                    pieChart.setData(data);


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

                data.put("circle", "1");

                return data;
            }
        };//여기까지가 생성자 호출
//        //1.첫번째 매개변수, Method(get?post?service)
//        //2번째 매개변수:url
//        //3번째 매개변수: 응답을 감지하는 리스너
//        //4번째 매개변수 : 에러 감지하는 리스너
//
//
//        //태그 달아주기

//        btn_new.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stringRequest.setTag("MAIN");
//                requestQueue.add(stringRequest);
//            }
//        });

        //   data2 = getArguments().getStringArrayList("data2");
        stringRequest.setTag("MAIN");
        requestQueue.add(stringRequest);
        return fragment;
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public MyPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }

}