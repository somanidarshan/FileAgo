package com.example.fileagoapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private ArrayList<groupdata> groupdataArrayList;
    private Context context;
    private String token;

    public GroupAdapter(ArrayList<groupdata> groupdataArrayList, Context context, String token) {
        this.groupdataArrayList = groupdataArrayList;
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.groupview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            groupdata groupdata=groupdataArrayList.get(position);
            holder.groupname.setText(groupdata.getGroup_name());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,GroupDataOnce.class);
                i.putExtra("groupname",groupdata.getGroup_name());
                i.putExtra("uuid",groupdata.getGroup_uuid());
                i.putExtra("token",token);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupdataArrayList.size();
    }
  public class ViewHolder extends RecyclerView.ViewHolder{
      private TextView groupname;

      public ViewHolder(@NonNull View itemView) {
          super(itemView);
          groupname=itemView.findViewById(R.id.groupname);


      }
  }
}
