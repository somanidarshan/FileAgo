package com.example.fileagoapplication;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.BitSet;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class UploadFilestoServer extends AppCompatActivity {
    private String filepath;
    private String token,tittle;
    private String uuid;
    private Uri filedata;
    private String filename;
    private   byte[] contentbyes;
    private CardView addpdf;
    private Button upload;
    private String actionbartittle;
    private String fileaccesskey;
    private TextView displsyname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_filesto_server);
        addpdf=findViewById(R.id.addpdf);
        upload=findViewById(R.id.uploadpdfBtn);
        displsyname=findViewById(R.id.pdfTitle);
        uuid=getIntent().getStringExtra("uuid");
        token=getIntent().getStringExtra("token");
        actionbartittle=getIntent().getStringExtra("name");
        fileaccesskey=getIntent().getStringExtra("filekey");

        addpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select  File"),1);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tittle= displsyname.getText().toString();
                    uploadfile(token,uuid,filedata,filename);
            }
        });
    }
    private void uploadfile(String token, String uuid, Uri filedata, String filename) {
        Call<UploadToken> call=RetrofitClient.getApiInterface().gettoken("Bearer "+token,uuid);
        call.enqueue(new Callback<UploadToken>() {
            @Override
            public void onResponse(Call<UploadToken> call, Response<UploadToken> response) {
                if(response.isSuccessful()){
                    String uploadtoken=response.body().getToken();
                    RequestBody requestBody1= RequestBody.create(MediaType.parse("multipart/form-data"),contentbyes);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file",filename,requestBody1);
                    Call<FilesUploadMsg> call1=RetrofitClient.getApiInterface().uploadfile(uploadtoken,fileToUpload);
                    call1.enqueue(new Callback<FilesUploadMsg>() {
                        @Override
                        public void onResponse(Call<FilesUploadMsg> call, Response<FilesUploadMsg> response) {
                                        if(response.isSuccessful()){
                                            Toast.makeText(UploadFilestoServer.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(UploadFilestoServer.this,RecursiveDatalists.class);
                                            i.putExtra("token",token);
                                            i.putExtra("uuid",uuid);
                                            i.putExtra("name",actionbartittle);
                                            i.putExtra("filekey",fileaccesskey);
                                            startActivity(i);
                                            finish();


                                        }
                                        else{
                                       Toast.makeText(UploadFilestoServer.this,"Sorry", Toast.LENGTH_SHORT).show();
                                        }
                        }
                        @Override
                        public void onFailure(Call<FilesUploadMsg> call, Throwable t) {
                            Toast.makeText(UploadFilestoServer.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UploadToken> call, Throwable t) {
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            filedata=data.getData();

            if(filedata.toString().startsWith("content://")){
                Cursor cursor=null;
                try {
                    cursor= UploadFilestoServer.this.getContentResolver().query(filedata,null,null,null,null);
                    if(cursor!=null && cursor.moveToFirst()){
                        filename=cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(filedata.toString().startsWith("file://")) {
                filename = new File(filedata.toString()).getName();
            }
            displsyname.setText(filename);
            try {
                InputStream inputStream=UploadFilestoServer.this.getContentResolver().openInputStream(filedata);
                 contentbyes=new byte[inputStream.available()];
                inputStream.read(contentbyes);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}