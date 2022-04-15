package com.example.fileagoapplication;
import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class GroupDataAdapter extends RecyclerView.Adapter<GroupDataAdapter.ViewHolder> {
    private ArrayList<data> dataArrayList;
    private Context context;
    private AlertDialog.Builder dialogBuilder;
    private String token;
    private String actionbartittle;
    private String groupuuid;
    private Long updated;
    private String prevuuid;
    private String actionbartitle;
    private String fileaccesskey;
    private long size;
    public GroupDataAdapter(ArrayList<data> dataArrayList, Context context, String token, String actionbartittle, String groupuuid,String fileaccesskey) {
        this.dataArrayList = dataArrayList;
        this.context = context;
        this.token = token;
        this.actionbartittle = actionbartittle;
        this.groupuuid = groupuuid;
        this.fileaccesskey=fileaccesskey;
    }
    @NonNull
    @Override
    public GroupDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dataview,parent,false);
        return new GroupDataAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull GroupDataAdapter.ViewHolder holder, int position) {
        data data=dataArrayList.get(position);
        String shortfoldername="";
        if(data.getName().length()>32){
            for(int i=0;i<15;i++){
                shortfoldername+=data.getName().charAt(i);
            }
            shortfoldername+="...";
            for(int j=data.getName().length()-10;j<data.getName().length();j++){
                shortfoldername+=data.getName().charAt(j);
            }
            holder.foldername.setText(shortfoldername);
        }
        else {
            holder.foldername.setText(data.getName());
        }
        updated=data.getUpdated();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat simpletimeFormat=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        String date=simpleDateFormat.format(updated);
        String time=simpletimeFormat.format(updated);
        String combine=time+" "+date;
        holder.updatedtext.setText(combine);


        if(data.getType().equals("File")){
            String filename = data.getName();
            String filenameArray[] = filename.split("\\.");
            String extension = filenameArray[filenameArray.length-1];
            System.out.println(extension);
            if(extension.equals("pdf")) {
                holder.folderimage.setImageResource(R.drawable.ic_pdflogo);
            }
            else if(extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg")){
                holder.folderimage.setImageResource(R.drawable.imageicon);
            }
            else if(extension.equals("txt")){
                holder.folderimage.setImageResource(R.drawable.ic_pdflogo);
            }
            else if(extension.equals("docx")){
                holder.folderimage.setImageResource(R.drawable.docs_logo);
            }
            else if(extension.equals("mp3")){
                holder.folderimage.setImageResource(R.drawable.audio_file);
            }
            else if(extension.equals("")){
                holder.folderimage.setImageResource(R.drawable.ic_folder);
            }
            size=data.getSize();
            String filessize= "";
            if (size >= 1073741824 ) {
                filessize= (size / 1073741824)+ " GB";
            }
            else if (size >= 1048576) {
                filessize= (size / 1048576) + " MB";
            }
           else if (size >= 1024) {
                filessize= (size / 1024)+ " KB";
            }
            else {
                filessize= size + " Bytes";
            }
            holder.filesize.setText(filessize);
        }
        holder.popupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(context,view);
                popupMenu.getMenuInflater().inflate(R.menu.popupoptions,popupMenu.getMenu());
                popupMenu.getMenu().findItem(R.id.options_download).setVisible(false);
                popupMenu.getMenu().findItem(R.id.option_delete).setVisible(false);
                popupMenu.show();
                Call<MyAccessWorkspace> call=RetrofitClient.getApiInterface().myaccess("Bearer "+token,data.getUuid());
                call.enqueue(new Callback<MyAccessWorkspace>() {
                    @Override
                    public void onResponse(Call<MyAccessWorkspace> call, Response<MyAccessWorkspace> response) {
                        if(response.isSuccessful()){
                            MyAccessWorkspace myAccessWorkspace=response.body();
                            String[] access=myAccessWorkspace.getData();
                            Set<String> ss=new HashSet();
                            for(int i=0;i<access.length;i++){
                                ss.add(access[i]);
                            }
                            if(ss.contains("download") && data.getType().equals("File")){
                                popupMenu.getMenu().findItem(R.id.options_download).setVisible(true);
                            }
                            if(ss.contains("delete")){
                                popupMenu.getMenu().findItem(R.id.option_delete).setVisible(true);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<MyAccessWorkspace> call, Throwable t) {

                    }
                });
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.option_delete:
                                Toast.makeText(context, "Delete Clicked", Toast.LENGTH_SHORT).show();
                                delete(data.getUuid(),token,prevuuid,actionbartitle);
                                break;

                            case R.id.option_rename:
                                Toast.makeText(context, "Rename Clicked", Toast.LENGTH_SHORT).show();
                                rename(data.getUuid(),token,prevuuid,actionbartitle);
                                break;
                            case R.id.options_download:
                                Toast.makeText(context, "Download Clicked", Toast.LENGTH_SHORT).show();
                                downloadfile(data.getUuid(),data.getName(),fileaccesskey,token);
                                break;
                            case R.id.option_fav:
                                addtofav(data.getUuid(),token);
                                break;
                        }
                        return true;
                    }
                });
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.getType().equals("Dir")) {
                    Intent i = new Intent(context, RecursiveGroupDatalist.class);
                    i.putExtra("components", actionbartittle);
                    i.putExtra("name", data.getName());
                    i.putExtra("uuid", data.getUuid());
                    i.putExtra("token", token);
                    i.putExtra("groupuuid", groupuuid);
                    i.putExtra("filekey",fileaccesskey);
                    context.startActivity(i);
                }
            }
        });
    }
    private void downloadfile(String uuid, String name, String fileaccesskey, String token) {
        String filename=name;
        name= URLEncoder.encode(name);
        System.out.println(name);
        Call<ResponseBody> call=RetrofitClient.getApiInterface().downloadfile(fileaccesskey,uuid,name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                        }
                    }
                    String u = "https://ocean.fileago.com/resources/auth/download/" + fileaccesskey + "/" + uuid + "/999999999999999/" + filename;
                    DownloadManager.Request request=new DownloadManager.Request(Uri.parse(u));
                    request.setTitle(filename);
                    request.setDescription("Downloading "+filename);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.allowScanningByMediaScanner();
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);
                    DownloadManager dm= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Error in Downloading", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void addtofav(String uuid, String token) {
        Fav fav=new Fav(true);
        Call<Void> call=RetrofitClient.getApiInterface().addtofavorite("Bearer "+token,uuid,fav);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
    private void rename(String uuid, String token, String prevuuid, String actionbartitle) {
        dialogBuilder=new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Enter the New-Name of the Folder");
        final EditText filename=new EditText(context);
        filename.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogBuilder.setView(filename);
        dialogBuilder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RenameFile renameFile=new RenameFile();
                renameFile.setNew_name(filename.getText().toString());
                Call<Void> call=RetrofitClient.getApiInterface().renamefile("Bearer "+token,uuid,renameFile);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context, "File Renamed Success", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(context,RecursiveDatalists.class);
                            i.putExtra("uuid",prevuuid);
                            i.putExtra("token",token);
                            i.putExtra("name",actionbartitle);
                            context.startActivity(i);
                            ((Activity)context).finish();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        dialogBuilder.show();
    }
    private void delete(String uuid, String token, String prevuuid, String actionbartitle) {
        Call<Void> call=RetrofitClient.getApiInterface().deletenode("Bearer "+token,uuid);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    dataArrayList.clear();
                    Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show();
                    System.out.print(prevuuid);
                    if(prevuuid.equals("")){
                        Intent i=new Intent(context,Workspace.class);
                        i.putExtra("token",token);
                        context.startActivity(i);
                        ((Activity)context).finish();
                    }
                    else {
                        Intent i = new Intent(context, RecursiveDatalists.class);
                        i.putExtra("uuid", prevuuid);
                        i.putExtra("token", token);
                        i.putExtra("name", actionbartitle);
                        context.startActivity(i);
                        ((Activity) context).finish();
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call,Throwable t) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView foldername;
        private ImageView popupmenu;
        private ImageView folderimage;
        private TextView updatedtext;
        private TextView filesize;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderimage=itemView.findViewById(R.id.folder_image);
            foldername=itemView.findViewById(R.id.foldername);
            popupmenu=itemView.findViewById(R.id.moreoptions);
            updatedtext=itemView.findViewById(R.id.updated);
            filesize=itemView.findViewById(R.id.filesize);

        }
    }
}
