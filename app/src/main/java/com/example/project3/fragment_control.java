package com.example.project3;

import android.app.DownloadManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
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


    TextView tvAll;
    private int number = 7;
    AppCompatImageView img_display, img_bulb, img_plug1, img_plug2, img_plug3, img_lock;
    NeumorphCardView cardView;
    //display,plug1,allOff,plug2,plug3,bulb,lock;
    LottieAnimationView allOffBtn, lottie_All, lottie_allOff;
    LottieAnimationView lottieView;
    //lottie_Display,lottie_bulb,lottie_plug1,lottie_plug2,lottie_plug3,lottie_lock;


    int[] imgID = {R.id.img_display, R.id.img_bulb, R.id.img_plug1, R.id.img_plug2, R.id.img_plug3, R.id.img_lock};
    int[] cardID = {R.id.display, R.id.bulb, R.id.plug1, R.id.plug2, R.id.plug3, R.id.lock};
    int[] lottieID = {R.id.lottie_Display, R.id.lottie_bulb, R.id.lottie_plug1, R.id.lottie_plug2, R.id.lottie_plug3, R.id.lottie_lock};


    RequestQueue requestQueue;

    StringRequest stringRequest, stringRequest2, stringRequest3;
    boolean switchOnAll = false;
    boolean checkSr1 = false;
    boolean switchOnOff = false;
    boolean switchOn1, switchOn2, switchOn3, switchOn4, switchOn5, switchOn6 = false;


    String roomID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_control, container, false);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        tvAll = fragment.findViewById(R.id.tvAll);

        lottie_All = fragment.findViewById(R.id.lottie_all);
        roomID = getArguments().getString("room");
        tvAll.bringToFront();

        String url = "http://172.30.1.49:8083/LoginServer/sensorCon";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] responses = response.split("/");
                AppCompatImageView imgView = fragment.findViewById(imgID[number - 1]);
                LottieAnimationView lottieView = fragment.findViewById(lottieID[number - 1]);
                LottieAnimationView lottieView2=fragment.findViewById(R.id.lottie_all);
                NeumorphCardView cardView = fragment.findViewById(cardID[number - 1]);
                TextView tvAll=fragment.findViewById(R.id.tvAll);


//                Toast.makeText(getContext(),responses[1],Toast.LENGTH_SHORT).show();
                if (responses[1].equals("on")) {

                    imgView.setColorFilter(Color.parseColor("#2D7DF6"));
                    lottieView.setMinAndMaxProgress(0.0f, 1.0f);
                    lottieView.playAnimation();
                    cardView.setShapeType(1);
                    tvAll.setText(responses[2]+" Devices On..");


                } else if (responses[1].equals("off")) {

                    imgView.setColorFilter(Color.parseColor("#7D818A"));
                    lottieView.setMinAndMaxProgress(0.0f, 0.0f);
                    lottieView.playAnimation();
                    cardView.setShapeType(0);
                    tvAll.setText(responses[2]+" Devices On..");
                    if(responses[2].equals("0"))
                    {
                        tvAll.setText("All OFF");
                        lottieView2.setMinAndMaxProgress(0.0f,0.0f);
                        lottieView2.playAnimation();

                    }
                    else{
                        tvAll.setText(responses[2]+"Devices On...");
                        lottieView2.setMinAndMaxProgress(0.0f,1.0f);
                        lottieView2.playAnimation();
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
                } else if (checkSr1 == false) {
                    sensor.put("onOff", "off");
                    sensor.put("sr", "sr" + String.valueOf(number));
                }

                return sensor;
            }
        };
        String url2 = "http://172.30.1.49:8083/LoginServer/howMany";
        stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int result = Integer.parseInt(response);
                TextView tvAll = fragment.findViewById(R.id.tvAll);
                LottieAnimationView lottieView = fragment.findViewById(R.id.lottie_all);
                if (result>0) {

                    tvAll.setText(result + " Devices on");
                    tvAll.bringToFront();
                } else {

                    tvAll.setText("All OFF");
                    tvAll.bringToFront();
                    lottieView.setMinAndMaxProgress(0.0f,0.0f);
                    lottieView.playAnimation();
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

                sensor.put("how", "how");


                return sensor;
            }
        };


        requestQueue.add(stringRequest2);


        for (int i = 0; i < cardID.length; i++) {
            NeumorphCardView cardView;
            AppCompatImageView imgView;
            // LottieAnimationView lottieView;
            imgView = fragment.findViewById(imgID[i]);
            //  lottieView=fragment.findViewById(lottieID[i]);
            cardView = fragment.findViewById(cardID[i]);
            //   lottieView.setMinAndMaxProgress(0.0f,0.0f);
            //   lottieView.playAnimation();
            cardView.setTag(i + 1);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    number = (Integer) cardView.getTag();
//                    if (checkSr1 == true)
//                        cardView.setShapeType(0);
//                    else
//                        cardView.setShapeType(1);
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
        }

        return fragment;
    }
}