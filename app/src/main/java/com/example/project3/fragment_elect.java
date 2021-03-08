package com.example.project3;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import at.grabner.circleprogress.CircleProgressView;
import soup.neumorphism.NeumorphCardView;


public class fragment_elect extends Fragment {
    MyPagerAdapter adapter;
    private FragmentPagerAdapter fragmentPagerAdapter;
    View frag_grap1_2;
    ViewPager viewPager;
    private String room;
    NeumorphCardView reload,room_color;
    LottieAnimationView lottie_reload;
    TextView tv1, room_col;
    TabLayout tabLayout;
    Fragment frame_2;
    //CircleProgressView circle;
    PieChart pieChart;
    String total;
    private String result;
    RequestQueue requestQueue;
    StringRequest stringRequest;

    Button btn_new;

    ArrayList<String> data2 = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
room=getArguments().getStringArrayList("data").get(2);





        adapter = new MyPagerAdapter(getChildFragmentManager(),room);
        adapter.addItem(new fragment_graph1());
        adapter.addItem(new fragment_graph2());
        adapter.addItem(new fragment_graph3());

        View fragment = inflater.inflate(R.layout.fragment_elect, container, false);

       // frag_grap1_2 = fragment.findViewById(R.id.frag_graph2);
        viewPager = (ViewPager) (fragment.findViewById(R.id.Viewpager));
        tabLayout = fragment.findViewById(R.id.tab_layout);

        viewPager.setOffscreenPageLimit(3);






        room_color=fragment.findViewById(R.id.room_color);
        tv1 = fragment.findViewById(R.id.date);
        result = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("M월 dd일");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        result = sdf.format(timestamp);
        tv1.setText(result);

        reload = fragment.findViewById(R.id.reload);
        reload.bringToFront();

        room_col = fragment.findViewById(R.id.room_col);
        if(room.equals("a")){
        room_col.setTextColor(Color.WHITE);
        room_color.setBackgroundColor(Color.parseColor("#0085FF"));
        room_col.setText("room A");}
        else if(room.equals("b")){
            room_col.setBackgroundColor(Color.parseColor("#86C7BF"));
            room_col.setText("room B");
        }
        else{
            room_col.setBackgroundColor(Color.parseColor("#8FA19F"));
            room_col.setText("room C");
        }



        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        // ViewPage 바꿔줄 그래프 Fragment ( Fragment_Grap1_1, Fragment_Grap1_2)

        adapter.notifyDataSetChanged();
        pieChart = fragment.findViewById(R.id.pieChart);


        requestQueue = Volley.newRequestQueue(getContext()); //현재 페이지 정보 보내주는것
        String url = "http://172.30.1.49:8083/LoginServer/usePercentage";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //응답 받아오는것
                //jin성공
                try {

                    JSONArray array = new JSONArray(response);
                    String user1 = array.getJSONObject(0).getString("value");
                    String user2 = array.getJSONObject(1).getString("value");
                    String user3 = array.getJSONObject(2).getString("value");


                    float user1_=Float.parseFloat(user1);
                    float user2_=Float.parseFloat(user2);
                    float user3_=Float.parseFloat(user3);
                    float sum=user1_+user2_+user3_;

                  if(sum>200&&sum<400){
                      total="누적 1단계";
                  }else if(sum>400){
                      total="누적 2단계";
                  }






                    pieChart.setUsePercentValues(true);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setExtraOffsets(5, 5, 5, 0);
                    pieChart.setDrawHoleEnabled(true);

                    pieChart.setTransparentCircleRadius(40f);


                    ArrayList<PieEntry> yValues = new ArrayList<>();
                    yValues.add(new PieEntry(Float.parseFloat(user1), "roomA")); //여기다가 기존거 더해주자.
                    yValues.add(new PieEntry(Float.parseFloat(user2), "roomB")); //여기다가 가데이터 기존 양 더해주기.
                    yValues.add(new PieEntry(Float.parseFloat(user3), "roomC")); //+ 가데이터



                    pieChart.setHoleRadius(45);

                    pieChart.animateY(1000, Easing.EaseInCubic);

                    PieDataSet dataSet = new PieDataSet(yValues, " '방 별 누적 사용현황'");
                    dataSet.setSliceSpace(1f);
                    dataSet.setSelectionShift(5f);


                    int[] myColors={Color.parseColor("#0085FF"),Color.parseColor("#BEBEBE"),Color.parseColor("#8D8D8D")};
                    dataSet.setColors(myColors);
                    dataSet.setHighlightEnabled(true);
                    PieData data = new PieData((dataSet));
                    data.setValueTextSize(14f);
                    data.setValueTextColor(Color.parseColor("black"));

                    data.setValueFormatter(new PercentFormatter(pieChart));
                    pieChart.setUsePercentValues(true);

                    Legend l = pieChart.getLegend();
                    l.setTextSize(16f);
                    l.setForm(Legend.LegendForm.CIRCLE);
                    pieChart.setCenterText(total);
                    pieChart.setCenterTextSize(14);
                    pieChart.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                    if(total.contains("1단계")){
                    pieChart.setCenterTextColor(Color.parseColor("#099527"));}
                    else if(total.contains("2단계")){
                        pieChart.setCenterTextColor(Color.parseColor("#CF8009"));
                    }
                    pieChart.setHoleColor(Color.parseColor("#E4EBF5"));
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


        stringRequest.setTag("MAIN");
        requestQueue.add(stringRequest);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     Toast.makeText(getContext(), "들어오나", Toast.LENGTH_SHORT).show();
                requestQueue.add(stringRequest);
            }
        });

        return fragment;
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> items = new ArrayList<Fragment>();
     String room;

        public MyPagerAdapter(@NonNull FragmentManager fm,String room) {
            super(fm);
            this.room = room;
           // Toast.makeText(getContext(), "어댑터 안"+arg, Toast.LENGTH_SHORT).show();
        }


        public void addItem(Fragment item) {

            items.add(item);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                fragment_graph1 fr = (fragment_graph1) items.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("room",room);
                fr.setArguments(bundle);

                return fr;
            } else if(position==1) {
                fragment_graph2 fr2 = (fragment_graph2) items.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("room",room);
                fr2.setArguments(bundle);
                return fr2;
            } else if(position==2){
                fragment_graph3 fr3 = (fragment_graph3) items.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("room",room);
                fr3.setArguments(bundle);
                return fr3;
            }
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }

}