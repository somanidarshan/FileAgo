package com.example.fileagoapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class HomeActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button btn;
    LoginRequest loginRequest;
    ImageView imagelogo;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        email=findViewById(R.id.emailid);
        password=findViewById(R.id.password);
        btn=findViewById(R.id.signbtn);
        imagelogo=findViewById(R.id.logo);
        sharedPreferences=getSharedPreferences("session",MODE_PRIVATE);
        String emailid=sharedPreferences.getString("email",null);
        String pass=sharedPreferences.getString("pass",null);
        if(emailid!=null){
            Intent i=new Intent(HomeActivity.this,Workspace.class);
            sharedPreferences.edit().putString("emailid",emailid).apply();
            sharedPreferences.edit().putString("pass",pass).apply();
            startActivity(i);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                sendPostRequest();
            }
        });
       }
    private void sendPostRequest() {
        loginRequest=new LoginRequest();
        loginRequest.setUsername(email.getText().toString());
        loginRequest.setPassword(password.getText().toString());
        Call<User> call=RetrofitClient.getApiInterface().userlogin(loginRequest);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    String sta = response.body().getStatus();
                    Log.e("onResponse: ", sta);
                    if (sta.equals("success")) {
                        String token = response.body().getToken();
                        sharedPreferences.edit().putString("email",email.getText().toString()).apply();
                        sharedPreferences.edit().putString("pass",password.getText().toString()).apply();
                        sharedPreferences.edit().putString("token",token).apply();
                        Intent intent = new Intent(HomeActivity.this, Workspace.class);
                        Log.i(token, "onResponse: ");
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(HomeActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(HomeActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}