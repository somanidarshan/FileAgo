package com.example.fileagoapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class Groups extends Fragment {
    private String token;
    private RecyclerView groupview;
    private ArrayList<groupdata> groupdataArrayList;
    private GroupAdapter groupadapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_groups, container, false);
        groupview=view.findViewById(R.id.grouplist);
        Bundle data=getArguments();
        if(data!=null){
            token =data.getString("token");
        }
        groupdataArrayList=new ArrayList<>();
        groupadapter=new GroupAdapter(groupdataArrayList,getContext(),token);
        groupview.setLayoutManager(new LinearLayoutManager(getContext()));
        groupview.setAdapter(groupadapter);
        getGroups(token);
        groupadapter.notifyDataSetChanged();
        return view;
    }
    private void getGroups(String token) {
        retrofit2.Call<GroupWorkspace> call=RetrofitClient.getApiInterface().getgroups("Bearer "+token);
        call.enqueue(new Callback<GroupWorkspace>() {
            @Override
            public void onResponse(retrofit2.Call<GroupWorkspace> call, Response<GroupWorkspace> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), "Succesfull", Toast.LENGTH_SHORT).show();
                    GroupWorkspace groupSpc= response.body();
                    ArrayList<groupdata> grplist=groupSpc.getData();
                    String status=response.body().getStatus();
                    System.out.println(status);
                    for(int i=0;i<grplist.size();i++){
                        groupdataArrayList.add(new groupdata(grplist.get(i).getGroup_name(),grplist.get(i).getGroup_uuid()));
                    }
                    groupadapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getActivity(), token, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GroupWorkspace> call, Throwable t) {
            }
        });
    }
}