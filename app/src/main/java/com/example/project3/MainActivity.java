package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity<first> extends AppCompatActivity {
    EditText edt_id;
    EditText edt_pw;
    Button btn_login;
    RequestQueue requestQueue;
    //StringRequest stringRequest;
    StringRequest stringRequest;

    ImageView login_img;
    String id;
    String name; // frag1 사용자정보 이름
    String tel; // frag1 사용자정보 연락처
    String room; // frag1,2 사용자정보 방번호
    String count1; // frag2 기기 sr1
    String count2; // frag2 기기 sr2
    String count3; // frag3 기기 sr3
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_id = findViewById(R.id.edt_id);
        edt_pw = findViewById(R.id.edt_pw);

        login_img=findViewById(R.id.login_img);
        requestQueue = Volley.newRequestQueue(this);
        String url="http://172.30.1.49:8083/LoginServer/AllSelectServlet";
        stringRequest  = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("false")) {
                    Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
                try {

                    JSONArray array = new JSONArray(response);
                    name = array.getJSONObject(0).getString("name"); // 필요한 데이터 저장, 필요 데이터 종류에 따라 리스트 생성해주기
                    name = new String(name.getBytes("8859_1"), "utf-8");
                    tel = array.getJSONObject(0).getString("tel");

                    room = array.getJSONObject(0).getString("room");


                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("tel", tel);
                    intent.putExtra("room", room);

                    startActivity(intent);
                    finish();
                } catch (JSONException | UnsupportedEncodingException e) {
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
                data.put("id", edt_id.getText().toString());
                data.put("pw", edt_pw.getText().toString());

                return data;
            }
        };

        stringRequest.setTag("MAIN");

        login_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requestQueue.add(stringRequest); // requestQueu2 통로로 AllSelect 데이터 요청
            }
        });


    }


    }
