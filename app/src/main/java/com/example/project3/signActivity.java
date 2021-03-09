package com.example.project3;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signActivity extends AppCompatActivity {
    private static final int PICK_FROM_ALBUM = 10;
    Button btn_start;
    private FirebaseAuth mAuth;
    EditText email,name,password;
    ImageView profile;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        btn_start = findViewById(R.id.btn_start);
        password=findViewById(R.id.password); //받아오기
        name=findViewById(R.id.id);
        email=findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
btn_start.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if(email.getText().toString()==null||name.getText().toString()==null||password.getText().toString()==null)
            return;


        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(signActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        UserModel userModel = new UserModel();
                        userModel.userName=name.getText().toString();
                        userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        String uid=task.getResult().getUser().getUid();
                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel) ;
                        signActivity.this.finish();

                        
                    }
                });
    }
});


    }


}