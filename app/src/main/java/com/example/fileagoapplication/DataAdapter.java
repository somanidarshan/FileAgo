package com.example.fileagoapplication;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<data> dataArrayList;
    private Context context;
    private String token;
    private AlertDialog.Builder dialogBuilder;
    private String prevuuid;
    private String actionbartitle;
    public DataAdapter(ArrayList<data> dataArrayList, Context context, String token,String prevuuid,String actionbartitle) {
        this.dataArrayList = dataArrayList;
        this.context = context;
        this.token = token;
        this.prevuuid=prevuuid;
        this.actionbartitle=actionbartitle;
    }

    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dataview,parent,false);
        return new DataAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolder holder, int position) {
            data data=dataArrayList.get(position);
            holder.foldername.setText(data.getName());
            if(data.getType().equals("File")){
                String filename = data.getName();
                String filenameArray[] = filename.split("\\.");
                String extension = filenameArray[filenameArray.length-1];
                System.out.println(extension);
                if(extension.equals("pdf")) {
                    holder.folderimage.setImageResource(R.drawable.ic_pdflogo);
                }
                else if(extension.equals("jpg") || extension.equals("png")){
                    holder.folderimage.setImageResource(R.drawable.imageicon);
                }
                else if(extension.equals("txt")){
                    holder.folderimage.setImageResource(R.drawable.ic_pdflogo);
                }
                else if(extension.equals("")){
                    holder.folderimage.setImageResource(R.drawable.ic_folder);
                }
            }
            holder.popupmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu=new PopupMenu(context,view);
                    popupMenu.getMenuInflater().inflate(R.menu.popupoptions,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.option_delete:
                                    Toast.makeText(context, "Delete Clicked", Toast.LENGTH_SHORT).show();
                                    delete(data.getUuid(),token,prevuuid,actionbartitle);

                                     break;
                                case R.id.option_move:
                                    Toast.makeText(context, "Move Clicked", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.option_rename:
                                    Toast.makeText(context, "Rename Clicked", Toast.LENGTH_SHORT).show();
                                    rename(data.getUuid(),token,prevuuid,actionbartitle);
                                    break;
                                case R.id.options_download:
                                    Toast.makeText(context, "Download Clicked", Toast.LENGTH_SHORT).show();
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
                    Intent i=new Intent(context,RecursiveDatalists.class);
                    i.putExtra("name",data.getName());
                    i.putExtra("uuid",data.getUuid());
                    i.putExtra("token",token);
                    context.startActivity(i);
                }
            });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView foldername;
        private ImageView popupmenu;
        private ImageView folderimage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderimage=itemView.findViewById(R.id.folder_image);
            foldername=itemView.findViewById(R.id.foldername);
            popupmenu=itemView.findViewById(R.id.moreoptions);
        }
    }

    public void delete(String uuid,String token,String prevuuid,String actionbartitle){
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

    public void rename(String uuid,String token,String prevuuid,String actionbartitle){
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


}
