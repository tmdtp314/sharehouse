package com.example.project3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MyService extends Service {
    NotificationManager Notifi_M;
    ServiceThread thread;
    Notification Notifi;
    private int elctricity_bill;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    int aram;
    String user_room;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return Service.START_STICKY;
        } else {

            aram = Integer.parseInt(intent.getStringExtra("alarmValue"));
            user_room = intent.getStringExtra("user_room");
            Toast.makeText(this,aram + "뜸?" + user_room,Toast.LENGTH_SHORT).show();
        }
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        thread.stopForever();
        thread = null;
    }


    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {

            requestQueue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://172.30.1.49:8083/LoginServer/alarmServlet"; // 총 전력량을 받을 URL

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONArray array = null;
                    try {
                        array = new JSONArray(response);
                        elctricity_bill =(int)Double.parseDouble(array.getJSONObject(0).getString("ses_wh"));
                        Toast.makeText(getApplicationContext(), elctricity_bill + "뜸?", Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("user_room", user_room);
                    return data;
                }
            };
            stringRequest.setTag("MAIN");
            requestQueue.add(stringRequest);

            Intent intent = new Intent(MyService.this, fragment_mypage.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String NOTIFICATION_ID = "10001";
            String NOTIFICATION_NAME = "전력량위험알람";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, NOTIFICATION_NAME, IMPORTANCE);
                notificationManager.createNotificationChannel(channel);
            }

            if (elctricity_bill > aram) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_ID)
                        .setContentTitle("전력량 경보")
                        .setContentText("현재 누적 전력량 : " + elctricity_bill + "으로 위험입니다.")
                        .setSmallIcon(R.drawable.bulb)
                        .setVibrate(new long[]{0, 2000, 1000, 3000})
                        .setAutoCancel(true);
                notificationManager.notify(0, builder.build());
                builder.setContentIntent(pendingIntent);
                getApplicationContext().stopService(intent);
            }


            //토스트 띄우기
            Toast.makeText(MyService.this, "뜸?", Toast.LENGTH_LONG).show();

        }
    }


}