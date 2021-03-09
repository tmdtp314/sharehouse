package com.example.project3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class autoLogin extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String RoomID;
    private String password;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    private Button button;
    private TextView textView;
    private ListView lv;
    private Button btn_send, btn_back;
    private EditText edt_msg;

    private ArrayList<TalkVO> talk = new ArrayList<>();
    private Talkadapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_login);

        // 필요한 Bundle 데이터 -> (RoomID, name)

        RoomID = "a";//번들1
        password="123456";//번들2
        firebaseRemoteConfig=FirebaseRemoteConfig.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        loginEvent();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("msg");

        adapter = new Talkadapter(autoLogin.this, R.layout.talklayout, talk);
        textView = findViewById(R.id.tv_result);
        btn_send = findViewById(R.id.btn_send);
        lv = findViewById(R.id.listview);
        edt_msg = findViewById(R.id.edt_text);
        btn_back = findViewById(R.id.btn_back);

        textView.setText(RoomID+"_"+"오현종");

//        int firstVisibleItem = lv.getFirstVisiblePosition();
//        int oldCount = adapter.getCount();
//        View view = lv.getChildAt(0);
//        int pos = (view == null ? 0 :  view.getBottom());
//        lv.setSelectionFromTop(firstVisibleItem + adapter.getCount() - oldCount + 1, pos);

        lv.setSelection(adapter.getCount() - 1);
        lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//        lv.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        ref.addChildEventListener(new ChildEventListener() { // 하위 경로에 무슨일이 생겼을 때를 감지!
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // 자식이 추가되었을 때!
                TalkVO temp = dataSnapshot.getValue(TalkVO.class);
                talk.add(temp);
                Log.v("asdf", temp.getMsg());
                adapter = new Talkadapter(autoLogin.this, R.layout.talklayout, talk);
                lv.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // 자식이 바뀌었을 때!
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // 자식이 제거되었을 때!
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // 자식이 옮겨졌을 때!
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 에러가 발생하여 Listener가 제대로 동작하지 못했을 때!
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), fragment_mypage.class);
                startActivity(intent);
                finish();
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = edt_msg.getText().toString();

//                Date currentTime = Calendar.getInstance().getTime();
//                String date_text = new SimpleDateFormat("―――――――――――yyyy 년 MM월 dd일 EE요일―――――――――――", Locale.getDefault()).format(currentTime);
                ref.push().setValue(new TalkVO(R.drawable.bulb_img,"윤승주",msg, changeTime(), changeDate()));
                InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                edt_msg.setText("");



            }
        });
        lv.setTranscriptMode(lv.TRANSCRIPT_MODE_ALWAYS_SCROLL);

//로그인 인터페이스 리스너
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(autoLogin.this,"로그인",Toast.LENGTH_SHORT).show();
                } else {

                }


            }};
    }
    void loginEvent() {
        firebaseAuth.signInWithEmailAndPassword(RoomID, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //로그인 확인되었는지만 알려주는 메소드 로그인 안되었을 때만 작동
                if (!task.isSuccessful()) {
                    Toast.makeText(autoLogin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
    public static String changeTime(){
        String result="";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        result=sdf.format(timestamp);

        return result;
    }

    public static String changeDate(){
        String result="";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat( "―――――――――――yyyy 년 MM월 dd일 EE요일―――――――――――");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        result=sdf.format(timestamp);

        return result;
    }
}

