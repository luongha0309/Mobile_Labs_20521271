package com.example.mobile_labs_20521271;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public  class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    EditText fullnameInput, phoneInput, usernameInput, passwordInput;
    TextView loginLink;
    FirebaseAuth mAuth;
    String username;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AppCompatActivity activity = (AppCompatActivity) RegisterActivity.this;
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().hide();
        }


        registerButton = findViewById(R.id.btnRegister);
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        loginLink = findViewById(R.id.login_link);
        fullnameInput = findViewById(R.id.full_name);
        phoneInput = findViewById(R.id.phone);


        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName;
                String passWord;
                String fullName;
                String phoneNumber;

                userName = usernameInput.getText().toString();
                passWord = passwordInput.getText().toString();
                fullName = fullnameInput.getText().toString();
                phoneNumber = phoneInput.getText().toString();

                if(TextUtils.isEmpty(fullName)){
                    Toast.makeText(RegisterActivity.this, "Please enter full name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(RegisterActivity.this, "Please enter your phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(RegisterActivity.this, "Please enter username!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passWord)){
                    Toast.makeText(RegisterActivity.this, "Please enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
                    usernameInput.setError("Invalid Email");
                    usernameInput.setFocusable(true);
                }
                else if (passWord.length() < 6) {
                    passwordInput.setError("Password length must be greater than 6 character");
                    passwordInput.setFocusable(true);
                }
                else if(phoneNumber.length() < 10 || phoneNumber.length() > 11) {
                    phoneInput.setError("Phone number is invalid. Please enter again!");
                    phoneInput.setFocusable(true);
                }
                else {
                    //Neu nhu khong co loi thi thuc hien ham luu du lieu
                    registerUser(userName, passWord, fullName, phoneNumber);
                }

            }
        });

    }
    //Ham thuc hien luu du lieu len Realtime Database
    private void registerUser(String userName, final String passWord, final String fullName, final String phoneNumber) {
        mAuth.createUserWithEmailAndPassword(userName, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String username = user.getEmail();
                    String uid = user.getUid();
                    HashMap<Object, String>  users= new HashMap<>();
                    users.put("email", username);
                    users.put("uid", uid);
                    users.put("name", fullName);
                    users.put("phone", phoneNumber);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Users");
                    reference.child(uid).setValue(users);
                    Toast.makeText(RegisterActivity.this, "Registered User " + user.getEmail() + " successfully", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
            }
        });
    }
}
