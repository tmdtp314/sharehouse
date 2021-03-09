package com.example.project3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class TalkActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        linearLayout=findViewById(R.id.splashactivity_linearlayout);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.default_config);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                        } else {

                        }

                        displayMessage();
                    }
                });
        Log.v("data_tag",mFirebaseRemoteConfig.getString("splash_background"));
    }
    void displayMessage(){
        String splash_background=mFirebaseRemoteConfig.getString("splash_background");
        Log.v("data_tag",splash_background);
        boolean caps = mFirebaseRemoteConfig.getBoolean("splash_message_caps");
        String splash_message=mFirebaseRemoteConfig.getString("splash_message");
        Log.v("data_tag",splash_message);
//        linearLayout.setBackgroundColor(Color.parseColor(splash_background));

//        if(caps){
//            AlertDialog.Builder builder=new AlertDialog.Builder(this);
//            builder.setMessage(splash_message).setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    finish();
//                }
//            });
//            builder.create().show();
//        }
        if (caps){

            SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
            first = pref.getBoolean("isFirst", false);
            if(first==false){
                Log.d("Is first Time?", "first");
                SharedPreferences.Editor editor = pref.edit();

                startActivity(new Intent(this, signActivity.class));
                //앱 최초 실행시 하고 싶은 작업
                editor.putBoolean("isFirst",true);
                editor.commit();
            }else{
                startActivity(new Intent(this, autoLogin.class));
                finish();
            }






        }


    }
}