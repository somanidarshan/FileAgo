package com.example.fileagoapplication;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

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
    private Button upload;
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
                Intent pickfile=new Intent(Intent.ACTION_GET_CONTENT);
                pickfile.setType("*/*");
                pickfile=Intent.createChooser(pickfile,"Choose a file");
                startActivityForResult(pickfile,1);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tittle= displsyname.getText().toString();
                if(tittle.isEmpty()){
                    Toast.makeText(UploadFilestoServer.this, "Title empty", Toast.LENGTH_SHORT).show();
                }
                else if(filedata==null){
                    Toast.makeText(UploadFilestoServer.this, "Filedata empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadfile(token,uuid,filedata,filename);
                }
            }
        });
    }
    private void uploadfile(String token, String uuid,Uri filedata,String filename) {
        Call<UploadToken> call=RetrofitClient.getApiInterface().gettoken("Bearer "+token,uuid);
        call.enqueue(new Callback<UploadToken>() {
            @Override
            public void onResponse(Call<UploadToken> call, Response<UploadToken> response) {
                if(response.isSuccessful()){
                    String uploadtoken=response.body().getToken();
//                    RequestBody requestBody1= RequestBody.create(MediaType.parse("multipart/form-data"),filepath);
//                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file",filename,requestBody1);

                    File file=new File(String.valueOf(filedata));
//                    File file=new File(filepath);
                   // File file = new File(filedata.getPath());
                    RequestBody requestBody1= RequestBody.create(MediaType.parse("image/jpg"),filepath);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file",filename,requestBody1);
                    System.out.println(requestBody1);
                    Call<FilesUploadMsg> call1=RetrofitClient.getApiInterface().uploadfile(uploadtoken,fileToUpload);
                    call1.enqueue(new Callback<FilesUploadMsg>() {
                        @Override
                        public void onResponse(Call<FilesUploadMsg> call, Response<FilesUploadMsg> response) {
                                        if(response.isSuccessful()){
                                            Toast.makeText(UploadFilestoServer.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            //System.out.println(response.body().toString());
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            filedata=data.getData();
            System.out.println(data.getData().toString());
            filepath=getPath(filedata);
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
            System.out.println(filename);
        }
    }
private String getPath(Uri uri){
        String[] projection={MediaStore.Images.Media.DATA};
        Cursor cursor=managedQuery(uri,projection,null,null,null);
        int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
}
}