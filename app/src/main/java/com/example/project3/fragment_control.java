package com.example.project3;

import android.app.DownloadManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

import soup.neumorphism.NeumorphCardView;


public class fragment_control extends Fragment {


    fragment_elect fr_elct;
    private int gaesu;
    private String room;
    //***************
    TextView tvAll;
    private int number = 6;
    AppCompatImageView img_display, img_bulb, img_plug1, img_plug2, img_plug3, img_lock;
    NeumorphCardView allOffBtn;

    LottieAnimationView lottie_All, lottie_allOff;


   LottieAnimationView l1,l2,l3,l4,l5,l6;

    int[] imgID = {R.id.img_display, R.id.img_bulb, R.id.img_plug1, R.id.img_lock}; //R.id.img_plug2, R.id.img_plug3,
    int[] cardID = {R.id.display, R.id.bulb, R.id.plug1, R.id.lock}; // R.id.plug2, R.id.plug3,
    int[] lottieID = {R.id.lottie_Display, R.id.lottie_bulb, R.id.lottie_plug1, R.id.lottie_lock}; //, R.id.lottie_plug2, R.id.lottie_plug3


    RequestQueue requestQueue;

    StringRequest stringRequest, stringRequest2, stringRequest3;
    boolean switchOffAll = false;
    boolean checkSr1 = false;
    boolean switchOn1, switchOn2, switchOn3, switchOn4, switchOn5, switchOn6 = false;
    //  boolean[] switches={switchOn1,switchOn2,switchOn3,switchOn4,switchOn5,switchOn6};


    String roomID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_control, container, false);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        int temp = 3;
       // room = "a";//room = getArguments().getString("room");
        tvAll = fragment.findViewById(R.id.tvAll);
        lottie_allOff = fragment.findViewById(R.id.lottie_allOff);
        lottie_All = fragment.findViewById(R.id.lottie_all);
        allOffBtn = fragment.findViewById(R.id.allOff);
        room = getArguments().getString("room");
        Toast.makeText(getContext(),room,Toast.LENGTH_SHORT).show();
        tvAll.bringToFront();

        lottie_allOff.setMinAndMaxProgress(0.5f, 0.5f);
        lottie_allOff.playAnimation();

        for(int i=0;i<lottieID.length;i++){

            fragment.findViewById(lottieID[i]).setVisibility(View.INVISIBLE);
        }
        lottie_All.setVisibility(View.INVISIBLE);

        String url2 = "http://172.30.1.49:8083/LoginServer/howMany";
        stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { //여기를 개선하자. !!!!!!!!!!!!!!!!
                String[] responses = response.split("/");
                gaesu = Integer.parseInt(responses[0]);

                TextView tvAll = fragment.findViewById(R.id.tvAll);
                LottieAnimationView lottieView = fragment.findViewById(R.id.lottie_all);
                LottieAnimationView lottie_allOff = fragment.findViewById(R.id.lottie_allOff);

                for (int i = 1; i < responses.length; i++) {
                    int result = responses.length - 1;
                    if (response.length() > 2) {

                        int chogi = Integer.parseInt(responses[i].substring(2));

                        LottieAnimationView lottieViewChogi = fragment.findViewById(lottieID[chogi - 1]);
                        lottieViewChogi.setMinAndMaxProgress(0.0f,1.0f);
                        lottieViewChogi.playAnimation();
                        lottieViewChogi.setVisibility(View.VISIBLE);
                        tvAll.setText(result + " Devices On..");


                        lottie_All.setMinAndMaxProgress(0.0f,1.0f);
                        lottie_All.playAnimation();
                        lottie_All.setVisibility(View.VISIBLE);

                        AppCompatImageView imgChogi = fragment.findViewById(imgID[chogi - 1]);
                        imgChogi.setColorFilter(Color.parseColor("#2D7DF6"));
                        NeumorphCardView cardViewChogi = fragment.findViewById(cardID[chogi - 1]);
                        cardViewChogi.setShapeType(1);


                    }


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> sensor = new HashMap<>();

                sensor.put("roomId", room);


                return sensor;
            }
        };


        requestQueue.add(stringRequest2);
        for (int i = 0; i < temp; i++) {
            NeumorphCardView cardView;
//            AppCompatImageView imgView;
//            LottieAnimationView lottieView;
//            imgView = fragment.findViewById(imgID[i]);

            cardView = fragment.findViewById(cardID[i]);

            cardView.setTag(i + 1);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    number = (Integer) cardView.getTag();

                    switch (number) {
                        case 1:
                            switchOn1 = !switchOn1;
                            checkSr1 = switchOn1;

                            break;
                        case 2:
                            switchOn2 = !switchOn2;
                            checkSr1 = switchOn2;
                            break;
                        case 3:
                            switchOn3 = !switchOn3;
                            checkSr1 = switchOn3;
                            break;
                        case 4:
                            switchOn4 = !switchOn4;
                            checkSr1 = switchOn4;
                            break;
                        case 5:
                            switchOn5 = !switchOn5;
                            checkSr1 = switchOn5;
                            break;
                        case 6:
                            switchOn6 = !switchOn6;
                            checkSr1 = switchOn6;
                            break;
                    }


                    Toast.makeText(getContext(), String.valueOf(number), Toast.LENGTH_SHORT).show();
                    String booleans = Boolean.toString(checkSr1);
                    Toast.makeText(getContext(), booleans, Toast.LENGTH_SHORT).show();
                    stringRequest.setTag("sr" + number + booleans);
                    requestQueue.add(stringRequest);

                }
            });

            String url = "http://172.30.1.49:8083/LoginServer/sensorCon";
            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String[] responses = response.split("/");
                    AppCompatImageView imgView = fragment.findViewById(imgID[number - 1]);
                    LottieAnimationView lottieView = fragment.findViewById(lottieID[number - 1]);
                  //  LottieAnimationView lottieView2 = fragment.findViewById(R.id.lottie_all);


                    NeumorphCardView cardView = fragment.findViewById(cardID[number - 1]);
                    TextView tvAll = fragment.findViewById(R.id.tvAll);


//
                    if (responses[1].equals("on")) {

                        imgView.setColorFilter(Color.parseColor("#0368FF"));
                        lottieView.setMinAndMaxProgress(0.0f, 1.0f);
                        lottieView.playAnimation();
                        lottieView.setVisibility(View.VISIBLE);
                        cardView.setShapeType(1);
                        tvAll.setText(responses[2] + " Devices On..");
                        lottie_allOff.setMinAndMaxProgress(0.0f, 0.0f);
                        lottie_All.setMinAndMaxProgress(0.0f, 1.0f);
                        lottie_All.playAnimation();
                        lottie_All.setVisibility(View.VISIBLE);


                    } else if (responses[1].equals("off")) {

                        imgView.setColorFilter(Color.parseColor("#7D818A"));
                        lottieView.setMinAndMaxProgress(0.0f, 0.0f);
                        lottieView.playAnimation();
                        cardView.setShapeType(0);
                        tvAll.setText(responses[2] + " Devices On..");
                        if (responses[2].equals("0")) {
                            tvAll.setText("All OFF");

                            lottie_All.setVisibility(View.INVISIBLE);
                            lottieView.setMinAndMaxProgress(0.0f, 0.0f);
                            lottieView.playAnimation();

                            lottie_allOff.setMinAndMaxProgress(0.0f, 0.5f);
                            lottie_allOff.playAnimation();


                        } else {
                            tvAll.setText(responses[2] + " Devices On...");
                            lottie_All.setMinAndMaxProgress(0.0f, 1.0f);
                            lottie_All.playAnimation();
                            lottie_All.setVisibility(View.VISIBLE);

                        }

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> sensor = new HashMap<>();

                    if (checkSr1 == true) {
                        sensor.put("onOff", "on");
                        sensor.put("sr", "sr" + String.valueOf(number));
                        sensor.put("roomId", room);
                    } else {
                        sensor.put("onOff", "off");
                        sensor.put("sr", "sr" + String.valueOf(number));
                        sensor.put("roomId", room);
                    }

                    return sensor;
                }
            };


            String url3 = "http://172.30.1.49:8083/LoginServer/allOff";
            stringRequest3 = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    int result = Integer.parseInt(response);
                    if (result > 0) {
                        for (int i = 0; i < 3; i++) {
                            LottieAnimationView lottie = fragment.findViewById(lottieID[i]);
                            NeumorphCardView cardView = fragment.findViewById(cardID[i]);
                            AppCompatImageView img = fragment.findViewById(imgID[i]);
                            lottie.setMinAndMaxProgress(0.0f, 0.0f);
                            lottie.playAnimation();
                            cardView.setShapeType(0);
                            img.setColorFilter(Color.parseColor("#7D818A"));
                            tvAll.setText("All OFF");
                            lottie_All.setMinAndMaxProgress(0.0f, 0.0f);
                            lottie_All.playAnimation();
                            lottie_allOff.setMinAndMaxProgress(0.0f, 0.5f);
                            lottie_allOff.playAnimation();

                        }


                    } else {
                        Toast.makeText(getContext(), "전원 off실패", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> sensor = new HashMap<>();

                    sensor.put("alloff", "a"); //a는 방 아이디


                    return sensor;
                }
            };


            allOffBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestQueue.add(stringRequest3);
                }
            });

        }
        temp = gaesu;
        return fragment;
    }

}