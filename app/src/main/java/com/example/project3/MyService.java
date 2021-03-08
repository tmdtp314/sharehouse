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


public class MyService extends Service {
    NotificationManager Notifi_M;
    ServiceThread thread;
    Notification Notifi;
    private int elctricity_bill;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        elctricity_bill= Integer.parseInt(intent.getStringExtra("alarmValue"));
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        thread.stopForever();
        thread = null;
    }


    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
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

            if (elctricity_bill > 10000) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_ID)
                        .setContentTitle("전력량 경보")
                        .setContentText("현재 누적 전력량 : " + elctricity_bill + "으로 위험입니다.")
                        .setSmallIcon(R.drawable.bulb);
                notificationManager.notify(0, builder.build());
                elctricity_bill = 0;
                builder.setAutoCancel(true);
                builder.setContentIntent(pendingIntent);
            }

            //소리추가
//                .defaults = Notification.DEFAULT_SOUND;
//
//                //알림 소리를 한번만 내도록
//                Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;

//                    //확인하면 자동으로 알림이 제거 되도록
//                    Notifi.flags = Notification.FLAG_AUTO_CANCEL;
//
//                    Notifi_M.notify(777, Notifi);


            //토스트 띄우기
            Toast.makeText(MyService.this, "뜸?", Toast.LENGTH_LONG).show();

        }
    }


}
