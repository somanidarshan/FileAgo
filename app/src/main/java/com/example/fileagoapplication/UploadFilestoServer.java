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
    private CardView addpdf;
    private FileInputStream fip;
    private StringBuilder filecontent;
    private Button upload;
    FileInputStream fis;
    String msg;
    String mediaPath;
    String[] mediaColumns = {MediaStore.Video.Media._ID};
    private Bitmap bitmap;
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
        addpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent pickfile=new Intent(Intent.ACTION_GET_CONTENT);
//
//                pickfile=Intent.createChooser(pickfile,"Choose a file");
//                startActivityForResult(pickfile,1);
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
                    uploadfile(token,uuid,filename,filecontent);
            }
        });
    }
    private void uploadfile(String token, String uuid, String filename, StringBuilder filecontent) {
        Call<UploadToken> call=RetrofitClient.getApiInterface().gettoken("Bearer "+token,uuid);
        call.enqueue(new Callback<UploadToken>() {
            @Override
            public void onResponse(Call<UploadToken> call, Response<UploadToken> response) {
                if(response.isSuccessful()){
                    String uploadtoken=response.body().getToken();
                    File file=new File(filepath);
                    RequestBody requestBody1= RequestBody. create(MediaType.parse("multipart/form-data"),file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file",filename,requestBody1);
                    Call<FilesUploadMsg> call1=RetrofitClient.getApiInterface().uploadfile(uploadtoken,fileToUpload);
                    call1.enqueue(new Callback<FilesUploadMsg>() {
                        @Override
                        public void onResponse(Call<FilesUploadMsg> call, Response<FilesUploadMsg> response) {
                                        if(response.isSuccessful()){
                                            Toast.makeText(UploadFilestoServer.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
            System.out.print("11111111111111111111111111");
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
            filepath=filedata.toString();
            File file=new File(filepath);
            try {
                 fip=new FileInputStream(file);
                 InputStreamReader isr=new InputStreamReader(fip);
                 BufferedReader buff=new BufferedReader(isr);
                String line=null;
                while((line=buff.readLine())!=null){
                    filecontent.append(line+"\n");
                }
                fip.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            try {
//                fis=openFileInput(filename);
//                byte[] b=new byte[fis.available()];
//                fis.read(b);
//                msg=b.toString();
//                fis.close();
//                Toast.makeText(UploadFilestoServer.this, msg, Toast.LENGTH_SHORT).show();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


//            FileInputStream fis = null;
//            try {
//                fis = UploadFilestoServer.this.openFileInput(filename);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader bufferedReader = new BufferedReader(isr);
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while (true) {
//                try {
//                    if (!((line = bufferedReader.readLine()) != null)) break;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                sb.append(line);
//            }
//            ByteArrayOutputStream byteArrayOutputStream= null;
//            try {
//                byteArrayOutputStream = (ByteArrayOutputStream) getContentResolver().openOutputStream(filedata);
//                filepath=byteArrayOutputStream.toByteArray().toString();
//                System.out.print(filepath);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

//            FileInputStream fis=null;
//            try {
//                fis=openFileInput(filename);
//                InputStreamReader isr=new InputStreamReader(fis);
//                BufferedReader br=new BufferedReader(isr);
//                StringBuilder sb=new StringBuilder();
//                String content;
//                while((content=br.readLine())!=null){
//                    sb.append(content).append("\n");
//                }
//                String filecontent=sb.toString();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            finally {
//                if(fis!=null){
//                    try {
//                        fis.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
        }
    }
}