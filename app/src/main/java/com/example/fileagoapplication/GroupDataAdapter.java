package com.example.fileagoapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foldername=itemView.findViewById(R.id.foldername);
        }
    }
}
