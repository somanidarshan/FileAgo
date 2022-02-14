package com.example.fileagoapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupDataAdapter extends RecyclerView.Adapter<GroupDataAdapter.ViewHolder> {
    private ArrayList<data> dataArrayList;
    private Context context;
    private String token;
    private String actionbartittle;
    private String groupuuid;


    public GroupDataAdapter(ArrayList<data> dataArrayList, Context context, String token, String actionbartittle, String groupuuid) {
        this.dataArrayList = dataArrayList;
        this.context = context;
        this.token = token;
        this.actionbartittle = actionbartittle;
        this.groupuuid = groupuuid;
    }

    @NonNull
    @Override
    public GroupDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dataview,parent,false);
        return new GroupDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupDataAdapter.ViewHolder holder, int position) {
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
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,RecursiveGroupDatalist.class);
                i.putExtra("components",actionbartittle);
                i.putExtra("name",data.getName());
                i.putExtra("uuid",data.getUuid());
                i.putExtra("token",token);
                i.putExtra("groupuuid",groupuuid);
                context.startActivity(i);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderimage=itemView.findViewById(R.id.folder_image);
            foldername=itemView.findViewById(R.id.foldername);
            popupmenu=itemView.findViewById(R.id.moreoptions);


        }
    }
}
