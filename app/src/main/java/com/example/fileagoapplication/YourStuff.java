package com.example.fileagoapplication;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class YourStuff extends Fragment {
    private String string;
    private FloatingActionButton extendedFloatingActionButton;
    private FloatingActionButton fab1,fab2;
    private RecyclerView dataview;
    private ArrayList<data> dataArrayList;
    private DataAdapter dataAdapter;
    Boolean isfalse;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_your_stuff, container, false);
        dataview=view.findViewById(R.id.folderslist);
        fab1=view.findViewById(R.id.fileupload);
        fab2=view.findViewById(R.id.createfolder);
        fab1.hide();
        fab2.hide();
        extendedFloatingActionButton=view.findViewById(R.id.actions);
        isfalse=false;
       extendedFloatingActionButton.show();
        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isfalse) {
                    fab1.show();
                    fab2.show();
                    isfalse = true;
                }
                else{
                    fab1.hide();
                    fab2.hide();
                    isfalse=false;
                }
            }
        });
        Bundle data=getArguments();
        if(data!=null){
            string =data.getString("token");
        }
        dataArrayList=new ArrayList<>();
        dataAdapter=new DataAdapter(dataArrayList,getContext(),string);
        dataview.setLayoutManager(new LinearLayoutManager(getContext()));
        dataview.setAdapter(dataAdapter);
        getData(string);
        dataAdapter.notifyDataSetChanged();
            return view;
    }

    private void getData(String string) {
        retrofit2.Call<PersonalWorkspace> call=RetrofitClient.getApiInterface().getdirectory("Bearer "+string);
        call.enqueue(new Callback<PersonalWorkspace>() {
            @Override
            public void onResponse(retrofit2.Call<PersonalWorkspace> call, Response<PersonalWorkspace> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), "Succesfull", Toast.LENGTH_SHORT).show();
                    PersonalWorkspace workSpc= response.body();
                    ArrayList<data> folders=workSpc.getData();
                    String status=response.body().getStatus();
                    System.out.println(status);
                    for(int i=0;i<folders.size();i++){
                        dataArrayList.add(new data(folders.get(i).getName(),folders.get(i).getUuid()));
                    }
                    dataAdapter.notifyDataSetChanged();

                }
                else{
                    Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PersonalWorkspace> call, Throwable t) {

            }
        });
    }

}