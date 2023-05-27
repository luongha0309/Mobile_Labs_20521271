package com.example.mobile_labs_20521271;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button loginBtn;
    TextView registerLink;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //An thanh tieu de
        AppCompatActivity activity = (AppCompatActivity) LoginActivity.this;
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().hide();
        }

        FirebaseApp.initializeApp(this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        registerLink = findViewById(R.id.register_link);
        loginBtn = findViewById(R.id.btnLogin);
        firebaseAuth = FirebaseAuth.getInstance();

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName, passWord;

                userName = username.getText().toString();
                passWord = password.getText().toString();

                //Kiem tra xem neu nhu Username hay Password co trong hay khong
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(LoginActivity.this, "Plese enter your email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(passWord)){
                    Toast.makeText(LoginActivity.this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Sau khi khong co loi thi thuc hien dang nhap bang Email va Password
                firebaseAuth.signInWithEmailAndPassword(userName, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(LoginActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Incorrect username or password, please enter again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

}