package com.example.fileagoapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    ImageView imagelogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        email=findViewById(R.id.emailid);
        password=findViewById(R.id.password);
        btn=findViewById(R.id.signbtn);
        imagelogo=findViewById(R.id.logo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                sendPostRequest();
            }
        });
       }
    private void sendPostRequest() {
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setUsername(email.getText().toString());
        loginRequest.setPassword(password.getText().toString());

        Call<User> call=RetrofitClient.getApiInterface().userlogin(loginRequest);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    String sta=response.body().getStatus();
                    Log.e("onResponse: ",sta);
                    if (sta.equals("success")) {
                        String token=response.body().getToken();
                        Intent intent=new Intent(HomeActivity.this,Workspace.class);
                        intent.putExtra("token",token);
                        Log.i(token, "onResponse: ");
                        startActivity(intent);
                        Toast.makeText(HomeActivity.this, "Logged in success", Toast.LENGTH_SHORT).show();
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